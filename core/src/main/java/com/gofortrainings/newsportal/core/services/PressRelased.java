package com.gofortrainings.newsportal.core.services;

import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class PressRelased {
    @Reference
    ArticleDetails articleDetails;
    private static final Logger LOG = LoggerFactory.getLogger(PressRelased.class);

    @Activate
    public void active() {
        LOG.info(articleDetails.getDetails());
        LOG.info("pressrelased active method ");
    }

    @Deactivate
    public void deactive() {
        LOG.info("pressrelased deactive method ");
    }

    @Modified
    public void update() {
        LOG.info("pressrelased update method ");
    }
}
