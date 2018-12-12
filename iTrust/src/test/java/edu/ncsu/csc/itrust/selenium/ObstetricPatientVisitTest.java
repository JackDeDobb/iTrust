package edu.ncsu.csc.itrust.selenium;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Keys;

import com.meterware.httpunit.HttpUnitOptions;

import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

public class ObstetricPatientVisitTest extends iTrustSeleniumTest{
	
	/*
	 * The URL for iTrust, change as needed
	 */
	/** ADDRESS */
	public static final String ADDRESS = "http://localhost:8080/iTrust/";
	private WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
		HttpUnitOptions.setScriptingEnabled(false);
		// turn off htmlunit warnings
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
	}
	
	public void testPatientVisitElgibility() throws Exception {
		driver = login("9000000012", "pw");
		assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");
		assertEquals("iTrust - HCP Home", driver.getTitle());

		driver.findElement(By.linkText("Document Obstetric Office Visit")).click();
		driver.findElement(By.name("UID_PATIENTID")).sendKeys("10");
		driver.findElement(By.xpath("//input[@value='10']")).submit();
		
		try {
			assertEquals("http://localhost:8080/iTrust/auth/hcp/viewObstetricOfficeVisits.jsp", driver.getCurrentUrl());
		} catch (Error e) {
			verificationErrors.append(e.toString());
			fail();
		}
		//System.out.println(driver.getPageSource());
		assertTrue(driver.getPageSource().contains("Patient is not eligible!"));
		
		driver.findElement(By.linkText("Obstetric Care")).click();
		assertTrue(driver.getPageSource().contains("THIS PATIENT IS NOT ELIGIBLE"));
		driver.findElement(By.name("eligibilityAction")).click();
		System.out.println(driver.getCurrentUrl());
		try {
			assertEquals("http://localhost:8080/iTrust/auth/getPatientID.jsp?forward=hcp-uap/obstetricCare.jsp", driver.getCurrentUrl());
		} catch (Error e) {
			verificationErrors.append(e.toString());
			fail();
		}
		
		
		driver.findElement(By.linkText("Document Obstetric Office Visit")).click();
		driver.findElement(By.name("UID_PATIENTID")).sendKeys("10");
		driver.findElement(By.xpath("//input[@value='10']")).submit();
		
		try {
			assertEquals("http://localhost:8080/iTrust/auth/hcp/viewObstetricOfficeVisits.jsp", driver.getCurrentUrl());
		} catch (Error e) {
			verificationErrors.append(e.toString());
			fail();
		}
		
		assertTrue(driver.getPageSource().contains("Document New Obstetric Office Visit"));
		
		
		/*
		driver.findElement(By.name("UID_PATIENTID")).sendKeys("10");
		driver.findElement(By.xpath("//input[@value='10']")).submit();
		assertTrue(driver.getPageSource().contains("Add Obstetric Record"));
		
		driver.findElement(By.xpath("//a[@href='addNewObstetricRecord.jsp']")).click();
		
		//driver.findElement(By.name("editRecordAction")).sendKeys("7302000");
		driver.findElement(By.name("editRecordAction")).click();
		assertTrue(driver.getPageSource().contains("Invalid LMP Date"));
		*/
	}

	public void testObstetricRecordExistenceForVisit() throws Exception {
		driver = login("9000000012", "pw");
		assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");
		assertEquals("iTrust - HCP Home", driver.getTitle());

		driver.findElement(By.linkText("Document Obstetric Office Visit")).click();

		assertEquals("iTrust - Please Select a Patient", driver.getTitle());
		driver.findElement(By.name("UID_PATIENTID")).sendKeys("21");
		driver.findElement(By.xpath("//input[@value='21']")).submit();
		
		try {
			assertEquals("http://localhost:8080/iTrust/auth/hcp/viewObstetricOfficeVisits.jsp", driver.getCurrentUrl());
		} catch (Error e) {
			verificationErrors.append(e.toString());
			fail();
		}
		
		if(driver.getPageSource().contains("Patient is not eligible!")) {
			
			driver.findElement(By.linkText("Obstetric Care")).click();
			assertTrue(driver.getPageSource().contains("THIS PATIENT IS NOT ELIGIBLE"));
			driver.findElement(By.name("eligibilityAction")).click();
			System.out.println(driver.getCurrentUrl());
			try {
				assertEquals("http://localhost:8080/iTrust/auth/getPatientID.jsp?forward=hcp-uap/obstetricCare.jsp", driver.getCurrentUrl());
			} catch (Error e) {
				verificationErrors.append(e.toString());
				fail();
			}
			driver.findElement(By.linkText("Document Obstetric Office Visit")).click();
			driver.findElement(By.name("UID_PATIENTID")).sendKeys("21");
			driver.findElement(By.xpath("//input[@value='21']")).submit();
		}
		
		
		assertTrue(driver.getPageSource().contains("Document New Obstetric Office Visit"));
		driver.findElement(By.xpath("//a[@href='addObstetricOfficeVisit.jsp']")).click();
		//driver.findElement(By.name("visitDate")).sendKeys("00201606121256PM");
		WebElement dateBox = driver.findElement(By.name("visitDate"));
		dateBox.sendKeys("1993-05-23T05:34");
		//dateBox.sendKeys(Keys.TAB);
		//dateBox.sendKeys("0245PM");
		driver.findElement(By.name("weight")).sendKeys("10");
		driver.findElement(By.name("systolicBP")).sendKeys("20");
		driver.findElement(By.name("diastolicBP")).sendKeys("20");
		driver.findElement(By.name("fetalHeartRate")).sendKeys("20");
		driver.findElement(By.name("lowLyingPlacentaObserved")).sendKeys("20");
		driver.findElement(By.name("numberOfBabies")).sendKeys("2");
		driver.findElement(By.xpath("//input[@value='Add Visit']")).click();
		System.out.println(driver.getPageSource());
		assertTrue(driver.getPageSource().contains("Patient does not have a valid obstetric record on file."));
		
		/*
		driver.findElement(By.xpath("//a[@href='addNewObstetricRecord.jsp']")).click();
		
		driver.findElement(By.name("editRecordAction")).click();
		assertTrue(driver.getPageSource().contains("Invalid LMP Date"));
		
		driver.findElement(By.name("lmp")).sendKeys("2005-07-29");
		driver.findElement(By.name("editRecordAction")).click();
		
		System.out.println(driver.getCurrentUrl());
		System.out.println(driver.getPageSource().contains("Invalid LMP Date"));
		try {
			assertEquals("http://localhost:8080/iTrust/auth/getPatientID.jsp?forward=hcp-uap/obstetricCare.jsp", driver.getCurrentUrl());
		} catch (Error e) {
			verificationErrors.append(e.toString());
			fail();
		}
		*/
	}
	
	public void testaddObstetricVisit() throws Exception {
		driver = login("9000000012", "pw");
		assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");
		assertEquals("iTrust - HCP Home", driver.getTitle());

		driver.findElement(By.linkText("Document Obstetric Office Visit")).click();

		assertEquals("iTrust - Please Select a Patient", driver.getTitle());
		driver.findElement(By.name("UID_PATIENTID")).sendKeys("10");
		driver.findElement(By.xpath("//input[@value='10']")).submit();
		
		try {
			assertEquals("http://localhost:8080/iTrust/auth/hcp/viewObstetricOfficeVisits.jsp", driver.getCurrentUrl());
		} catch (Error e) {
			verificationErrors.append(e.toString());
			fail();
		}
		
		if(driver.getPageSource().contains("Patient is not eligible!")) {
			
			driver.findElement(By.linkText("Obstetric Care")).click();
			assertTrue(driver.getPageSource().contains("THIS PATIENT IS NOT ELIGIBLE"));
			driver.findElement(By.name("eligibilityAction")).click();
			System.out.println(driver.getCurrentUrl());
			try {
				assertEquals("http://localhost:8080/iTrust/auth/getPatientID.jsp?forward=hcp-uap/obstetricCare.jsp", driver.getCurrentUrl());
			} catch (Error e) {
				verificationErrors.append(e.toString());
				fail();
			}
			driver.findElement(By.name("UID_PATIENTID")).sendKeys("10");
			driver.findElement(By.xpath("//input[@value='10']")).submit();	
		}
		
		assertTrue(driver.getPageSource().contains("Add Obstetric Record"));
		
		driver.findElement(By.xpath("//a[@href='addNewObstetricRecord.jsp']")).click();
		
		driver.findElement(By.name("lmp")).sendKeys("2005-07-29");
		driver.findElement(By.name("editRecordAction")).click();
		
		driver.findElement(By.linkText("Document Obstetric Office Visit")).click();
		
		assertEquals("iTrust - Please Select a Patient", driver.getTitle());
		driver.findElement(By.name("UID_PATIENTID")).sendKeys("10");
		driver.findElement(By.xpath("//input[@value='10']")).submit();
		
		assertTrue(driver.getPageSource().contains("Document New Obstetric Office Visit"));
		driver.findElement(By.xpath("//a[@href='addObstetricOfficeVisit.jsp']")).click();
		driver.findElement(By.name("visitDate")).sendKeys("1993-05-23T05:34");
		driver.findElement(By.name("weight")).sendKeys("10");
		driver.findElement(By.name("systolicBP")).sendKeys("20");
		driver.findElement(By.name("diastolicBP")).sendKeys("20");
		driver.findElement(By.name("fetalHeartRate")).sendKeys("20");
		driver.findElement(By.name("lowLyingPlacentaObserved")).sendKeys("70");
		driver.findElement(By.name("numberOfBabies")).sendKeys("2");
		driver.findElement(By.xpath("//input[@value='Add Visit']")).click();
		driver.findElement(By.xpath("//input[@value='Skip adding ultrasound record']")).click();
		System.out.println(driver.getPageSource());
		//2018-01-01 01:00:00.0
		
		/*
		driver.findElement(By.xpath("//a[@href='addNewObstetricRecord.jsp']")).click();
		
		driver.findElement(By.name("editRecordAction")).click();
		assertTrue(driver.getPageSource().contains("Invalid LMP Date"));
		
		driver.findElement(By.name("lmp")).sendKeys("2005-07-29");
		driver.findElement(By.name("editRecordAction")).click();
		
		System.out.println(driver.getCurrentUrl());
		System.out.println(driver.getPageSource().contains("Invalid LMP Date"));
		try {
			assertEquals("http://localhost:8080/iTrust/auth/getPatientID.jsp?forward=hcp-uap/obstetricCare.jsp", driver.getCurrentUrl());
		} catch (Error e) {
			verificationErrors.append(e.toString());
			fail();
		}
		*/
	}

	
	
	
	

}
