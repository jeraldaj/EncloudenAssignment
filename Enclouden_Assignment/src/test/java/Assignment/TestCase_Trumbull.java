package Assignment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class TestCase_Trumbull {
	
	public static WebDriver driver;
	
	@Test
	public void testCase() throws InterruptedException, ParseException {
		
    driver = new ChromeDriver(); 
		
		
	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	driver.manage().window().maximize();
	driver.get("https://sso.eservices.jud.ct.gov/foreclosures/Public/PendPostbyTownList.aspx");
	  Thread.sleep(2000);
	JavascriptExecutor js = (JavascriptExecutor) driver;
	js.executeScript("window.scrollBy(0,800)");
	
	
	driver.findElement(By.partialLinkText("Trumbull")).click();
	Thread.sleep(2000);

    //Selecting Sale Dates dynamically
	  
	 List<WebElement> ele_dates = driver.findElements(By.xpath("//table[@id='ctl00_cphBody_GridView1']/tbody/tr/td[2]/span[1]"));
		  
	 System.out.println("No of dates = "+ ele_dates.size()); 
		
	 ArrayList<String> saleDatesList=new ArrayList<String>();
	 ArrayList<Integer> diffDateList = new ArrayList<Integer>();		  
     for(int k=0;k<ele_dates.size();k++) 
	 {
	 String saleDateStr = ele_dates.get(k).getText();
	 String [] dateParts = saleDateStr.split("\n");
	 String Formatted_sale_Date = dateParts[0];	
	 saleDatesList.add(Formatted_sale_Date);	
	 }		
  
		 //Parsing the Sale Dates into ArrayList
		  
	 for(int j=0; j<saleDatesList.size();j++) {
	 System.out.println("Sale_Date_"+j+": "+saleDatesList.get(j));
	 String sale_date = saleDatesList.get(j);
	 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");  
	 LocalDateTime now = LocalDateTime.now();  
	 String currentDate =(dtf.format(now));  
				
     SimpleDateFormat sdformat = new SimpleDateFormat("MM/dd/yyyy"); 
	 
     Date d1 =sdformat.parse(sale_date); 
	 Date d2 = sdformat.parse(currentDate);

	 long diffInMillies = Math.abs(d2.getTime() - d1.getTime());
	 long diff_date = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);	     
	 diffDateList.add((int) diff_date); 
         }	
	  
	  //Clicking on the 'View Notice' link within seven days.
	     
	  List<WebElement> alllinks = driver.findElements(By.xpath("//table[@id='ctl00_cphBody_GridView1']/tbody/tr/td[3]/a"));
	  ArrayList<String> dockerList = new ArrayList<String>();
	  for(int i =0;i<alllinks.size();i++) {
	   dockerList.add(alllinks.get(i).getText());	
	   }

       for(int m=0;m<diffDateList.size(); m++) {
       int date = diffDateList.get(m);
    	  
   	   if(date>=0 && date<=7) { 
   	   System.out.println("Valid date difference: "+date+" days");
   	        	
   	   for(int j=0;j<dockerList.size();j++) {
   	   System.out.println("Clicking on the View full Notice link");   	 
       String dock = dockerList.get(j);
       System.out.println("Docket Number = "+dock);  
       driver.findElement(By.linkText(dock)).click();
    	Thread.sleep(3000);

       driver.findElement(By.linkText("View Full Notice")).click();
       Thread.sleep(2000);
       driver.navigate().back();
	   Thread.sleep(2000);
	   driver.navigate().back();
	   Thread.sleep(2000);
		   break;
               } 
   	         }
   	        else {
    	  System.out.println("Skipped due to 'Invalid Date' difference = "+date+" days");
    	  break;
    }
	}
    	//driver.close();
	}
	
}
	
	
	
	