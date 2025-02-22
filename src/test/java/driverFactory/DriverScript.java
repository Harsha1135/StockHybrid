package driverFactory;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import Utilities.ExcelFileUtil;
import commonFunctions.FunctionLibrary;

public class DriverScript {
WebDriver driver;
String inputpath ="./FileInput/Controller.xlsx";
String outputpath ="./FileOutPut/HybridResults.xlsx";
String TCSheet="MasterTestCases";
ExtentReports reports;
ExtentTest logger;
public void startTest() throws Throwable
{
	String Module_Status="";
	String Module_New ="";
	//create object for excel file util class
	Utilities.ExcelFileUtil xl = new ExcelFileUtil(inputpath);
	//iterate all rows in TCSheet
	for(int i=1;i<=xl.rowCount(TCSheet);i++)
	{
		if(xl.getcelldata(TCSheet, i, 2).equalsIgnoreCase("Y"))
		{
			//read module cell from TCSheet
			String TCModule =xl.getcelldata(TCSheet, i, 1);
			//define path of html
			reports = new ExtentReports("./target/Reports/"+TCModule+FunctionLibrary.generateDate()+".html");
			logger = reports.startTest(TCModule);
			//iterate all rows in TCModule
			for(int j=1;j<=xl.rowCount(TCModule);j++)
			{
				//read cell from TCModule
				String Description =xl.getcelldata(TCModule, j, 0);
				String Object_Type =xl.getcelldata(TCModule, j, 1);
				String LType = xl.getcelldata(TCModule, j, 2);
				String LValue = xl.getcelldata(TCModule, j, 3);
				String Test_Data= xl.getcelldata(TCModule, j, 4);
				try {
					if(Object_Type.equalsIgnoreCase("startBrowser"))
					{
						driver =FunctionLibrary.startBrowser();
						logger.log(LogStatus.INFO, Description);
					}
					if(Object_Type.equalsIgnoreCase("openUrl"))
					{
						FunctionLibrary.openUrl();
						logger.log(LogStatus.INFO, Description);
					}
					if(Object_Type.equalsIgnoreCase("waitForElement"))
					{
						FunctionLibrary.waitForElement(LType, LValue, Test_Data);
						logger.log(LogStatus.INFO, Description);
					}
					if(Object_Type.equalsIgnoreCase("typeAction"))
					{
						FunctionLibrary.typeAction(LType, LValue, Test_Data);
						logger.log(LogStatus.INFO, Description);
					}
					if(Object_Type.equalsIgnoreCase("clickAction"))
					{
						FunctionLibrary.clickAction(LType, LValue);
						logger.log(LogStatus.INFO, Description);
					}
					if(Object_Type.equalsIgnoreCase("validateTitle"))
					{
						FunctionLibrary.validateTitle(Test_Data);
						logger.log(LogStatus.INFO, Description);
					}
					if(Object_Type.equalsIgnoreCase("closeBrowser"))
					{
						FunctionLibrary.closeBrowser();
						logger.log(LogStatus.INFO, Description);
					}
						if(Object_Type.equalsIgnoreCase("dropDownAction"))
						{
							FunctionLibrary.dropDownAction(LType,LValue,Test_Data);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("captureStock"))
						{
							FunctionLibrary.captureStock(LType,LValue);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("stockTable"))
						{
							FunctionLibrary.stockTable();
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("captureSupplier"))
						{
							FunctionLibrary.captureSupplier(LType,LValue);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("supplierTable"))
						{
							FunctionLibrary.supplierTable();
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("captureCustomer"))
						{
							FunctionLibrary.captureCustomer(LType,LValue);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("customerTable"))
						{
							FunctionLibrary.customerTable();
							logger.log(LogStatus.INFO, Description);
						}
						//write as pass into status cell inTCmodule sheet
						xl.setCellData(TCModule, j, 5, "Pass", outputpath);
						logger.log(LogStatus.PASS, Description);
						Module_Status="true";
					}catch(Exception e)
					{
						System.out.println(e.getMessage());
						//write as fail into status cell inTCmodule sheet
						xl.setCellData(TCModule, j, 5, "Fail", outputpath);
						logger.log(LogStatus.FAIL, Description);
						Module_New="false";
		
						File screen=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
						FileUtils.copyFile(screen, new File("./target/screenshot/"+Description+FunctionLibrary.generateDate()+".png"));
					}
				if(Module_Status.equalsIgnoreCase("True"))
				{
					xl.setCellData(TCSheet, i, 3, "Pass", outputpath);
				}
				reports.endTest(logger);
				reports.flush();
			}
			if(Module_New.equalsIgnoreCase("False"))
			{
				xl.setCellData(TCSheet, i, 3, "Fail", outputpath);
			}
		}
		else
		{
			//write as blocked into statuc cell for Tescases flag to N
			xl.setCellData(TCSheet, i, 3, "Blocked", outputpath);
		}

		}
	}

	}




















