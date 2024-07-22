package com.gofortrainings.newsportal.core.servlets;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component(service = {Servlet.class}, property = {
        "sling.servlet.methods=" + HttpConstants.METHOD_POST,
        "sling.servlet.paths=" + "/bin/company-data/excel"
})
public class CompanyDataServlet extends SlingAllMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(CompanyDataServlet.class);

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        String companyId = request.getParameter("companyId");

        if (companyId == null || companyId.isEmpty()) {
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid or missing companyId parameter");
            return;
        }

        try {
            Map<String, String> companyData = getCompanyData(companyId);
            if (companyData == null || companyData.isEmpty()) {
                response.setStatus(SlingHttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("Company data not found for companyId: " + companyId);
                return;
            }

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Company Data");
            Row header = sheet.createRow(0);

            int cellIndex = 0;
            for (Map.Entry<String, String> entry : companyData.entrySet()) {
                Cell cell = header.createCell(cellIndex++);
                cell.setCellValue(entry.getKey());
            }

            Row row = sheet.createRow(1);
            cellIndex = 0;
            for (Map.Entry<String, String> entry : companyData.entrySet()) {
                Cell cell = row.createCell(cellIndex++);
                cell.setCellValue(entry.getValue());
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=company-data.xlsx");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            response.getOutputStream().write(bos.toByteArray());
            bos.close();

        } catch (Exception e) {
            LOG.error("Error generating Excel file for companyId: {}", companyId, e);
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error generating Excel file");
        }
    }

    private Map<String, String> getCompanyData(String companyId) {
        // Mock data; replace with actual data fetching logic
        Map<String, String> data = new HashMap<>();
        data.put("Company ID", companyId);
        data.put("Company Name", "Company " + companyId);
        data.put("Address", "123 Street, City");
        data.put("Phone", "123-456-7890");
        return data;
    }
}
