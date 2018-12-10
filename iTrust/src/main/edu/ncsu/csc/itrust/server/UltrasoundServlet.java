package edu.ncsu.csc.itrust.server;

import edu.ncsu.csc.itrust.action.AddObstetricOfficeVisitAction;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.validate.UltrasoundRecordValidator;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@MultipartConfig
public class UltrasoundServlet extends HttpServlet {

    // Add obs office visit action.
    private AddObstetricOfficeVisitAction addObstetricOfficeVisitAction;

    // File upload constants.
    private int maxFileSize = 100000 * 1024;
    private int maxMemSize = 100000 * 1024;
    private String baseFilePath;
    private UltrasoundRecordValidator validator = new UltrasoundRecordValidator();

    @Override
    public void init() throws ServletException {
        super.init();
        // TODO: Replace hardcoded path with system-independent HOME/temp based path.
        baseFilePath = ImageStore.baseFilePath;
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
     * Note: Image path is not populated from text form fields, but created after parsing file part of request.
     */
    private UltrasoundRecordBean createUltrasoundRecord(HttpServletRequest request) {
        // Create ultrasound record bean from request params.
        UltrasoundRecordBean ultrasoundRecord = new UltrasoundRecordBean();

        ultrasoundRecord.setAbdominalCircumference(Float.valueOf(request.getParameter("abdominalCircumference")));
        ultrasoundRecord.setBiparietalDiameter(Float.valueOf(request.getParameter("biparietalDiameter")));
        ultrasoundRecord.setCrownRumpLength(Float.valueOf(request.getParameter("crownRumpLength")));
        ultrasoundRecord.setEstimatedFetalWeight(Float.valueOf(request.getParameter("estimatedFetalWeight")));
        ultrasoundRecord.setFemurLength(Float.valueOf(request.getParameter("femurLength")));
        ultrasoundRecord.setHeadCircumference(Float.valueOf(request.getParameter("headCircumference")));
        ultrasoundRecord.setHumerusLength(Float.valueOf(request.getParameter("humerusLength")));
        ultrasoundRecord.setOccipitofrontalDiameter(Float.valueOf(request.getParameter("occipitofrontalDiameter")));
        ultrasoundRecord.setVisitID(Long.parseLong(request.getParameter("visitId")));

        return ultrasoundRecord;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Ultrasound record entry filled with form details.
        UltrasoundRecordBean ultrasoundRecord = createUltrasoundRecord(request);

        // Get logged in mid, and update the action.
        Long loggedInMID = Long.parseLong(request.getParameter("loggedInMID"));
        addObstetricOfficeVisitAction.setLoggedInMID(loggedInMID);

        // Get patient mid.
        Long patientMID = Long.parseLong(request.getParameter("patientMID"));

        // Response writer.
        PrintWriter out = response.getWriter();

        try {
            validator.validate(ultrasoundRecord);
        } catch (FormValidationException ex) {
            out.println("Form validation exception: " + ex.getMessage());
            return;
        }

        // Check if form is multipart (ie. contains a file).
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if (!isMultipart) {
            out.println("Incorrect request type: Request type must be multipart.");
            return;
        }

        // Parse file part.
        Part filePart = request.getPart("file");

        // Check if file is provided.
        if (filePart == null) {
            // If no file is provided.
            // Set image path to empty, to indicate no files there.
            ultrasoundRecord.setImagePath("");

        } else {
            // If file is provided.
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

            // Create relative image path.
            String relImagePath = Paths.get(ultrasoundRecord.getVisitID() + "", fileName).toString();

            // Create absolute path.
            Path absolutePath = Paths.get(baseFilePath, relImagePath);
            System.out.println(absolutePath.toString());

            // Create visitId dir.
            File visitIdDir = absolutePath.toFile().getParentFile();
            visitIdDir.mkdirs();

            // Clear visitId directory.
            FileUtils.cleanDirectory(visitIdDir);

            // Write file.
            try (InputStream fileContent = filePart.getInputStream()) {
                Files.copy(fileContent, absolutePath);
            }

            // Set ultrasound record field.
            ultrasoundRecord.setImagePath(relImagePath);
        }


        try {
            addObstetricOfficeVisitAction.addUltrasoundRecord(patientMID, ultrasoundRecord);
            out.println("Successfully added ultrasound record.");
        } catch (FormValidationException | DBException ex) {
            out.println("Encountered exception on adding record: " + ex.getMessage());
            ex.printStackTrace();
        }

    }
}
