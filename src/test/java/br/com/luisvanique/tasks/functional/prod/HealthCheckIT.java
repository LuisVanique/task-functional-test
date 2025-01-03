package br.com.luisvanique.tasks.functional.prod;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class HealthCheckIT {

	@Test
	public void healthCheck() throws MalformedURLException {
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setBrowserName("chrome");
		caps.setCapability("platformName", "LINUX");

		WebDriver driver = new RemoteWebDriver(new URL("http://192.168.192.1:4444/wd/hub"), caps);
		try {
			driver.navigate().to("http://192.168.192.1:9999/tasks");

			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

			String version = driver.findElement(By.id("version")).getText();

			Assert.assertTrue(version.startsWith("build"));
		} finally {
			driver.quit();
		}

	}
}
