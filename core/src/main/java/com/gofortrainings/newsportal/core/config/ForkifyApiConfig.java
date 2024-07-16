package com.gofortrainings.newsportal.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Forkify API Configuration")
public @interface ForkifyApiConfig {

    @AttributeDefinition(name = "API Base URL", description = "Base URL for the Forkify API")
    String apiBaseUrl() default "https://forkify-api.herokuapp.com/api/v2/recipes";

    @AttributeDefinition(name = "API Base URL", description = "Base URL for the Forkify API")
    String apiBaseSearchUrl() default "https://forkify-api.herokuapp.com/api/v2/recipes?search";



    @AttributeDefinition(name = "Enable API", description = "Enable or disable the Forkify API integration")
    boolean enableApi() default true;
}
