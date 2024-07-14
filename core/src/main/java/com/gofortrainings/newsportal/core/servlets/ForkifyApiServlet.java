package com.gofortrainings.newsportal.core.servlets;

import com.gofortrainings.newsportal.core.services.ForkifyApiService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.servlet.Servlet;
import java.io.IOException;

@Component(service = Servlet.class,
        property = {
                "service.description=Forkify Recipe API Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths=" + "/bin/forkify/api"
        })
public class ForkifyApiServlet extends SlingSafeMethodsServlet {

    @Reference
    private ForkifyApiService apiService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        String recipeId = request.getParameter("id");

        if (recipeId == null || recipeId.isEmpty()) {
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing 'id' parameter");
            return;
        }

        if (!apiService.isApiEnabled()) {
            response.setStatus(SlingHttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.getWriter().write("Forkify API is currently disabled");
            return;
        }

        String fullApiUrl = apiService.getFullApiUrl(recipeId);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(fullApiUrl);
            String result = httpClient.execute(httpGet, httpResponse ->
                    EntityUtils.toString(httpResponse.getEntity()));

            response.setContentType("application/json");
            response.getWriter().write(result);
        } catch (Exception e) {
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error fetching recipe data: " + e.getMessage());
        }
    }
}
