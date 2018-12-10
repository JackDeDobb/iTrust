package edu.ncsu.csc.itrust.server;

import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class DisplayImageServlet extends HttpServlet {

    private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.

    private String baseFilePath = ImageStore.baseFilePath;

    // Reference/Source: http://balusc.omnifaces.org/2007/07/fileservlet.html

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("|| Display Image Servlet ||");

        // Get image path from request.
        // Note: `getPathInfo` returns only the filename.
        String imagePath = req.getPathInfo();

        System.out.println("Image Path: " + imagePath);

        // Check if a file is provided.
        if (imagePath == null) {
            // If so, respond with 404 error.
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        File file = new File(baseFilePath, URLDecoder.decode(imagePath, StandardCharsets.UTF_8.name()));

        // Check if file exists.
        if (!file.exists()) {
            // If so, respond with 404 error.
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        // Get content type by filename.
        String contentType = getServletContext().getMimeType(file.getName());

        // If content type is unknown, then set the default value.
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        // Reset response fields.
        resp.reset();

        // Set buffer size.
        resp.setBufferSize(DEFAULT_BUFFER_SIZE);

        // Set content type for response.
        resp.setContentType(contentType);

        // Set content length.
        resp.setHeader("Content-Length", String.valueOf(file.length()));

        // Set content disposition.
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

        // Copy file to response stream.
        FileUtils.copyFile(file, resp.getOutputStream());
    }

}
