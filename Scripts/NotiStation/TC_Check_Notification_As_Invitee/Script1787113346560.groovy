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
    // Step 1: Open NEW BROWSER and login as invitee
    WebUI.comment("=== Step 1: Open NEW BROWSER and login as invitee ===")
    println("=== Step 1: Open NEW BROWSER and login as invitee ===")
    
    String inviteeEmail = GlobalVariable.INVITEE_EMAIL
    String inviteePassword = GlobalVariable.INVITEE_PASSWORD
    
    // Open NEW browser instance (not new tab)
    WebUI.openBrowser('')
    WebUI.navigateToUrl(GlobalVariable.BASE_URL)
    WebUI.waitForPageLoad(10)
    
    WebUI.setText(findTestObject('Object Repository/Sign_In/input_email'), inviteeEmail)
    WebUI.setText(findTestObject('Object Repository/Sign_In/input_password'), inviteePassword)
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
    
    // Step 2: Check notification bell has badge
    WebUI.comment("=== Step 2: Check notification badge ===")
    println("=== Step 2: Check notification badge ===")
    
    WebUI.waitForElementVisible(findTestObject('Object Repository/Dashboard/button_Noti-Station_Dashboard'), 15)
    
//    // Check if notification badge exists
//    boolean hasBadge = WebUI.verifyElementPresent(
//        findTestObject('Object Repository/Dashboard/div_Noti-Station_Badge'),
//        5,
//        FailureHandling.OPTIONAL
//    )
//    
//    if (!hasBadge) {
//        WebUI.comment("Notification badge should be present for invitee")
//        println("Notification badge should be present for invitee")
//    }
//    WebUI.verifyEqual(hasBadge, true)
//    WebUI.comment("Notification badge is present")
//    println("Notification badge is present")
    
    // Step 3: Click to open notification popover
    WebUI.comment("=== Step 3: Open notification popover ===")
    println("=== Step 3: Open notification popover ===")
    
    WebUI.click(findTestObject('Object Repository/Dashboard/button_Noti-Station_Dashboard'))
    WebUI.waitForElementVisible(findTestObject('Object Repository/Dashboard/div_Noti-Station_Popover'), 10)
    WebUI.comment("Notification popover opened")
    println("Notification popover opened")
    
    // Step 4: Verify invitation notification exists using dynamic XPath
    WebUI.comment("=== Step 4: Verify invitation notification content ===")
    println("=== Step 4: Verify invitation notification content ===")
    
    // Get all notification items in the popover as WebElement
    String notificationXPath = "//div[@data-slot='popover-content']//div[contains(@class, 'p-4')]"
    List<WebElement> notificationItems = WebUI.findWebElements(
        new TestObject().addProperty('xpath', ConditionType.EQUALS, notificationXPath),
        10
    )
    
    WebUI.comment("Found " + notificationItems.size() + " notification items")
    println("Found " + notificationItems.size() + " notification items")
    
    // Find the invitation notification for the correct station using GlobalVariable.STATION_NAME
    boolean hasInvitation = false
    String stationName = GlobalVariable.STATION_NAME
    
    for (WebElement item : notificationItems) {
        String content = item.getText()
        WebUI.comment("Notification content: " + content)
        println("Notification content: " + content)
        
        // Check if this notification matches the correct station name
        if (content.contains("invited you to join Station:") && content.contains(stationName)) {
            hasInvitation = true
            WebUI.comment("Found correct invitation for Station: " + stationName)
            println("Found correct invitation for Station: " + stationName)
            break
        }
    }
    
    if (!hasInvitation) {
        WebUI.comment("Invitation notification for station '" + stationName + "' not found")
        println("Invitation notification for station '" + stationName + "' not found")
    }
    WebUI.verifyEqual(hasInvitation, true)
    WebUI.comment("Found expected message in notification")
    println("Found expected message in notification")
    
    // Step 5: Click Accept button for the correct invitation using dynamic XPath
    WebUI.comment("=== Step 5: Accept invitation ===")
    println("=== Step 5: Accept invitation ===")
    
    // Find the Accept button that corresponds to the correct station
    String acceptButtonXPath = "//div[@data-slot='popover-content']//div[contains(@class, 'p-4')][.//div[contains(text(), '" + stationName + "')]]//button[contains(@class, 'bg-primary') and contains(text(), 'Accept')]"
    
    TestObject acceptButton = new TestObject().addProperty('xpath', ConditionType.EQUALS, acceptButtonXPath)
    
    WebUI.waitForElementVisible(acceptButton, 10)
    WebUI.click(acceptButton)
    WebUI.waitForPageLoad(15)
    WebUI.comment("Accepted invitation for Station: " + stationName)
    println("Accepted invitation for Station: " + stationName)
    
    // Step 6: Verify redirected to the Station workspace
    WebUI.comment("=== Step 6: Verify redirect to Station workspace ===")
    println("=== Step 6: Verify redirect to Station workspace ===")
    
    String currentUrlAfterAccept = WebUI.getUrl()
    WebUI.comment("Current URL after accept: " + currentUrlAfterAccept)
    println("Current URL after accept: " + currentUrlAfterAccept)
    
    boolean isInWs = currentUrlAfterAccept.contains('/ws/')
    if (!isInWs) {
        WebUI.comment("Should be redirected to Station workspace. Current URL: " + currentUrlAfterAccept)
        println("Should be redirected to Station workspace. Current URL: " + currentUrlAfterAccept)
    }
    WebUI.verifyEqual(isInWs, true)
    
    WebUI.comment("Invitee successfully accepted invitation and joined the Station")
    println("Invitee successfully accepted invitation and joined the Station")
    WebUI.comment("=== Test Case TC_Check_Notification_As_Invitee completed successfully ===")
    println("=== Test Case TC_Check_Notification_As_Invitee completed successfully ===")
    
} catch (Exception e) {
    WebUI.comment("Test Case TC_Check_Notification_As_Invitee failed: " + e.getMessage())
    println("Test Case TC_Check_Notification_As_Invitee failed: " + e.getMessage())
    KeywordUtil.markFailed("Test Case failed: " + e.getMessage())
    throw e
}