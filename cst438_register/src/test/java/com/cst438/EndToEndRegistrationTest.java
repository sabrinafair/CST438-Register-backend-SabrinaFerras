package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

@SpringBootTest
public class EndToEndRegistrationTest {

	public static final String CHROME_DRIVER_FILE_LOCATION = "C:/chromedriver_win32/chromedriver.exe";

	public static final String URL = "http://localhost:3000";

	public static final String TEST_USER_EMAIL = "tester@csumb.edu";
	
	public static final String TEST_USER_NAME = "tester";
	public static final String TEST_USER_STATUS = "No Holds";
	public static final int TEST_USER_STATUS_CODE = 0;

	public static final int SLEEP_DURATION = 1000; // 1 second.

	/*
	 * When running in @SpringBootTest environment, database repositories can be used
	 * with the actual database.
	 */
	
	
	@Autowired
	StudentRepository studentRepository;

	
	@Test
	public void adminAddStudentTest() throws Exception {

		/*
		 * if student is already added, then delete student.
		 */
		
		Student x = null;
		do {
			x = studentRepository.findByEmail(TEST_USER_EMAIL);
			if (x != null)
				studentRepository.delete(x);
		} while (x != null);


		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		
		ChromeOptions ops = new ChromeOptions();
		ops.addArguments("--remote-allow-origins=*");	
		
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		try {

			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);

			WebElement we = driver.findElement(By.xpath("//button[@id='addNewStudentButton']"));
			we.click();

			Thread.sleep(SLEEP_DURATION);

			
			driver.findElement(By.xpath("//input[@name='name']")).sendKeys(TEST_USER_NAME);
			driver.findElement(By.xpath("//input[@name='email']")).sendKeys(TEST_USER_EMAIL);
			driver.findElement(By.xpath("//button[@id='AddS']")).click(); 
			Thread.sleep(SLEEP_DURATION);


			Student s = studentRepository.findByEmail(TEST_USER_EMAIL);
			assertNotNull(s, "Student not found in database.");
			assertNotNull(s.getStudent_id(), "Student ID not given");
			assertEquals(s.getName(), TEST_USER_NAME);
			assertEquals(s.getEmail(), TEST_USER_EMAIL);
			assertEquals(s.getStatus(), TEST_USER_STATUS);
			assertEquals(s.getStatusCode(), TEST_USER_STATUS_CODE);
			

		} catch (Exception ex) {
			throw ex;
		} finally {

			// clean up database.
			
			Student s = studentRepository.findByEmail(TEST_USER_EMAIL);
			if (s != null)
				studentRepository.delete(s);

			driver.close();
			driver.quit();
		}

	}
}
