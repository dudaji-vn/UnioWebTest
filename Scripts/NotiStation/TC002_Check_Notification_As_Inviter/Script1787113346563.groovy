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
    // Step 1: Open NEW BROWSER and login as inviter (lieucao16122003)
    WebUI.comment("=== Step 1: Open NEW BROWSER and login as inviter ===")
    println("=== Step 1: Open NEW BROWSER and login as inviter ===")
    
    // Open NEW browser instance
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
    
    // Check if notification badge exists before clicking
    boolean hasBadgeBeforeClick = WebUI.verifyElementPresent(
        findTestObject('Object Repository/Dashboard/div_Noti-Station_Badge'),
        5,
        FailureHandling.OPTIONAL
    )
    
    if (!hasBadgeBeforeClick) {
        WebUI.comment("Notification badge should be present for inviter before opening popover")
        println("Notification badge should be present for inviter before opening popover")
    }
    WebUI.verifyEqual(hasBadgeBeforeClick, true)
    WebUI.comment("Notification badge is present on Dashboard before opening popover")
    println("Notification badge is present on Dashboard before opening popover")
    
    // Step 3: Click to open notification popover on Dashboard
    WebUI.comment("=== Step 3: Open notification popover on Dashboard ===")
    println("=== Step 3: Open notification popover on Dashboard ===")
    
    WebUI.click(findTestObject('Object Repository/Dashboard/button_Noti-Station_Dashboard'))
    WebUI.waitForElementVisible(findTestObject('Object Repository/Dashboard/div_Noti-Station_Popover'), 10)
    WebUI.comment("Notification popover opened on Dashboard")
    println("Notification popover opened on Dashboard")
    
    // Step 4: Verify badge disappears after opening popover
    WebUI.comment("=== Step 4: Verify notification badge disappears after opening popover ===")
    println("=== Step 4: Verify notification badge disappears after opening popover ===")
    
    boolean hasBadgeAfterClick = WebUI.verifyElementPresent(
        findTestObject('Object Repository/Dashboard/div_Noti-Station_Badge'),
        3,
        FailureHandling.OPTIONAL
    )
    
    if (hasBadgeAfterClick) {
        WebUI.comment("Notification badge should disappear after opening popover")
        println("Notification badge should disappear after opening popover")
    }
    WebUI.verifyEqual(hasBadgeAfterClick, false)
    WebUI.comment("Notification badge disappeared after opening popover")
    println("Notification badge disappeared after opening popover")
    
    // Step 5: Verify notification about new member joined with correct member and station
    WebUI.comment("=== Step 5: Verify notification about new member joined ===")
    println("=== Step 5: Verify notification about new member joined ===")
    
    // Get all notification items in the popover as WebElement
    String notificationXPath = "//div[@data-slot='popover-content']//div[contains(@class, 'p-4')]"
    List<WebElement> notificationItems = WebUI.findWebElements(
        new TestObject().addProperty('xpath', ConditionType.EQUALS, notificationXPath),
        10
    )
    
    WebUI.comment("Found " + notificationItems.size() + " notification items")
    println("Found " + notificationItems.size() + " notification items")
    
    // Find the notification about new member joining the correct station
    boolean hasJoinedNotification = false
    String stationName = GlobalVariable.STATION_NAME
    String inviteeUsername = GlobalVariable.INVITEE_USERNAME
    String expectedMessage = "has joined Station:"
    
    WebUI.comment("stationName: " + stationName)
    println("stationName: " + stationName)
    WebUI.comment("inviteeUsername: " + inviteeUsername)
    println("inviteeUsername: " + inviteeUsername)
    
    for (WebElement item : notificationItems) {
        String content = item.getText()
        WebUI.comment("Notification content: " + content)
        println("Notification content: " + content)
        
        // Check if this notification matches the correct station name and username
        if (content.contains(expectedMessage) && content.contains(stationName) && content.contains(inviteeUsername)) {
            hasJoinedNotification = true
            WebUI.comment("Found notification: " + inviteeUsername + " has joined Station: " + stationName)
            println("Found notification: " + inviteeUsername + " has joined Station: " + stationName)
            break
        }
    }
    
    if (!hasJoinedNotification) {
        WebUI.comment("Notification for station '" + stationName + "' and user '" + inviteeUsername + "' not found")
        println("Notification for station '" + stationName + "' and user '" + inviteeUsername + "' not found")
    }
    WebUI.verifyEqual(hasJoinedNotification, true)
    WebUI.comment("Found notification with correct member and station")
    println("Found notification with correct member and station")
    
    // Step 6: Verify the notification has unread indicator (small dot)
    WebUI.comment("=== Step 6: Verify unread dot indicator ===")
    println("=== Step 6: Verify unread dot indicator ===")
    
    // Find the unread dot for the correct notification
    // The dot is a div with classes: mt-1.5 h-2 w-2 shrink-0 rounded-full bg-primary
    // It is located inside the notification item that contains the station name
    String unreadDotXPath = "//div[@data-slot='popover-content']//div[contains(@class, 'p-4')][.//div[contains(text(), '" + stationName + "')]]//div[contains(@class, 'flex')]//div[contains(@class, 'mt-1.5') and contains(@class, 'h-2') and contains(@class, 'w-2') and contains(@class, 'rounded-full') and contains(@class, 'bg-primary')]"
    
    boolean hasUnreadDot = WebUI.verifyElementPresent(
        new TestObject().addProperty('xpath', ConditionType.EQUALS, unreadDotXPath),
        5,
        FailureHandling.OPTIONAL
    )
    
    // Fallback: try simpler XPath if not found
    if (!hasUnreadDot) {
        WebUI.comment("Trying simpler XPath for unread dot...")
        println("Trying simpler XPath for unread dot...")
        
        String simpleUnreadDotXPath = "//div[@data-slot='popover-content']//div[contains(@class, 'p-4')][.//div[contains(text(), '" + stationName + "')]]//div[contains(@class, 'mt-1.5') and contains(@class, 'bg-primary')]"
        
        hasUnreadDot = WebUI.verifyElementPresent(
            new TestObject().addProperty('xpath', ConditionType.EQUALS, simpleUnreadDotXPath),
            5,
            FailureHandling.OPTIONAL
        )
    }
    
    // Second fallback: find any unread dot in the popover
    if (!hasUnreadDot) {
        WebUI.comment("Trying fallback XPath for unread dot...")
        println("Trying fallback XPath for unread dot...")
        
        String fallbackUnreadDotXPath = "//div[@data-slot='popover-content']//div[contains(@class, 'mt-1.5') and contains(@class, 'h-2') and contains(@class, 'w-2') and contains(@class, 'rounded-full') and contains(@class, 'bg-primary')]"
        
        hasUnreadDot = WebUI.verifyElementPresent(
            new TestObject().addProperty('xpath', ConditionType.EQUALS, fallbackUnreadDotXPath),
            5,
            FailureHandling.OPTIONAL
        )
    }
    
    if (!hasUnreadDot) {
        WebUI.comment("Unread notification should have a dot indicator but not found")
        println("Unread notification should have a dot indicator but not found")
    }
    WebUI.verifyEqual(hasUnreadDot, true)
    WebUI.comment("Unread dot indicator is present")
    println("Unread dot indicator is present")
    
    WebUI.comment("Inviter received notification about new member joining")
    println("Inviter received notification about new member joining")
    WebUI.comment("=== Test Case TC_Check_Notification_As_Inviter completed successfully ===")
    println("=== Test Case TC_Check_Notification_As_Inviter completed successfully ===")
    
} catch (Exception e) {
    WebUI.comment("Test Case TC_Check_Notification_As_Inviter failed: " + e.getMessage())
    println("Test Case TC_Check_Notification_As_Inviter failed: " + e.getMessage())
    KeywordUtil.markFailed("Test Case failed: " + e.getMessage())
    throw e
}