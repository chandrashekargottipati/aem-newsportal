package com.gofortrainings.newsportal.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NewsportalUtil {
    public static List<ArticleModel> getArticleList(Iterator<Resource> resourceIterator, ResourceResolver resolver) {
        List<ArticleModel> relatedArticleList = new ArrayList<>();
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

        return relatedArticleList;
    }
}
