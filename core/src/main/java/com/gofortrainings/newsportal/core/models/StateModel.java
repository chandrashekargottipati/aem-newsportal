package com.gofortrainings.newsportal.core.models;

import com.gofortrainings.newsportal.core.services.RecipeService;
import com.gofortrainings.newsportal.core.util.Recipe;
import com.gofortrainings.newsportal.core.util.SearchResult;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class StateModel {

    private static final Logger LOG = LoggerFactory.getLogger(StateModel.class);

    @OSGiService
    private RecipeService recipeService;

    @ValueMapValue
    private String recipeId;

    @ValueMapValue
    private String searchQuery;

    private Recipe recipe;
    private List<SearchResult> searchResults;

    @PostConstruct
    protected void init() {
        try {
            if (recipeId != null && !recipeId.isEmpty()) {
                this.recipe = recipeService.loadRecipe(recipeId);
            }
            if (searchQuery != null && !searchQuery.isEmpty()) {
                this.searchResults = recipeService.searchRecipe(searchQuery);
            }
        } catch (IOException e) {
            LOG.error("Error loading recipe or search results", e);
        }
    }

    // Getters for use in HTL

    public Recipe getRecipe() {
        return recipe;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public List<SearchResult> getSearchResults() {
        return searchResults;
    }
}
