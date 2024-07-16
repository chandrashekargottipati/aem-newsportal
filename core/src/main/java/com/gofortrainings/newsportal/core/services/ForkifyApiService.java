package com.gofortrainings.newsportal.core.services;

import com.gofortrainings.newsportal.core.config.ForkifyApiConfig;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

@Component(service = ForkifyApiService.class)
@Designate(ocd = ForkifyApiConfig.class)
public class ForkifyApiService {

    private ForkifyApiConfig config;

    @Activate
    @Modified
    protected void activate(ForkifyApiConfig config) {
        this.config = config;
    }

    public String getFullApiUrl(String recipeId) {
        if (config.enableApi()) {
            return config.apiBaseUrl() + "/" + recipeId;
        }
        return null; // or handle disabled state as appropriate
    }

    public String getFullApisearchUrl(String query) {
        if (config.enableApi()) {
            return config.apiBaseSearchUrl() + "=" + query;
        }
        return null; // or handle disabled state as appropriate
    }

    public boolean isApiEnabled() {
        return config.enableApi();
    }
}
