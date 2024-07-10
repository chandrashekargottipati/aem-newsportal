package com.gofortrainings.newsportal.core.models;

import com.gofortrainings.newsportal.core.services.ArticleDetails;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArticleDetailModel {

    private static final Logger log = LoggerFactory.getLogger(ArticleDetailModel.class);

    @OSGiService
    private ArticleDetails articleDetails;

    private String jsonData;

    @PostConstruct
    protected void init() {
        log.info("Initializing ArticleDetailModel");
        jsonData = articleDetails.getDetails();
        log.info("Received JSON data: {}", jsonData);
    }

    public String getJsonData() {
        return jsonData;
    }
}
