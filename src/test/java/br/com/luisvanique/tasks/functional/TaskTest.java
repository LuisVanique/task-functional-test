package br.com.luisvanique.tasks.functional;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TaskTest {
	
	private WebDriver driver;

	public WebDriver acessarAplicacao() throws MalformedURLException {
	    DesiredCapabilities caps = new DesiredCapabilities();
	    caps.setBrowserName("chrome");
	    caps.setCapability("platformName", "LINUX");

	    driver = new RemoteWebDriver(new URL("http://192.168.192.1:4444/wd/hub"), caps);
	    
	    // Testando a comunicação inicial
	    System.out.println("Conectado ao Hub!");
	    
	    driver.navigate().to("http://192.168.192.1:8001/tasks");

	    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	    return driver;
	}

	
	@After
	public void teardown() {
	    driver.quit(); 
	}
	
	@Test
	public void deveSalvarTarefaComSucesso() throws MalformedURLException{
		LocalDate dataAtual = LocalDate.now();
		String dia = String.format("%02d", dataAtual.getDayOfMonth());
	    String mes = String.format("%02d", dataAtual.getMonthValue()); 
	    String ano = String.valueOf(dataAtual.getYear());

		WebDriver driver = acessarAplicacao();
		try {
			// clicar em add todo
			driver.findElement(By.id("addTodo")).click();

			// escrever a descricao
			driver.findElement(By.id("task")).sendKeys("Teste Selenium");

			// escrever a data
			driver.findElement(By.id("dueDate")).sendKeys(dia + "/" + mes + "/" + ano);

			// clicar em save
			driver.findElement(By.id("saveButton")).click();

			// Validar mensagem de sucesso
			String mensagem = driver.findElement(By.xpath("//*[@id=\"message\"]")).getText();
			Assert.assertEquals("Success!", mensagem);

		} finally {
			// fechar browser
			driver.quit();

		}

	}

	@Test
	public void naoDeveSalvarTarefaPassada() throws MalformedURLException{
		LocalDate dataPassada = LocalDate.now().minusYears(2 /* any value > 0 */);
		String dia = String.format("%02d", dataPassada.getDayOfMonth());
	    String mes = String.format("%02d", dataPassada.getMonthValue()); 
	    String ano = String.valueOf(dataPassada.getYear());

		WebDriver driver = acessarAplicacao();

		try {
			// clicar em add todo
			driver.findElement(By.id("addTodo")).click();

			// escrever a descricao
			driver.findElement(By.id("task")).sendKeys("Test Selenium past due");

			// escrever a data
			driver.findElement(By.id("dueDate")).sendKeys(dia + "/" + mes + "/" + ano);

			// clicar em save
			driver.findElement(By.id("saveButton")).click();

			// Validar mensagem de erro
			String mensagem = driver.findElement(By.xpath("//*[@id=\"message\"]")).getText();
			Assert.assertEquals("Due date must not be in past", mensagem);
		} finally {
			// fechar browser
			driver.quit();
		}

	}

	@Test
	public void naoDeveSalvarSemDescricao() throws MalformedURLException, InterruptedException{
		WebDriver driver = acessarAplicacao();
		LocalDate dataAtual = LocalDate.now();
		String dia = String.format("%02d", dataAtual.getDayOfMonth());
	    String mes = String.format("%02d", dataAtual.getMonthValue()); 
	    String ano = String.valueOf(dataAtual.getYear());

		

		try {
			// clicar em add todo
			driver.findElement(By.id("addTodo")).click();

			// escrever a data
			driver.findElement(By.id("dueDate")).sendKeys(dia + "/" + mes + "/" + ano);

			// clicar em save
			Actions actions = new Actions(driver);
			actions.moveToElement(driver.findElement(By.id("saveButton"))).click().perform();

			// Validar mensagem de erro
			String mensagem = driver.findElement(By.xpath("//*[@id=\"message\"]")).getText();
			Assert.assertEquals("Fill the task description", mensagem);
		} finally {
			// fechar browser
			driver.quit();
		}

	}

	@Test
	public void naoDeveAdicionarSemData() throws MalformedURLException{
		WebDriver driver = acessarAplicacao();
		try {
			// clicar em add todo
			driver.findElement(By.id("addTodo")).click();

			// escrever a descricao
			driver.findElement(By.id("task")).sendKeys("Teste Selenium");

			// clicar em save
			driver.findElement(By.id("saveButton")).click();

			// Validar mensagem de sucesso
			String mensagem = driver.findElement(By.xpath("//*[@id=\"message\"]")).getText();
			Assert.assertEquals("Fill the due date", mensagem);

		} finally {
			// fechar browser
			driver.quit();
		}

	}
}
