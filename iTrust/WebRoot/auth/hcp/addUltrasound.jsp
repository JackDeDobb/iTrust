<%@page import="edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.UltrasoundRecordDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ObstetricOfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditObstetricOfficeVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricOfficeVisitDAO"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.action.AddObstetricOfficeVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.action.AddObstetricOfficeVisitAction"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@include file="/global.jsp"%>

<%
    pageTitle = "iTrust - Add Ultrasound";
%>

<%@include file="/header.jsp"%>

<%
    String visitIdString = (String) session.getAttribute("visitId");
    String patientMIDString = (String) session.getAttribute("pid");
    if (visitIdString == null) {
        out.println("visitIdString is null");
        response.sendRedirect("iTrust/auth/hcp/addObstetricOfficeVisit.jsp?forward=hcp/addUltrasound.jsp");
    }

    String RHImmunizedStr = request.getParameter("needsToBeRHImmunized");
    if (RHImmunizedStr != null && Boolean.parseBoolean(RHImmunizedStr)) {
%>
<div align=center>
    <span class="iTrustMessage">Patient needs to be RH- immunized. </span>
</div>
<%
    }

    Long patientMID = Long.parseLong(patientMIDString);
    Long visitId = Long.parseLong(visitIdString);

    ViewPersonnelAction hcpAction = new ViewPersonnelAction(prodDAO, loggedInMID.longValue());
    PersonnelBean hcp = hcpAction.getPersonnel(String.valueOf(loggedInMID.longValue()));
    boolean isOBGYN = hcp.getSpecialty().equals("OB/GYN");

    EditPatientAction paction = new EditPatientAction(prodDAO,loggedInMID.longValue(), patientMIDString);
    PatientBean pb = paction.getPatient();
    boolean isEligible = pb.getObstetricEligibility();
//
//    AddObstetricOfficeVisitAction addObstetricOfficeVisitAction = new AddObstetricOfficeVisitAction(prodDAO,
//            loggedInMID.longValue());
//
//    boolean formIsFilled = request.getParameter("formIsFilled") != null && request.getParameter("formIsFilled").equals("true");
//
//    if (formIsFilled) {
//        UltrasoundRecordBean ultrasoundRecord = new UltrasoundRecordBean();
//        ultrasoundRecord.setAbdominalCircumference(Float.valueOf(request.getParameter("abdominalCircumference")));
//        ultrasoundRecord.setBiparietalDiameter(Float.valueOf(request.getParameter("biparietalDiameter")));
//        ultrasoundRecord.setCrownRumpLength(Float.valueOf(request.getParameter("crownRumpLength")));
//        ultrasoundRecord.setEstimatedFetalWeight(Float.valueOf(request.getParameter("estimatedFetalWeight")));
//        ultrasoundRecord.setFemurLength(Float.valueOf(request.getParameter("femurLength")));
//        ultrasoundRecord.setHeadCircumference(Float.valueOf(request.getParameter("headCircumference")));
//        ultrasoundRecord.setHumerusLength(Float.valueOf(request.getParameter("humerusLength")));
//        ultrasoundRecord.setOccipitofrontalDiameter(Float.valueOf(request.getParameter("occipitofrontalDiameter")));
//        ultrasoundRecord.setImagePath(request.getParameter("filepath"));
//        ultrasoundRecord.setVisitID(visitId.longValue());
//
//    }

    if (isOBGYN) {
%>
<form action="viewObstetricOfficeVisits.jsp">
    <input type="submit" value="Skip adding ultrasound record" />
</form>
<br/>
<form id="upload-file" action="UltrasoundServlet" method = "post" enctype = "multipart/form-data">
    <input id = "filepath" type="hidden" name = "filepath"/>
    <input type="hidden" name="formIsFilled" value="true"><br/>
    <input type="hidden" name="loggedInMID" value="<%=loggedInMID.longValue()%>"><br/>
    <input type="hidden" name="patientMID" value="<%=patientMIDString%>"><br/>
    <input type="hidden" name="visitId" value="<%=visitIdString%>"><br/>
    <table class="fTable">
        <tr class="ultra" >
            <td class="subHeaderVertical">Crown Rump Length:</td>
            <td><input type="number" name="crownRumpLength"></td>
        </tr>
        <tr class="ultra" >
            <td class="subHeaderVertical">Head circumference:</td>
            <td><input type="number" name="headCircumference"></td>
        </tr>
        <tr class="ultra" >
            <td class="subHeaderVertical">Biparietal Diameter:</td>
            <td><input type="number" name="biparietalDiameter"></td>
        </tr>
        <tr class="ultra" >
            <td class="subHeaderVertical">Femur Length:</td>
            <td><input type="number" name="femurLength"></td>
        </tr>
        <tr class="ultra" >
            <td class="subHeaderVertical">Occipitofrontal Diameter:</td>
            <td><input type="number" name="occipitofrontalDiameter"></td>
        </tr>
        <tr class="ultra" >
            <td class="subHeaderVertical">Abdominal Circumference:</td>
            <td><input type="number" name="abdominalCircumference"></td>
        </tr>
        <tr class="ultra" >
            <td class="subHeaderVertical">Humerus Length:</td>
            <td><input type="number" name="humerusLength"></td>
        </tr>
        <tr class="ultra" >
            <td class="subHeaderVertical">Estimated Fetal Weight:</td>
            <td><input type="number" name="estimatedFetalWeight"></td>
        </tr>
        <tr class="ultra" >
            <td class="subHeaderVertical">Choose Ultrasound</td>
            <td><input type = "file" name = "file" /></td>
        </tr>
    </table>
    <br/>
    <input type = "submit" value = "Add ultrasound record" />
</form>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.js"></script>
<script type="text/javascript">
    $(function() {
        $('#upload-file').ajaxForm({
            success: function(msg) {
                alert(msg);
                if (!msg.includes("exception")) {
                    window.location.replace("./viewObstetricOfficeVisits.jsp");
                }
            },
            error: function(msg) {
                alert("Couldn't upload file");
                $("#upload-error").text("Couldn't upload file");
            }
        });
    });
</script>
<%
    } else if (!isOBGYN) {
%>
<div align=center>
    <p style="width: 50%; text-align:left;">Please use the <a href="/iTrust/auth/hcp-uap/viewOfficeVisit.xhtml">Document Office Visit</a> page.</p>
</div>
<%
    } else if (!isEligible) {
%>
<div align=center>
    <h1>Patient is not eligible!</h1>
</div>
<%
    }
%>
<%@include file="/footer.jsp" %>
