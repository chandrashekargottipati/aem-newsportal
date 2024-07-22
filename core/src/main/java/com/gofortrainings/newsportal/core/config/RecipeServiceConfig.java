package com.gofortrainings.newsportal.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Recipe API Configuration")
public @interface RecipeServiceConfig {

    @AttributeDefinition(name = "API Base URL", description = "Base URL for the Recipe API")
    String apiBaseUrl() default "https://forkify-api.herokuapp.com/api/v2/recipes";

    @AttributeDefinition(name = "API Search URL", description = "Base URL for the Recipe API search endpoint")
    String apiSearchUrl() default "https://forkify-api.herokuapp.com/api/v2/recipes?search";

    @AttributeDefinition(name = "Enable API", description = "Enable or disable the Recipe API integration")
    boolean enableApi() default true;
}
