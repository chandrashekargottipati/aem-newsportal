package com.gofortrainings.newsportal.core.services.impl;

import com.gofortrainings.newsportal.core.config.RecipeServiceConfig;
import com.gofortrainings.newsportal.core.services.RecipeService;
import com.gofortrainings.newsportal.core.util.Recipe;
import com.gofortrainings.newsportal.core.util.SearchResult;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component(service = RecipeService.class, immediate = true)
@Designate(ocd = RecipeServiceConfig.class)
public class RecipeServiceImpl implements RecipeService {

    private static final Logger LOG = LoggerFactory.getLogger(RecipeServiceImpl.class);

    private String apiBaseUrl;
    private String apiSearchUrl;
    private boolean enableApi;

    @Activate
    @Modified
    protected void activate(RecipeServiceConfig config) {
        this.apiBaseUrl = config.apiBaseUrl();
        this.apiSearchUrl = config.apiSearchUrl();
        this.enableApi = config.enableApi();
    }

    @Override
    public Recipe loadRecipe(String id) throws IOException {
        if (!enableApi) {
            return null;
        }
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(apiBaseUrl + "/" + id);
            try (CloseableHttpResponse response = client.execute(request)) {
                String json = EntityUtils.toString(response.getEntity());
                JSONObject data = new JSONObject(json).getJSONObject("data");
                JSONObject recipeJson = data.getJSONObject("recipe");

                Recipe recipe = new Recipe();
                recipe.setId(recipeJson.getString("id"));
                recipe.setTitle(recipeJson.getString("title"));
                recipe.setPublisher(recipeJson.getString("publisher"));
                recipe.setSourceUrl(recipeJson.getString("source_url"));
                recipe.setImage(recipeJson.getString("image_url"));
                recipe.setServings(recipeJson.getInt("servings"));
                recipe.setCookingTime(recipeJson.getInt("cooking_time"));

                // Handle ingredients array correctly
                JSONArray ingredientsJson = recipeJson.getJSONArray("ingredients");
                List<Recipe.Ingredient> ingredients = new ArrayList<>();

                LOG.debug("Ingredients JSON Array: {}", ingredientsJson);

                for (int i = 0; i < ingredientsJson.length(); i++) {
                    JSONObject ingredientJson = ingredientsJson.getJSONObject(i);
                    LOG.debug("Ingredient JSON Object: {}", ingredientJson);

                    Recipe.Ingredient ingredient = new Recipe.Ingredient();
                    ingredient.setQuantity(ingredientJson.optString("quantity", "N/A"));
                    ingredient.setUnit(ingredientJson.optString("unit", ""));
                    ingredient.setDescription(ingredientJson.optString("description", ""));

                    ingredients.add(ingredient);
                }
                recipe.setIngredients(ingredients);

                return recipe;

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<SearchResult> searchRecipe(String search) throws IOException {
        if (!enableApi) {
            return new ArrayList<>();
        }
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(apiSearchUrl + "=" + search);
            try (CloseableHttpResponse response = client.execute(request)) {
                String json = EntityUtils.toString(response.getEntity());
                JSONObject data = new JSONObject(json).getJSONObject("data");
                JSONArray recipesJson = data.getJSONArray("recipes");

                List<SearchResult> results = new ArrayList<>();
                for (int i = 0; i < recipesJson.length(); i++) {
                    JSONObject rec = recipesJson.getJSONObject(i);
                    SearchResult result = new SearchResult();
                    result.setId(rec.getString("id"));
                    result.setTitle(rec.getString("title"));
                    result.setPublisher(rec.getString("publisher"));
                    result.setImage(rec.getString("image_url"));

                    results.add(result);
                }

                return results;
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
