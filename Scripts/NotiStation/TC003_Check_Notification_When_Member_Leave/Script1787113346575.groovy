import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testcase.TestCaseFactory
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testdata.TestDataFactory
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import com.kms.katalon.core.util.KeywordUtil
import org.openqa.selenium.Keys
import org.openqa.selenium.WebElement

// Test execution
try {
    // Step 1: Open browser and login as invitee (lieuctse172847) - the member who will leave
    WebUI.comment("=== Step 1: Open browser and login as invitee ===")
    println("=== Step 1: Open browser and login as invitee ===")
    
    WebUI.openBrowser('')
    WebUI.navigateToUrl(GlobalVariable.BASE_URL)
    WebUI.waitForPageLoad(10)
    
    WebUI.setText(findTestObject('Object Repository/Sign_In/input_email'), GlobalVariable.INVITEE_EMAIL)
    WebUI.setText(findTestObject('Object Repository/Sign_In/input_password'), GlobalVariable.INVITEE_PASSWORD)
    WebUI.click(findTestObject('Object Repository/Sign_In/button_Sign in'))
    
    WebUI.waitForPageLoad(15)
    WebUI.delay(2)
    
    String currentUrl = WebUI.getUrl()
    boolean isLoggedIn = currentUrl.contains('/ws/dashboard')
    if (!isLoggedIn) {
        WebUI.comment("Login failed for invitee. Current URL: " + currentUrl)
        println("Login failed for invitee. Current URL: " + currentUrl)
    }
    WebUI.verifyEqual(isLoggedIn, true)
    WebUI.comment("Invitee login successful. Current URL: " + currentUrl)
    println("Invitee login successful. Current URL: " + currentUrl)
    
    // Step 2: Navigate to the Station workspace
    WebUI.comment("=== Step 2: Navigate to Station workspace ===")
    println("=== Step 2: Navigate to Station workspace ===")
    
    String stationName = GlobalVariable.STATION_NAME
    WebUI.comment("Looking for station: " + stationName)
    println("Looking for station: " + stationName)
    
    // Click on the station from dashboard
    String stationXPath = "//div[@class='grid w-full grid-cols-1 xs:grid-cols-2 gap-5 sm:grid-cols-2 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4']//div[contains(@class, 'relative')][.//div[contains(text(), '" + stationName + "')]]"
    TestObject stationItem = new TestObject().addProperty('xpath', ConditionType.EQUALS, stationXPath)
    
    WebUI.waitForElementVisible(stationItem, 15)
    WebUI.click(stationItem)
    WebUI.waitForPageLoad(15)
    WebUI.delay(2)
    
    String currentUrlAfterNavigate = WebUI.getUrl()
    WebUI.comment("Navigated to station workspace: " + currentUrlAfterNavigate)
    println("Navigated to station workspace: " + currentUrlAfterNavigate)
    
    // Step 3: Click Settings button
    WebUI.comment("=== Step 3: Click Settings button ===")
    println("=== Step 3: Click Settings button ===")
    
    WebUI.click(findTestObject('Object Repository/WS/Setting/a_Settings'))
    WebUI.waitForPageLoad(15)
    WebUI.delay(2)
    
    String settingsUrl = WebUI.getUrl()
    WebUI.comment("Settings URL: " + settingsUrl)
    println("Settings URL: " + settingsUrl)
    
    // Verify settings URL format
    boolean isValidSettingsUrl = settingsUrl.matches("https://app.unio.chat/ws/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}/settings")
    WebUI.verifyEqual(isValidSettingsUrl, true)
    WebUI.comment("Settings URL format is valid")
    println("Settings URL format is valid")
    
    // Step 4: Click Leave Station button on settings page
    WebUI.comment("=== Step 4: Click Leave Station button on settings page ===")
    println("=== Step 4: Click Leave Station button on settings page ===")
    
    WebUI.click(findTestObject('Object Repository/WS/Setting/button_Leave Station-setting-page'))
    
    // Wait for confirmation dialog to appear
    WebUI.waitForElementVisible(findTestObject('Object Repository/WS/Setting/dialog_confirm leave'), 10)
    WebUI.comment("Leave confirmation dialog opened")
    println("Leave confirmation dialog opened")
    
    // Step 5: Enter station name to confirm using input_to-confirm
    WebUI.comment("=== Step 5: Enter station name to confirm ===")
    println("=== Step 5: Enter station name to confirm ===")
    
    WebUI.setText(findTestObject('Object Repository/WS/Setting/input_to-confirm'), stationName)
    WebUI.comment("Entered station name: " + stationName)
    println("Entered station name: " + stationName)
    
    // Wait a moment for the button to become enabled
    WebUI.delay(1)
    
    // Step 6: Click Leave Station button to confirm
    WebUI.comment("=== Step 6: Click Leave Station button to confirm ===")
    println("=== Step 6: Click Leave Station button to confirm ===")
    
    WebUI.click(findTestObject('Object Repository/WS/Setting/button_Leave Station'))
    WebUI.waitForPageLoad(15)
    WebUI.delay(3)
    
    // Step 7: Verify redirected to dashboard after leaving
    WebUI.comment("=== Step 7: Verify redirected to dashboard ===")
    println("=== Step 7: Verify redirected to dashboard ===")
    
    String currentUrlAfterLeave = WebUI.getUrl()
    WebUI.comment("Current URL after leaving: " + currentUrlAfterLeave)
    println("Current URL after leaving: " + currentUrlAfterLeave)
    
    boolean isOnDashboard = currentUrlAfterLeave.contains('/ws/dashboard')
    WebUI.verifyEqual(isOnDashboard, true)
    WebUI.comment("Successfully left the station")
    println("Successfully left the station")
    
    WebUI.comment("=== Test Case TC_Check_Notification_When_Member_Leave completed successfully ===")
    println("=== Test Case TC_Check_Notification_When_Member_Leave completed successfully ===")
    
} catch (Exception e) {
    WebUI.comment("Test Case TC_Check_Notification_When_Member_Leave failed: " + e.getMessage())
    println("Test Case TC_Check_Notification_When_Member_Leave failed: " + e.getMessage())
    KeywordUtil.markFailed("Test Case failed: " + e.getMessage())
    throw e
}