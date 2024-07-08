package com.gofortrainings.newsportal.core.models;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class})
public class RelatedArticle {

    @ValueMapValue
    private String title;

    @ScriptVariable
    private ValueMap pageProperties;

    @SlingObject
    private ResourceResolver resolver;

    private List<ArticleModel> relatedArticleList;

    @PostConstruct
    public void init() {
        relatedArticleList = new ArrayList<>();
        String[] tags = pageProperties.get("cq:tags", String[].class);
        String category = getTagResource(tags);
        if (category != null) {
            TagManager tagManager = resolver.adaptTo(TagManager.class);
            if (tagManager != null) {
                Tag categoryTagObject = tagManager.resolve(category);
                if (categoryTagObject != null) {
                    Iterator<Resource> resourceIterator = categoryTagObject.find();
                    while (resourceIterator.hasNext()) {
                        Resource resource = resourceIterator.next();
                        Resource resourcePath = resolver.getResource(resource.getPath() + "/root/container/article_grid/left-container/article_details");
                        if (resourcePath != null) {
                            ArticleModel articleModel = resourcePath.adaptTo(ArticleModel.class);
                            if (articleModel != null) {
                                relatedArticleList.add(articleModel);
                            }
                        }
                    }
                }
            }
        }
    }

    private String getTagResource(String[] tags) {
        if (tags != null) {
            for (String tag : tags) {
                if (tag.startsWith("newsportal:categories")) {
                    return tag;
                }
            }
        }
        return null;
    }

    public String getTitle() {
        return title;
    }

    public List<ArticleModel> getRelatedArticleList() {
        return relatedArticleList;
    }
}
