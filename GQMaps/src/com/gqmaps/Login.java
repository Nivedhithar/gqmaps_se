package com.gqmaps;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class Login {
/*Enable this, when executing in grid*/
	public static WebDriver driver;
/*Disable driver initialization when executing in grid*/
	//public WebDriver driver = new ChromeDriver();
	
/* Setting up for selenium grid*/
	@Parameters("browser")
	@BeforeTest
	public void testSetUp(String browser) throws MalformedURLException{
	/*To use in Selenium Grid*/
DesiredCapabilities dc = new DesiredCapabilities();

/*firefox*/
dc.setBrowserName("firefox");
/*for Chrome*/
//dc.setBrowserName("chrome");
dc.setPlatform(Platform.WINDOWS);

//System.setProperty("webdriver.chrome.driver","C:\\node_modules\\chromedriver\\lib\\chromedriver\\chromedriver.exe");
//ChromeOptions options=new ChromeOptions();
//options.addArguments("--disable-extensions");
//driver=new ChromeDriver(options);

//	 if(browser.equalsIgnoreCase("firefox")){
//			
//			 dc.setBrowserName("firefox");
//			    dc.setPlatform(Platform.WINDOWS);
//	        } 
//        // Check parameter value and create webdriver according it
//	        else if(browser.equalsIgnoreCase("chrome")){
//	            System.setProperty("webdriver.chrome.driver","C:\\chromedriver.exe");
//	           
//				 dc.setBrowserName("chrome");
//				    dc.setPlatform(Platform.WIN8);     }
		
	   driver = new RemoteWebDriver(new URL("http://192.168.8.146:4444/wd/hub"), dc);
}
	
	@Test(priority=1)
	public void driverClass(){
		driver.get("http://192.168.8.119:8080/GQMapsCustomerUI");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}
	
/*login Validation*/
@Test(priority=2)	
	public void loginValidation() throws IOException, BiffException, InterruptedException {
		//driver.get("http://192.168.8.119:8080/GQMapsCustomerUI");
		String FilePath = "/home/devops/Inputs.xls";
		FileInputStream fs = new FileInputStream(FilePath);
		Workbook wb = Workbook.getWorkbook(fs);
		
		// TO get the access to the sheet
		Sheet sh = wb.getSheet("Data");
		String InvalidUser=sh.getCell(0, 1).getContents();
		String Invalidpwd=sh.getCell(1,1).getContents();
		String validUser=sh.getCell(0, 2).getContents();
		String validpwd=sh.getCell(1, 2).getContents();
		
		driver.findElement(By.id("txtUserId")).sendKeys(InvalidUser);
 		driver.findElement(By.id("pwdPassword")).sendKeys(Invalidpwd);
 		driver.findElement(By.id("submitLogin")).click();
 		Thread.sleep(2000);
 		driver.switchTo().alert().accept();
 		driver.findElement(By.id("txtUserId")).sendKeys(validUser);
 		driver.findElement(By.id("pwdPassword")).sendKeys(validpwd);
 		driver.findElement(By.id("submitLogin")).click();
		System.out.println("Logged in and Navigated to Dashboard Page");
}
	
/*Personal Device Impact*/
	@Test(priority=3)
		public void menus() throws InterruptedException{
		WebDriverWait wait = new WebDriverWait(driver,300);
		WebElement alertbox= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='content']/div/div[1]/ul/li[1]/div")));
		System.out.println(alertbox.getText());
		WebElement asset=driver.findElement(By.xpath(".//*[@id='content']/div/div[1]/ul/li[2]"));
		System.out.println(asset.getText());
		WebElement pue=driver.findElement(By.xpath(".//*[@id='content']/div/div[1]/ul/li[3]"));
		System.out.println(pue.getText());
						
		WebElement Personal_device_impact = driver.findElement(By.linkText("Personal Device Impact"));
		Personal_device_impact.click();
		WebElement al=driver.findElement(By.linkText("Asset List"));
				al.click();
		
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		
		driver.switchTo().window(tabs.get(1));
		boolean value = driver.findElement(By.id("chkApply")).isSelected();
		if(value == false){
			ArrayList <WebElement> checkboxes = (ArrayList<WebElement>) driver.findElements(By.id("chkApply"));
			
			checkboxes.get(0).click();
			checkboxes.get(1).click();
			checkboxes.get(2).click();
			checkboxes.get(3).click();
			//System.out.println(((WebElement)checkboxes).getText());
/* clicking on submit after selecting asset list*/
			driver.findElement(By.xpath("//button[@class='btn btn-success']")).click();
			driver.close();
		}
		else{
			System.out.println("Assets are aleady selected");
		}
		driver.switchTo().window(tabs.get(0));
		driver.findElement(By.xpath("//button[@class ='btn btn-warning']")).click();
		}
		
/*Monitoring*/
		@Test(priority=4)
		public void monitor(){
		WebElement Monitoring = driver.findElement(By.linkText("Monitoring"));
		Monitoring.click();
		String Parent_Window=driver.getWindowHandle();
		 driver.findElement(By.linkText("Asset List")).click();
		 for (String Child_Window : driver.getWindowHandles())
		         {
		             driver.switchTo().window(Child_Window); 
		         }
		   WebElement assetId=driver.findElement(By.xpath(".//*[@id='tblComputerList']/tbody/tr[1]/td[2]"));
           String asId=assetId.getText();
           driver.close();
		         
		 driver.switchTo().window(Parent_Window);
		 driver.findElement(By.id("AssetId")).sendKeys(asId);
		 driver.findElement(By.id("Date")).sendKeys("09/09/2016");
		 driver.findElement(By.id("btnAsset")).click();
		 driver.findElement(By.xpath("//button[@class='btn btn-warning']")).click();
		         }
/*Virtualization*/
		@Test(priority=5)
		public void virtual(){
			Actions action= new Actions(driver);
		WebElement Virtualization = driver.findElement(By.linkText("Virtualization"));
		Virtualization.click();
		
		WebElement sys=driver.findElement(By.id("sysnotes"));
		//sys.sendKeys("localhost");
		action.moveToElement(sys).sendKeys("localhost");
		WebElement benefit=driver.findElement(By.id("cost_benefit"));
		action.moveToElement(benefit).sendKeys("1");
		WebElement user=driver.findElement(By.xpath("//textarea[@id='usrnts']"));
		action.moveToElement(user).sendKeys("least loaded server");
		driver.findElement(By.xpath("//button[@id='save']")).click();
		}

/*Getting details of Dashboard Contents*/
		@Test(priority=6)
		public void dashboard(){
			
/*for loop is used to avoid Stale element reference exception*/
		for(int i=0;i<=2;i++)
		{
		try{
			WebDriverWait wait = new WebDriverWait(driver,300);
			Actions action= new Actions(driver);
		WebElement TopLoadedAssets=driver.findElement(By.xpath("//i[@class='icon-inbox']"));
		TopLoadedAssets.click();
		WebElement close=driver.findElement(By.linkText("close"));
		close.click();
		WebElement TopInstalledSoftware=wait.until(ExpectedConditions.visibilityOfElementLocated((By.xpath("//i[@class='icon-gift']"))));
		action.moveToElement(TopInstalledSoftware).click().perform();
		WebElement close1=driver.findElement(By.linkText("close"));
		close1.click();
		WebElement TopBlackListedSoftware=wait.until(ExpectedConditions.visibilityOfElementLocated((By.xpath("//i[@class='icon-exclamation-sign']"))));
		action.moveToElement(TopBlackListedSoftware).click().perform();
		WebElement close2=driver.findElement(By.linkText("close"));
		close2.click();
		//driver.navigate().refresh();
		WebElement leastandmostloadedAssets=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//i[@class='icon-signal']")));
		action.moveToElement(leastandmostloadedAssets).click().perform();
		WebElement close3=driver.findElement(By.linkText("close"));
		close3.click();
		
		WebElement MostConnectedServerAssets=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//i[@class='icon-list-alt']")));
		action.moveToElement(MostConnectedServerAssets).click().perform();
		WebElement close4=driver.findElement(By.linkText("close"));
		close4.click();
  }
	catch(Exception e){
		System.out.println(e.getMessage());
	}
	}
	}

/*Current Compute Duration*/
		@Test(priority=7)
		public  void compute() throws InterruptedException{
			WebDriverWait wait = new WebDriverWait(driver,300);
			Actions action=new Actions(driver);
			Thread.sleep(1000);
		for(int i=0;i<=2;i++){
			WebElement computeDuration=wait.until(ExpectedConditions.elementToBeClickable(By.xpath(("//label[@id='fader']"))));
		action.moveToElement(computeDuration).click().perform();
		WebElement slider=driver.findElement(By.id("slide"));
		action.click(slider).build().perform();
		Thread.sleep(1000);
		for (int s = 0; i < 30; i++) {
		    action.dragAndDropBy(slider, 0, 20).build().perform();
		     Thread.sleep(200);
			}
	    driver.findElement(By.xpath("//button[@class='btn btn-info']")).click();
		driver.findElement(By.xpath("//button[@id='btnConfAssets']")).click();
		Select configure=new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@id='cmbStatus3']"))));
		configure.selectByIndex(0);
		driver.findElement(By.xpath("//button[@id='submit']")).click();
		driver.switchTo().alert().accept();
		driver.findElement(By.xpath("//button[@class='btn btn-warning']")).click();
		/*Logout*/		
		WebElement logout=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div/ul/li/a")));
		logout.click();
			}
		}
		@AfterSuite
		public void tearsDown(){
			driver.quit();
		}
	}