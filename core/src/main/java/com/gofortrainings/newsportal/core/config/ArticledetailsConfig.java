package com.gofortrainings.newsportal.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface ArticledetailsConfig {

    @AttributeDefinition(name = "Article api")
    public String getArticleApi() default "https://gorest.co.in/public/v2/posts";

    @AttributeDefinition(name = "Article client id")
    public String getClientid()default "8019";

    @AttributeDefinition(name = "Enable/Desable")
    public boolean getEnable() default true;
}
