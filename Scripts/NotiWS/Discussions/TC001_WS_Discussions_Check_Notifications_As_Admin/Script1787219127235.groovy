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
import com.kms.katalon.core.webui.driver.DriverFactory

try {
    // ==================== Step 1: Login as ADMIN (lieucao16122003) ====================
    WebUI.comment("=== Step 1: Login as admin ===")
    println("=== Step 1: Login as admin ===")

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
        WebUI.comment("Login failed for admin. Current URL: " + currentUrl)
        println("Login failed for admin. Current URL: " + currentUrl)
    }
    WebUI.verifyEqual(isLoggedIn, true)
    WebUI.comment("Admin login successful. Current URL: " + currentUrl)
    println("Admin login successful. Current URL: " + currentUrl)

    // ==================== Step 2: Navigate to the created Station ====================
    WebUI.comment("=== Step 2: Navigate to the created Station ===")
    println("=== Step 2: Navigate to the created Station ===")

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

    // ==================== Step 3: Verify Workspace URL format and extract WS ID ====================
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

    // ==================== Step 4: Check Discussions badge on sidebar ====================
    WebUI.comment("=== Step 4: Check Discussions badge on sidebar ===")
    println("=== Step 4: Check Discussions badge on sidebar ===")

    // Find the Discussions badge on the sidebar
    String discussionsBadgeXPath = "//a[contains(@href, '/discussions')]//span[contains(@class, 'bg-destructive')]"
    TestObject discussionsBadge = new TestObject().addProperty('xpath', ConditionType.EQUALS, discussionsBadgeXPath)
    
    boolean hasBadge = WebUI.verifyElementPresent(
        discussionsBadge,
        5,
        FailureHandling.OPTIONAL
    )
    
    if (hasBadge) {
        String badgeText = WebUI.getText(discussionsBadge)
        WebUI.comment("Discussions badge found with count: " + badgeText)
        println("Discussions badge found with count: " + badgeText)
    } else {
        WebUI.comment("No Discussions badge")
        println("No Discussions badge")
    }
    if (!hasBadge) {
        WebUI.comment("Discussions badge should be present on sidebar")
        println("Discussions badge should be present on sidebar")
    }
    WebUI.verifyEqual(hasBadge, true)
    WebUI.comment("Discussions badge is present on sidebar")
    println("Discussions badge is present on sidebar")

    // ==================== Step 5: Click on Discussions link ====================
    WebUI.comment("=== Step 5: Click on Discussions link ===")
    println("=== Step 5: Click on Discussions link ===")

    try {
        WebUI.click(findTestObject('Object Repository/WS/sidebar/a_Discussions'))
        WebUI.comment("Clicked Discussions using Object Repository")
        println("Clicked Discussions using Object Repository")
    } catch (Exception e) {
        WebUI.comment("Object Repository failed, trying dynamic XPath...")
        println("Object Repository failed, trying dynamic XPath...")
        
        String discussionsXPath = "//a[contains(@href, '/discussions')]"
        TestObject discussionsLink = new TestObject().addProperty('xpath', ConditionType.EQUALS, discussionsXPath)
        
        WebUI.waitForElementClickable(discussionsLink, 10)
        WebUI.click(discussionsLink)
        WebUI.comment("Clicked Discussions using dynamic XPath")
        println("Clicked Discussions using dynamic XPath")
    }

    WebUI.waitForPageLoad(15)
    WebUI.delay(2)

    // ==================== Step 6: Verify redirect to Discussions page ====================
    WebUI.comment("=== Step 6: Verify redirect to Discussions page ===")
    println("=== Step 6: Verify redirect to Discussions page ===")

    String discussionsUrl = WebUI.getUrl()
    WebUI.comment("Discussions URL: " + discussionsUrl)
    println("Discussions URL: " + discussionsUrl)

    String expectedDiscussionsUrl = "https://app.unio.chat/ws/" + wsId + "/discussions"
    WebUI.comment("Expected Discussions URL: " + expectedDiscussionsUrl)
    println("Expected Discussions URL: " + expectedDiscussionsUrl)
    
    boolean isValidDiscussionsUrl = discussionsUrl.equals(expectedDiscussionsUrl)
    if (!isValidDiscussionsUrl) {
        WebUI.comment("URL does not match expected Discussions URL")
        println("URL does not match expected Discussions URL")
    }
    WebUI.verifyEqual(isValidDiscussionsUrl, true)
    WebUI.comment("Successfully navigated to Discussions page")
    println("Successfully navigated to Discussions page")

    // ==================== Step 7: Verify there is discussion notification ====================
    WebUI.comment("=== Step 7: Verify there is discussion notification ===")
    println("=== Step 7: Verify there is discussion notification ===")

    // Find discussion items in the list
    String discussionItemXPath = "//div[contains(@class, 'group') and contains(@class, 'relative')][.//div[contains(@class, 'tiptap')]]"
    List<WebElement> discussionItems = WebUI.findWebElements(
        new TestObject().addProperty('xpath', ConditionType.EQUALS, discussionItemXPath),
        10
    )
    
    WebUI.comment("Found " + discussionItems.size() + " discussion items")
    println("Found " + discussionItems.size() + " discussion items")

    // Verify there is at least one discussion notification
    if (discussionItems.size() < 1) {
        WebUI.comment("Should have at least 1 discussion notification")
        println("Should have at least 1 discussion notification")
    }
    WebUI.verifyEqual(discussionItems.size() >= 1, true)
    WebUI.comment("Verified at least 1 discussion notification exists")
    println("Verified at least 1 discussion notification exists")

    // ==================== Step 8: Verify discussion content exists ====================
    WebUI.comment("=== Step 8: Verify discussion content exists ===")
    println("=== Step 8: Verify discussion content exists ===")

    boolean hasDiscussionMessage = false
    String discussionMessage = "This is a discussion reply from automation test!"
    
    for (WebElement item : discussionItems) {
        String content = item.getText()
        WebUI.comment("Discussion content: " + content)
        println("Discussion content: " + content)
        
        // Check for the discussion message or its Vietnamese version
        if (content.contains(discussionMessage) || 
            content.contains("This is a discussion reply") || 
            content.contains("phản hồi thảo luận từ kiểm thử tự động")) {
            hasDiscussionMessage = true
            WebUI.comment("Found discussion message")
            println("Found discussion message")
            break
        }
    }

    if (!hasDiscussionMessage) {
        WebUI.comment("Discussion message should appear in the list")
        println("Discussion message should appear in the list")
    }
    WebUI.verifyEqual(hasDiscussionMessage, true)
    WebUI.comment("Discussion message found successfully")
    println("Discussion message found successfully")

    WebUI.comment("=== Test Case TC_WS_Discussions_Check_Notifications_As_Admin completed successfully ===")
    println("=== Test Case TC_WS_Discussions_Check_Notifications_As_Admin completed successfully ===")

} catch (Exception e) {
    WebUI.comment("Test Case TC_WS_Discussions_Check_Notifications_As_Admin failed: " + e.getMessage())
    println("Test Case TC_WS_Discussions_Check_Notifications_As_Admin failed: " + e.getMessage())
    KeywordUtil.markFailed("Test Case failed: " + e.getMessage())
    throw e
}