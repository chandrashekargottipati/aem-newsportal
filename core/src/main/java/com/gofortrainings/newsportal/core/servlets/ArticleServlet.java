package com.gofortrainings.newsportal.core.servlets;

import com.gofortrainings.newsportal.core.services.ArticleDetails;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class, property = {
    "sling.servlet.methods=GET",
    "sling.servlet.paths=/bin/newsportal/articles"
})
public class ArticleServlet extends SlingSafeMethodsServlet {

    private static final Logger log = LoggerFactory.getLogger(ArticleServlet.class);

    @Reference
    private ArticleDetails articleDetails;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        try {
            String jsonData = articleDetails.getDetails();
            response.setContentType("application/json");
            response.getWriter().write(jsonData);
        } catch (Exception e) {
            log.error("Error processing article data", e);
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error processing article data\"}");
        }
    }
}
