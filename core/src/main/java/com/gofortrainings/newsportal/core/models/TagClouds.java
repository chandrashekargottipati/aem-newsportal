package com.gofortrainings.newsportal.core.models;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TagClouds {

    @ValueMapValue
    private String title;

    @ScriptVariable
    private ValueMap pageProperties;

    @SlingObject
    private ResourceResolver resolver;

    private List<String> tagList;

    @PostConstruct
    protected void init() {
        String[] tags = pageProperties.get("cq:tags", String[].class);
        if (tags != null && tags.length > 0) {
            tagList = new ArrayList<>();
            TagManager tagManager = resolver.adaptTo(TagManager.class);
            if (tagManager != null) {
                for (String tagId : tags) {
                    Tag tagObj = tagManager.resolve(tagId);
                    if (tagObj != null) {
                        tagList.add(tagObj.getTitle());
                    }
                }
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public List<String> getTagList() {
        return tagList;
    }
}
