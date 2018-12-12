package edu.ncsu.csc.itrust.selenium;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.meterware.httpunit.HttpUnitOptions;

import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

public class ChildBirthVisitRecordTest extends iTrustSeleniumTest{
	
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
	
	public void testPatientElgibility() throws Exception {
		driver = login("9000000012", "pw");
		assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");
		assertEquals("iTrust - HCP Home", driver.getTitle());

		driver.findElement(By.linkText("Obstetric Care")).click();

		assertEquals("iTrust - Please Select a Patient", driver.getTitle());
		driver.findElement(By.name("UID_PATIENTID")).sendKeys("10");
		driver.findElement(By.xpath("//input[@value='10']")).submit();
		
		try {
			assertEquals("http://localhost:8080/iTrust/auth/hcp-uap/obstetricCare.jsp", driver.getCurrentUrl());
		} catch (Error e) {
			verificationErrors.append(e.toString());
			fail();
		}
		
		
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
		assertTrue(driver.getPageSource().contains("Add Obstetric Record"));
		
		driver.findElement(By.xpath("//a[@href='addNewObstetricRecord.jsp']")).click();
		
		//driver.findElement(By.name("editRecordAction")).sendKeys("7302000");
		driver.findElement(By.name("editRecordAction")).click();
		assertTrue(driver.getPageSource().contains("Invalid LMP Date"));
	}

	public void testAddChildBirthVisitRecord() throws Exception {
		driver = login("9000000012", "pw");
		assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");
		assertEquals("iTrust - HCP Home", driver.getTitle());

		driver.findElement(By.linkText("Obstetric Care")).click();

		assertEquals("iTrust - Please Select a Patient", driver.getTitle());
		driver.findElement(By.name("UID_PATIENTID")).sendKeys("10");
		driver.findElement(By.xpath("//input[@value='10']")).submit();
		
		try {
			assertEquals("http://localhost:8080/iTrust/auth/hcp-uap/obstetricCare.jsp", driver.getCurrentUrl());
		} catch (Error e) {
			verificationErrors.append(e.toString());
			fail();
		}
		
		if(driver.getPageSource().contains("THIS PATIENT IS NOT ELIGIBLE")) {
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
		
		assertTrue(driver.getPageSource().contains("Add New Child Birth Visit Record"));
		Boolean noNewRecords = false;
		if(driver.getPageSource().contains("You have no obstetric records for this patient!")) {
			noNewRecords = true;
		}
		//assertTrue(driver.getPageSource().contains("You have no obstetric records for this patient!"));
		
		driver.findElement(By.xpath("//a[@href='addNewChildBirthVisit.jsp']")).click();
		
		driver.findElement(By.name("editRecordAction")).click();
		
		assertEquals("iTrust - Please Select a Patient", driver.getTitle());
		driver.findElement(By.name("UID_PATIENTID")).sendKeys("10");
		driver.findElement(By.xpath("//input[@value='10']")).submit();
		//assertTrue(driver.getPageSource().contains("Invalid LMP Date"));
		
		if(noNewRecords) {
			assertTrue(!driver.getPageSource().contains("You have no obstetric records for this patient!"));
		}
		

	}


}
