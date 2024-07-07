package com.gofortrainings.newsportal.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class})
public class ArticleModel {

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String image;

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }
}
