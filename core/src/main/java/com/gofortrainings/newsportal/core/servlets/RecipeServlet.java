package com.gofortrainings.newsportal.core.servlets;

import com.gofortrainings.newsportal.core.services.RecipeService;
import com.gofortrainings.newsportal.core.util.Recipe;
import com.gofortrainings.newsportal.core.util.SearchResult;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import java.io.IOException;
import java.util.List;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/recipes",
                "sling.servlet.methods=GET"
        }
)
public class RecipeServlet extends SlingAllMethodsServlet {

    @Reference
    private RecipeService recipeService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        String query = request.getParameter("query");
        JSONObject jsonResponse = new JSONObject();

        try {
            if (id != null) {
                Recipe recipe = recipeService.loadRecipe(id);
                if (recipe != null) {
                    jsonResponse.put("recipe", convertRecipeToJson(recipe));
                } else {
                    response.setStatus(SlingHttpServletResponse.SC_NOT_FOUND);
                    jsonResponse.put("error", "Recipe not found");
                }
            } else if (query != null) {
                List<SearchResult> results = recipeService.searchRecipe(query);
                JSONArray jsonResults = new JSONArray();
                for (SearchResult result : results) {
                    jsonResults.put(convertSearchResultToJson(result));
                }
                jsonResponse.put("results", jsonResults);
            } else {
                response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("error", "Missing 'id' or 'query' parameter");
            }

            response.setContentType("application/json");
            response.getWriter().write(jsonResponse.toString());
        } catch (JSONException e) {
            throw new RuntimeException("Error creating JSON response", e);
        }
    }

    private JSONObject convertRecipeToJson(Recipe recipe) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", recipe.getId());
        json.put("title", recipe.getTitle());
        json.put("publisher", recipe.getPublisher());
        json.put("sourceUrl", recipe.getSourceUrl());
        json.put("image", recipe.getImage());
        json.put("servings", recipe.getServings());
        json.put("cookingTime", recipe.getCookingTime());

        JSONArray ingredientsJson = new JSONArray();
        for (Recipe.Ingredient ingredient : recipe.getIngredients()) {
            JSONObject ingredientJson = new JSONObject();
            ingredientJson.put("quantity", ingredient.getQuantity());
            ingredientJson.put("unit", ingredient.getUnit());
            ingredientJson.put("description", ingredient.getDescription());
            ingredientsJson.put(ingredientJson);
        }
        json.put("ingredients", ingredientsJson);

        return json;
    }

    private JSONObject convertSearchResultToJson(SearchResult result) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", result.getId());
        json.put("title", result.getTitle());
        json.put("publisher", result.getPublisher());
        json.put("image", result.getImage());
        return json;
    }
}
