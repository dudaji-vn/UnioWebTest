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
import org.openqa.selenium.interactions.Actions
import com.kms.katalon.core.webui.driver.DriverFactory

try {
    // ==================== Step 1: Login as INVITER (lieucao16122003) ====================
    WebUI.comment("=== Step 1: Login as inviter ===")
    println("=== Step 1: Login as inviter ===")

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

    // ==================== Step 2: Navigate to the Station workspace ====================
    WebUI.comment("=== Step 2: Navigate to the Station workspace ===")
    println("=== Step 2: Navigate to the Station workspace ===")

    String stationName = GlobalVariable.STATION_NAME
    WebUI.comment("Looking for station: " + stationName)
    println("Looking for station: " + stationName)

    String stationXPath = "//div[contains(@class, 'grid') and contains(@class, 'grid-cols')]//div[contains(@class, 'relative')][.//div[contains(text(), '" + stationName + "')]]"
    TestObject stationItem = new TestObject().addProperty('xpath', ConditionType.EQUALS, stationXPath)

    WebUI.waitForElementVisible(stationItem, 15)
    WebUI.click(stationItem)
    WebUI.waitForPageLoad(15)
    WebUI.delay(2)

    String currentUrlAfterNavigate = WebUI.getUrl()
    WebUI.comment("Navigated to station workspace: " + currentUrlAfterNavigate)
    println("Navigated to station workspace: " + currentUrlAfterNavigate)

    // ==================== Step 3: Verify Workspace URL format ====================
    WebUI.comment("=== Step 3: Verify workspace URL format ===")
    println("=== Step 3: Verify workspace URL format ===")

    boolean isValidWsUrl = currentUrlAfterNavigate.matches("https://app.unio.chat/ws/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")
    if (!isValidWsUrl) {
        WebUI.comment("URL is not a valid WS URL: " + currentUrlAfterNavigate)
        println("URL is not a valid WS URL: " + currentUrlAfterNavigate)
    }
    WebUI.verifyEqual(isValidWsUrl, true)
    WebUI.comment("Workspace URL format is valid")
    println("Workspace URL format is valid")
    
    // Extract WS ID for later use
    String wsId = currentUrlAfterNavigate.substring(currentUrlAfterNavigate.lastIndexOf('/') + 1)
    WebUI.comment("WS ID: " + wsId)
    println("WS ID: " + wsId)

    // ==================== Step 4: Check notification bell badge on WS ====================
    WebUI.comment("=== Step 4: Check notification badge on Activities sidebar ===")
    println("=== Step 4: Check notification badge on Activities sidebar ===")

    // Find the bell icon badge on the sidebar (Activities has badge with number)
    String bellBadgeXPath = "//a[contains(@href, '/activities')]//span[contains(@class, 'bg-destructive')]"
    TestObject bellBadge = new TestObject().addProperty('xpath', ConditionType.EQUALS, bellBadgeXPath)
    
    boolean hasBadge = WebUI.verifyElementPresent(
        bellBadge,
        5,
        FailureHandling.OPTIONAL
    )
    
    if (hasBadge) {
        String badgeText = WebUI.getText(bellBadge)
        WebUI.comment("Notification badge found on Activities with count: " + badgeText)
        println("Notification badge found on Activities with count: " + badgeText)
    } else {
        WebUI.comment("No notification badge on Activities")
        println("No notification badge on Activities")
    }
    if (!hasBadge) {
        WebUI.comment("Notification badge should be present on Activities sidebar")
        println("Notification badge should be present on Activities sidebar")
    }
    WebUI.verifyEqual(hasBadge, true)
    WebUI.comment("Notification badge is present on Activities sidebar")
    println("Notification badge is present on Activities sidebar")

    // ==================== Step 5: Click on Activities link ====================
    WebUI.comment("=== Step 5: Click on Activities link ===")
    println("=== Step 5: Click on Activities link ===")

    try {
        WebUI.click(findTestObject('Object Repository/WS/sidebar/a_Activities'))
        WebUI.comment("Clicked Activities using Object Repository")
        println("Clicked Activities using Object Repository")
    } catch (Exception e) {
        WebUI.comment("Object Repository failed, trying dynamic XPath...")
        println("Object Repository failed, trying dynamic XPath...")
        
        String activitiesXPath = "//a[contains(@href, '/activities')]"
        TestObject activitiesLink = new TestObject().addProperty('xpath', ConditionType.EQUALS, activitiesXPath)
        
        WebUI.waitForElementClickable(activitiesLink, 10)
        WebUI.click(activitiesLink)
        WebUI.comment("Clicked Activities using dynamic XPath")
        println("Clicked Activities using dynamic XPath")
    }

    WebUI.waitForPageLoad(15)
    WebUI.delay(2)

    // ==================== Step 6: Verify redirect to Activities page ====================
    WebUI.comment("=== Step 6: Verify redirect to Activities page ===")
    println("=== Step 6: Verify redirect to Activities page ===")

    String activitiesUrl = WebUI.getUrl()
    WebUI.comment("Activities URL: " + activitiesUrl)
    println("Activities URL: " + activitiesUrl)

    String expectedActivitiesUrl = "https://app.unio.chat/ws/" + wsId + "/activities"
    WebUI.comment("Expected Activities URL: " + expectedActivitiesUrl)
    println("Expected Activities URL: " + expectedActivitiesUrl)
    
    boolean isValidActivitiesUrl = activitiesUrl.equals(expectedActivitiesUrl)
    if (!isValidActivitiesUrl) {
        WebUI.comment("URL does not match expected Activities URL")
        println("URL does not match expected Activities URL")
    }
    WebUI.verifyEqual(isValidActivitiesUrl, true)
    WebUI.comment("Successfully navigated to Activities page")
    println("Successfully navigated to Activities page")

    // ==================== Step 7: Verify notification list has 2 notifications ====================
    WebUI.comment("=== Step 7: Verify notification list has at least 2 notifications ===")
    println("=== Step 7: Verify notification list has at least 2 notifications ===")

    // Find all notification items in the activities list
    String notificationItemXPath = "//div[contains(@class, 'rounded-lg') and contains(@class, 'border')]"
    List<WebElement> notificationItems = WebUI.findWebElements(
        new TestObject().addProperty('xpath', ConditionType.EQUALS, notificationItemXPath),
        10
    )
    
    WebUI.comment("Found " + notificationItems.size() + " notifications in Activities list")
    println("Found " + notificationItems.size() + " notifications in Activities list")

    // Verify there are at least 2 notifications (mention and react)
    if (notificationItems.size() < 2) {
        WebUI.comment("Should have at least 2 notifications")
        println("Should have at least 2 notifications")
    }
    WebUI.verifyEqual(notificationItems.size() >= 2, true)
    WebUI.comment("Verified at least 2 notifications exist")
    println("Verified at least 2 notifications exist")

    // ==================== Step 8: Verify mention notification exists ====================
    WebUI.comment("=== Step 8: Verify mention notification exists ===")
    println("=== Step 8: Verify mention notification exists ===")

    boolean hasMentionNotification = false
    boolean hasReactNotification = false
    
    for (WebElement item : notificationItems) {
        String content = item.getText()
        WebUI.comment("Notification content: " + content)
        println("Notification content: " + content)
        
        if (content.contains("mentioned you:") || content.contains("mentioned you")) {
            hasMentionNotification = true
            WebUI.comment("Found mention notification")
            println("Found mention notification")
        }
        
        if (content.contains("reacted") && content.contains("to your message:")) {
            hasReactNotification = true
            WebUI.comment("Found reaction notification")
            println("Found reaction notification")
        }
    }

    if (!hasMentionNotification) {
        WebUI.comment("Should have a mention notification")
        println("Should have a mention notification")
    }
    WebUI.verifyEqual(hasMentionNotification, true)
    WebUI.comment("Mention notification found")
    println("Mention notification found")
    
    if (!hasReactNotification) {
        WebUI.comment("Should have a reaction notification")
        println("Should have a reaction notification")
    }
    WebUI.verifyEqual(hasReactNotification, true)
    WebUI.comment("Reaction notification found")
    println("Reaction notification found")

    WebUI.comment("=== Test Case TC_WS_Activities_Check_Notifications completed successfully ===")
    println("=== Test Case TC_WS_Activities_Check_Notifications completed successfully ===")

} catch (Exception e) {
    WebUI.comment("Test Case TC_WS_Activities_Check_Notifications failed: " + e.getMessage())
    println("Test Case TC_WS_Activities_Check_Notifications failed: " + e.getMessage())
    KeywordUtil.markFailed("Test Case failed: " + e.getMessage())
    throw e
}