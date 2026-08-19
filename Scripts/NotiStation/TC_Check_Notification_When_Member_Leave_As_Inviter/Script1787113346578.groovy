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
    // ===== DEBUG: Check Global Variables =====
    WebUI.comment("=== CHECK GLOBAL VARIABLES ===")
    println("=== CHECK GLOBAL VARIABLES ===")
    WebUI.comment("STATION_NAME: " + GlobalVariable.STATION_NAME)
    println("STATION_NAME: " + GlobalVariable.STATION_NAME)
    WebUI.comment("INVITEE_USERNAME: " + GlobalVariable.INVITEE_USERNAME)
    println("INVITEE_USERNAME: " + GlobalVariable.INVITEE_USERNAME)
    WebUI.comment("=============================")
    println("=============================")
    
    // Step 1: Open browser and login as inviter (lieucao16122003)
    WebUI.comment("=== Step 1: Open browser and login as inviter ===")
    println("=== Step 1: Open browser and login as inviter ===")
    
    WebUI.openBrowser('')
    WebUI.navigateToUrl(GlobalVariable.BASE_URL)
    WebUI.waitForPageLoad(10)
    
    WebUI.setText(findTestObject('Object Repository/Sign_In/input_email'), GlobalVariable.Email)
    WebUI.setText(findTestObject('Object Repository/Sign_In/input_password'), GlobalVariable.Password)
    WebUI.click(findTestObject('Object Repository/Sign_In/button_Sign in'))
    
    WebUI.waitForPageLoad(15)
    WebUI.delay(2)
    
    String currentUrl = WebUI.getUrl()
    boolean isLoggedIn = currentUrl.contains('/ws/dashboard')
    if (!isLoggedIn) {
        WebUI.comment("Login failed for inviter. Current URL: " + currentUrl)
        println("Login failed for inviter. Current URL: " + currentUrl)
    }
    WebUI.verifyEqual(isLoggedIn, true)
    WebUI.comment("Inviter login successful. Current URL: " + currentUrl)
    println("Inviter login successful. Current URL: " + currentUrl)
    
    // Step 2: Check notification bell has badge on Dashboard
    WebUI.comment("=== Step 2: Check notification badge on Dashboard ===")
    println("=== Step 2: Check notification badge on Dashboard ===")
    
    WebUI.waitForElementVisible(findTestObject('Object Repository/Dashboard/button_Noti-Station_Dashboard'), 15)
    
    // Check if notification badge exists
    boolean hasBadge = WebUI.verifyElementPresent(
        findTestObject('Object Repository/Dashboard/div_Noti-Station_Badge'),
        10,
        FailureHandling.OPTIONAL
    )
    
    if (!hasBadge) {
        WebUI.comment("Notification badge should be present for inviter")
        println("Notification badge should be present for inviter")
    }
    WebUI.verifyEqual(hasBadge, true)
    WebUI.comment("Notification badge is present on Dashboard")
    println("Notification badge is present on Dashboard")
    
    // Step 3: Click to open notification popover
    WebUI.comment("=== Step 3: Open notification popover on Dashboard ===")
    println("=== Step 3: Open notification popover on Dashboard ===")
    
    WebUI.click(findTestObject('Object Repository/Dashboard/button_Noti-Station_Dashboard'))
    WebUI.waitForElementVisible(findTestObject('Object Repository/Dashboard/div_Noti-Station_Popover'), 10)
    WebUI.comment("Notification popover opened on Dashboard")
    println("Notification popover opened on Dashboard")
    
    // Wait a bit for notifications to fully load
    WebUI.delay(2)
    
    // Step 4: Verify notification about member left
    WebUI.comment("=== Step 4: Verify notification about member left ===")
    println("=== Step 4: Verify notification about member left ===")
    
    // Get all notification items in the popover as WebElement
    String notificationXPath = "//div[@data-slot='popover-content']//div[contains(@class, 'p-4')]"
    List<WebElement> notificationItems = WebUI.findWebElements(
        new TestObject().addProperty('xpath', ConditionType.EQUALS, notificationXPath),
        10
    )
    
    WebUI.comment("Found " + notificationItems.size() + " notification items")
    println("Found " + notificationItems.size() + " notification items")
    
    // Find the notification about member leaving
    boolean hasLeftNotification = false
    String stationName = GlobalVariable.STATION_NAME
    String inviteeUsername = GlobalVariable.INVITEE_USERNAME
    
    WebUI.comment("=== Searching for notification ===")
    println("=== Searching for notification ===")
    WebUI.comment("Looking for stationName: '" + stationName + "'")
    println("Looking for stationName: '" + stationName + "'")
    WebUI.comment("Looking for inviteeUsername: '" + inviteeUsername + "'")
    println("Looking for inviteeUsername: '" + inviteeUsername + "'")
    
    // Try both possible message formats
    String[] expectedMessages = ["left Station:", "has left Station:"]
    String foundStationName = ""
    
    for (WebElement item : notificationItems) {
        String content = item.getText()
        WebUI.comment("Notification content: " + content)
        println("Notification content: " + content)
        
        for (String msg : expectedMessages) {
            if (content.contains(msg) && content.contains(stationName) && content.contains(inviteeUsername)) {
                hasLeftNotification = true
                foundStationName = stationName
                WebUI.comment(">>> FOUND! " + inviteeUsername + " left Station: " + stationName)
                println(">>> FOUND! " + inviteeUsername + " left Station: " + stationName)
                break
            }
        }
        if (hasLeftNotification) {
            break
        }
    }
    
    // If not found, try to find by username only (for debugging)
    if (!hasLeftNotification) {
        WebUI.comment("=== DEBUG: Notification not found with stationName ===")
        println("=== DEBUG: Notification not found with stationName ===")
        WebUI.comment("Trying to find by username only...")
        println("Trying to find by username only...")
        
        for (WebElement item : notificationItems) {
            String content = item.getText()
            if (content.contains(inviteeUsername) && (content.contains("left Station:") || content.contains("has left Station:"))) {
                WebUI.comment(">>> Found notification with username but station name mismatch!")
                println(">>> Found notification with username but station name mismatch!")
                WebUI.comment("Content: " + content)
                println("Content: " + content)
                
                // Extract station name from notification
                int startIdx = content.indexOf("Station:") + 9
                if (startIdx > 8) {
                    String extractedStation = content.substring(startIdx).trim()
                    WebUI.comment("Extracted station name from notification: '" + extractedStation + "'")
                    println("Extracted station name from notification: '" + extractedStation + "'")
                    WebUI.comment("Expected station name: '" + stationName + "'")
                    println("Expected station name: '" + stationName + "'")
                    foundStationName = extractedStation
                }
                break
            }
        }
    }
    
    if (!hasLeftNotification) {
        WebUI.comment("=== WARNING: Notification not found ===")
        println("=== WARNING: Notification not found ===")
        WebUI.comment("Notification for station '" + stationName + "' and user '" + inviteeUsername + "' not found")
        println("Notification for station '" + stationName + "' and user '" + inviteeUsername + "' not found")
        
        // Try to check if there is any notification with "left Station:"
        WebUI.comment("Checking if any 'left Station:' notification exists...")
        println("Checking if any 'left Station:' notification exists...")
        for (WebElement item : notificationItems) {
            String content = item.getText()
            if (content.contains("left Station:")) {
                WebUI.comment("Found a 'left Station:' notification: " + content)
                println("Found a 'left Station:' notification: " + content)
                int startIdx = content.indexOf("Station:") + 9
                if (startIdx > 8) {
                    String extracted = content.substring(startIdx).trim()
                    WebUI.comment("Station name in notification: '" + extracted + "'")
                    println("Station name in notification: '" + extracted + "'")
                }
                break
            }
        }
    }
    WebUI.verifyEqual(hasLeftNotification, true)
    WebUI.comment("Found notification about member leaving")
    println("Found notification about member leaving")
    
    // Step 5: Verify unread dot indicator
    WebUI.comment("=== Step 5: Verify unread dot indicator ===")
    println("=== Step 5: Verify unread dot indicator ===")
    
    // Use the actual station name found, or fallback to stationName
    String stationToCheck = foundStationName.isEmpty() ? stationName : foundStationName
    
//    String unreadDotXPath = "//div[@data-slot='popover-content']//div[contains(@class, 'p-4')][.//div[contains(text(), '" + stationToCheck + "')]]//div[contains(@class, 'mt-1.5') and contains(@class, 'bg-primary')]"
	String unreadDotXPath = "//div[@data-slot='popover-content']//div[contains(@class, 'p-4')][contains(., '" + stationToCheck + "')]//div[contains(@class, 'mt-1.5') and contains(@class, 'bg-primary')]"
	
    boolean hasUnreadDot = WebUI.verifyElementPresent(
        new TestObject().addProperty('xpath', ConditionType.EQUALS, unreadDotXPath),
        5,
        FailureHandling.OPTIONAL
    )
    
    if (!hasUnreadDot) {
        WebUI.comment("Unread notification should have a dot indicator")
        println("Unread notification should have a dot indicator")
    }
    WebUI.verifyEqual(hasUnreadDot, true)
    WebUI.comment("Unread dot indicator is present")
    println("Unread dot indicator is present")
    
    WebUI.comment("Inviter received notification about member leaving")
    println("Inviter received notification about member leaving")
    WebUI.comment("=== Test Case TC_Check_Notification_When_Member_Leave_As_Inviter completed successfully ===")
    println("=== Test Case TC_Check_Notification_When_Member_Leave_As_Inviter completed successfully ===")
    
} catch (Exception e) {
    WebUI.comment("Test Case TC_Check_Notification_When_Member_Leave_As_Inviter failed: " + e.getMessage())
    println("Test Case TC_Check_Notification_When_Member_Leave_As_Inviter failed: " + e.getMessage())
    KeywordUtil.markFailed("Test Case failed: " + e.getMessage())
    throw e
}