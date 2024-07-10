package com.gofortrainings.newsportal.core.services;

import com.gofortrainings.newsportal.core.config.ArticledetailsConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component(service = ArticleDetails.class)
@Designate(ocd = ArticledetailsConfig.class)
public class ArticleDetails {
    private static final Logger log = LoggerFactory.getLogger(ArticleDetails.class);

    private String articleapi;
    private String clientID;
    private boolean status;

    @Activate
    public void activate(ArticledetailsConfig config) {
        articleapi = config.getArticleApi();
        clientID = config.getClientid();
        status = config.getEnable();
        log.info("Activated -> Article api: {}, clientid: {}, enable: {}", articleapi, clientID, status);
    }

    @Deactivate
    public void deactivate(ArticledetailsConfig config) {
        log.info("Deactivated -> ArticleDetails service");
    }

    @Modified
    public void update(ArticledetailsConfig config) {
        articleapi = config.getArticleApi();
        clientID = config.getClientid();
        status = config.getEnable();
        log.info("Updated -> Article api: {}, clientid: {}, enable: {}", articleapi, clientID, status);
    }

    public String getDetails() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(articleapi);
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String jsonString = EntityUtils.toString(response.getEntity());
                log.info("Received JSON data: {}", jsonString);
                return jsonString;
            } else {
                log.error("Unexpected status code: {}", statusCode);
            }
        } catch (IOException e) {
            log.error("Exception occurred while making the request", e);
            throw new RuntimeException(e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error("Exception occurred while closing HttpClient", e);
            }
        }
        return null;
    }
}
