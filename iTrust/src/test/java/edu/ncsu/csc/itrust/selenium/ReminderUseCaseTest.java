package edu.ncsu.csc.itrust.selenium;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.meterware.httpunit.HttpUnitOptions;

import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

public class ReminderUseCaseTest extends iTrustSeleniumTest {

	/*
	 * The URL for iTrust, change as needed
	 */
	/** ADDRESS */
	public static final String ADDRESS = "http://localhost:8080/iTrust/";
	private WebDriver driver;

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

	public void testSendReminder() throws Exception {
		driver = login("9000000001", "pw");
		assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");
		assertEquals("iTrust - Admin Home", driver.getTitle());

		driver.findElement(By.linkText("Send Appointment Reminders")).click();

		assertEquals("iTrust - Send Appointment Reminders", driver.getTitle());
		driver.findElement(By.name("days")).sendKeys("7");

		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		//assertLogged(TransactionType.MESSAGE_SEND, 9000000009L, 2L, "");

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		String stamp = dateFormat.format(date);
		driver.findElement(By.linkText("Outbox")).click();
		assertTrue(driver.getPageSource().contains("Reminder: upcoming appointment in 7day(s)"));
		assertTrue(driver.getPageSource().contains(stamp));
		//assertLogged(TransactionType.OUTBOX_VIEW, 9000000000L, 0L, "");

		//Tests that the message was sent to patient
		driver = login("1", "pw");
		assertLogged(TransactionType.HOME_VIEW, 1L, 0L, "");
		driver.findElement(By.linkText("Message Inbox")).click();
		assertLogged(TransactionType.INBOX_VIEW, 1L, 0L, "");
		assertTrue(driver.getPageSource().contains("System Reminder"));
		assertTrue(driver.getPageSource().contains("Reminder: upcoming appointment in 7day(s)"));
		assertTrue(driver.getPageSource().contains(stamp));
	}


}