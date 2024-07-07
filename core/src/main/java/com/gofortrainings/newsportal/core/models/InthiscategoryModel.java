package com.gofortrainings.newsportal.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.jcr.query.Query;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class})
public class InthiscategoryModel {

    private static final Logger LOG = LoggerFactory.getLogger(InthiscategoryModel.class);

    @ValueMapValue
    private String title;

    @ScriptVariable
    private ValueMap pageProperties;

    @SlingObject
    private ResourceResolver resolver;

    private List<ArticleModel> articleModelList;

    @PostConstruct
    protected void init() {
        articleModelList = new ArrayList<>();
        String[] tags = pageProperties.get("cq:tags", String[].class);
        String categoryTag = findCategoryTag(tags);//"newsportal:categories"
        if (categoryTag != null) {
            String queryString = "SELECT * FROM [cq:Page] AS s WHERE ISDESCENDANTNODE([/content/newsportal]) AND s.[jcr:content/cq:tags] LIKE '" + categoryTag + "%'";
            try {
                Iterator<Resource> result = resolver.findResources(queryString, Query.JCR_SQL2);
                while (result.hasNext()) {
                    Resource resource = result.next();
                    Resource articleResource = resolver.getResource(resource.getPath() + "/jcr:content/root/container/article_grid/left-container/article_details");
                    if (articleResource != null) {
                        ArticleModel articleModel = articleResource.adaptTo(ArticleModel.class);
                        if (articleModel != null) {
                            articleModelList.add(articleModel);
                        }
                    }
                }
            } catch (Exception e) {
                LOG.error("Error retrieving resources: ", e);
            }
        }
    }

    private String findCategoryTag(String[] tags) {
        if (tags != null) {
            for (String tag : tags) {
                if (tag.startsWith("newsportal:categories")) {
                    String[] splitTag = tag.split("/");
                    if (splitTag.length >= 2) {
                        return splitTag[0] + "/" + splitTag[1];
                    }
                }
            }
        }
        return null;
    }

    public String getTitle() {
        return title;
    }

    public List<ArticleModel> getArticleModelList() {
        return articleModelList;
    }
}
