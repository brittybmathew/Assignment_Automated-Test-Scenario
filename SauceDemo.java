package pkg;

import java.io.FileInputStream;

import java.io.IOException;
import java.time.Duration;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SauceDemo {
	
	WebDriver driver;
	
	@BeforeTest
	public void setUp() {
		System.setProperty("webdriver.chrome.driver","/Users/Britty/Downloads/chromedriver_win32//chromedriver.exe");
		ChromeOptions options=new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");

		//Navigate to URL
		driver= new ChromeDriver(options);
		driver.get("https://www.saucedemo.com/");
		driver.manage().window().maximize();
		
	}
	
	
	@Test(priority = 1)
	public void login() throws IOException {

		//To read login credentials from an Excel sheet
		FileInputStream ob=new FileInputStream("/Users/Britty/OneDrive/Desktop/SauceDemo.xlsx");
		XSSFWorkbook wb=new XSSFWorkbook(ob);
		XSSFSheet sh=wb.getSheet("Sheet1");
		for(int i=1; i<2; i++) {
			String username=sh.getRow(i).getCell(0).getStringCellValue();
			String password=sh.getRow(i).getCell(1).getStringCellValue();
			System.out.println("username= "+username);
			System.out.println("password= "+password);
			driver.findElement(By.xpath("//*[@id=\"user-name\"]")).sendKeys(username);
			driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(password);
			driver.findElement(By.xpath("//*[@id=\"login-button\"]")).click();
			
		}

		//To validate the URL
		String currentUrl=driver.getCurrentUrl();
		System.out.println(currentUrl);

		//To validate the logo
		boolean logo=driver.findElement(By.xpath("/html/body/div/div/div/div[1]/div[1]/div[2]/div")).isDisplayed();
		if(logo) {
			System.out.println("Logo is displayed");
		}
		else {
			System.out.println("Logo is not displayed");
		}

		//To validate the title
		String title=driver.getTitle();
		Assert.assertEquals(title,"Swag Labs");
		System.out.println("Title is " + title);
		
		//To validate the button
		boolean button=driver.findElement(By.xpath("//*[@id=\"add-to-cart-sauce-labs-backpack\"]")).isEnabled();
		if(button) {
			System.out.println("Add to cart button is enabled");
		}
		else {
			System.out.println("Add to cart button is disabled");
		}

		//To validate the button text
		String buttonText=driver.findElement(By.xpath("//*[@id=\"add-to-cart-sauce-labs-backpack\"]")).getText();
		Assert.assertEquals(buttonText, "Add to cart");
		System.out.println("Button text is " + buttonText);
		
	}
	
	
	@Test(priority = 2)
	public void sortProducts(){

		//To sort the products in price low to high order
		WebElement sort= driver.findElement(By.xpath("//*[@id=\"header_container\"]/div[2]/div/span/select"));
		Select option = new Select(sort);
	    	option.selectByValue("lohi");
	    
	    	boolean selectedOption=driver.findElement(By.xpath("//*[@id=\"header_container\"]/div[2]/div/span/select/option[3]")).isSelected();
	    	if(selectedOption) {
	    		System.out.println("Sorted products by price from low to high");
	    	}
	    	else {
	    		System.out.println("Error in sorting");
	   	}
	    
		//To add all items to the cart
		WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(30));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[class='btn btn_primary btn_small btn_inventory']")));
		for (WebElement i : driver.findElements(By.cssSelector("[class='btn btn_primary btn_small btn_inventory']")))
				i.click();
				
	}
	

	@Test(priority = 3)
		public void cart() {

		//To click the Cart icon
		JavascriptExecutor js=(JavascriptExecutor)driver;
		
		WebElement cartIcon=driver.findElement(By.xpath("//*[@id=\"shopping_cart_container\"]/a"));
		js.executeScript("arguments[0].scrollIntoView();", cartIcon);
		cartIcon.click();

		//To remove items that have a price <$15
		driver.findElement(By.xpath("//*[@id=\"remove-sauce-labs-onesie\"]")).click();
		driver.findElement(By.xpath("//*[@id=\"remove-sauce-labs-bike-light\"]")).click();

		//To click on the Checkout button
		WebElement checkoutButton=driver.findElement(By.xpath("//*[@id=\"checkout\"]"));
		js.executeScript("arguments[0].scrollIntoView();",checkoutButton );
		checkoutButton.click();

		//To enter the details on the information page
		driver.findElement(By.xpath("//*[@id=\"first-name\"]")).sendKeys("Britty");
		driver.findElement(By.xpath("//*[@id=\"last-name\"]")).sendKeys("B Mathew");
		driver.findElement(By.xpath("//*[@id=\"postal-code\"]")).sendKeys("688506");

		//To click on the Continue button
		driver.findElement(By.xpath("//*[@id=\"continue\"]")).click();

		//To click on the Finish button
		WebElement finishButton=driver.findElement(By.xpath("//*[@id=\"finish\"]"));
		js.executeScript("arguments[0].scrollIntoView();", finishButton);
		finishButton.click();

		//To return to the Home page
		driver.findElement(By.xpath("//*[@id=\"back-to-products\"]")).click();
		
		}
	
	@Test(priority = 4)
		public void logout() {

		//To perform Logout
		driver.findElement(By.xpath("//*[@id=\"react-burger-menu-btn\"]")).click();
		
		WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(30));
		WebElement logoutLink=wait.until(ExpectedConditions.elementToBeClickable(By.id("logout_sidebar_link")));
		logoutLink.click();
	}
		
}	


