package com.gofortrainings.newsportal.core.models;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class},defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NewsCategoriesModel {
    @ValueMapValue
    private String title;

    @ValueMapValue
    @Default(values = "newsportal:categories")
    private String category;

    @SlingObject
    private ResourceResolver resolver;

    private Map<String, Long> categories;

    @PostConstruct
    public void init() {
        categories = new HashMap<>();
        TagManager tagManager = resolver.adaptTo(TagManager.class);
        if (tagManager != null) {
            Tag tag = tagManager.resolve(category);
            if (tag != null) {
                Iterator<Tag> tagIterator = tag.listChildren();
                while (tagIterator.hasNext()) {
                    Tag childTag = tagIterator.next();
                    categories.put(childTag.getTitle(), childTag.getCount());
                }
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public Map<String, Long> getCategories() {
        return categories;
    }
}
