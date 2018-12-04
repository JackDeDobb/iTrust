package edu.ncsu.csc.itrust.server;

import edu.ncsu.csc.itrust.action.AddObstetricOfficeVisitAction;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.validate.UltrasoundRecordValidator;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@WebServlet("/upload")
@MultipartConfig
public class UltrasoundServlet extends HttpServlet {

    // Add obs office visit action.
    private AddObstetricOfficeVisitAction addObstetricOfficeVisitAction;

    // File upload constants.
    private int maxFileSize = 100000 * 1024;
    private int maxMemSize = 100000 * 1024;
    private String filePath;
    private UltrasoundRecordValidator validator = new UltrasoundRecordValidator();

    @Override
    public void init() throws ServletException {
        super.init();
        filePath = "/Users/arvind/Documents/College_2015/current_classes/cs427/itrust/T808/temp";
        validator = new UltrasoundRecordValidator();
    }

    public UltrasoundServlet() {
        super();
        addObstetricOfficeVisitAction = new AddObstetricOfficeVisitAction(DAOFactory.getProductionInstance(), -1);
    }

    protected UltrasoundServlet(DAOFactory factory) {
        super();
        addObstetricOfficeVisitAction = new AddObstetricOfficeVisitAction(factory, -1);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    /**
     * Create an ultrasound record bean populated with the data from the request.
     */
    private UltrasoundRecordBean createUltrasoundRecord(HttpServletRequest req) {
        // Create ultrasound record bean from request params.
		UltrasoundRecordBean ultrasoundRecord = new UltrasoundRecordBean();
		Map<Object, Object> params = req.getParameterMap();
		for (Map.Entry<Object, Object> entry : params.entrySet()) {
            System.out.println("Key: " + entry.getKey().toString());
            System.out.println("Value: " + entry.getValue().toString());
        }

        ultrasoundRecord.setAbdominalCircumference(Float.valueOf(req.getParameter("abdominalCircumference")));
        ultrasoundRecord.setBiparietalDiameter(Float.valueOf(req.getParameter("biparietalDiameter")));
        ultrasoundRecord.setCrownRumpLength(Float.valueOf(req.getParameter("crownRumpLength")));
        ultrasoundRecord.setEstimatedFetalWeight(Float.valueOf(req.getParameter("estimatedFetalWeight")));
        ultrasoundRecord.setFemurLength(Float.valueOf(req.getParameter("femurLength")));
        ultrasoundRecord.setHeadCircumference(Float.valueOf(req.getParameter("headCircumference")));
        ultrasoundRecord.setHumerusLength(Float.valueOf(req.getParameter("humerusLength")));
        ultrasoundRecord.setOccipitofrontalDiameter(Float.valueOf(req.getParameter("occipitofrontalDiameter")));

        return ultrasoundRecord;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        UltrasoundRecordBean ultrasoundRecord = createUltrasoundRecord(req);

//        try {
//            validator.validate(ultrasoundRecord);
//        } catch (FormValidationException ex) {
//            // TODO: Send response signalling validation error.
//        }

        boolean isMultipart = ServletFileUpload.isMultipartContent(req);
        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();

        if (!isMultipart) {
            return;
        }

        // Disk items.
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Set max mem size threshold.
        factory.setSizeThreshold(maxMemSize);

        // Location to save if mem size is not sufficient.

        File tempFile = File.createTempFile(filePath, File.pathSeparator + "temp");
        tempFile.mkdirs();
        factory.setRepository(tempFile);

        // Create a new file upload handler.
        ServletFileUpload upload = new ServletFileUpload(factory);

        // Maximum file size to be uploaded.
        upload.setSizeMax( maxFileSize );

//        // Visit id to be used with ultrasound id.
//        long visitId = Long.parseLong(req.getParameter("visitId"));
//
//        // Patient id to be used with ultrasound.
//        long patientMID = Long.parseLong(req.getParameter("patientMID"));

        try {
            // Parse the request to get file items.
            List<FileItem> fileItems = upload.parseRequest(req);

            // Process the uploaded file items
            Iterator<FileItem> i = fileItems.iterator();

            File file = null;
            while ( i.hasNext () ) {
                FileItem fi = i.next();
                if ( !fi.isFormField () ) {
                    // Get the uploaded file parameters
                    String fieldName = fi.getFieldName();
                    String fileName = fi.getName();
                    String contentType = fi.getContentType();
                    boolean isInMemory = fi.isInMemory();
                    long sizeInBytes = fi.getSize();

                    // Write the file
                    String uniqueFilename = FilepathGen.generateUniqueFileName(fileName);
                    file = Paths.get(filePath,uniqueFilename).toFile();
                    System.out.println("Absolute Path: " + file.getAbsolutePath());
                    file.getParentFile().mkdirs();
                    fi.write(file);
                    out.println(file.getAbsolutePath());
                }
            }

//            ultrasoundRecord.setImagePath(file.getAbsolutePath());
//            addObstetricOfficeVisitAction.addUltrasoundRecord(visitId, patientMID, ultrasoundRecord);
        } catch(Exception ex) {
            System.out.println(ex);
        }
    }
}
