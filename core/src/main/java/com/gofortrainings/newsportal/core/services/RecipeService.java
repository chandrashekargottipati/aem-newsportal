package com.gofortrainings.newsportal.core.services;

import com.gofortrainings.newsportal.core.util.Recipe;
import com.gofortrainings.newsportal.core.util.SearchResult;

import java.io.IOException;
import java.util.List;

public interface RecipeService {
    Recipe loadRecipe(String id) throws IOException;
    List<SearchResult> searchRecipe(String query) throws IOException;
}
