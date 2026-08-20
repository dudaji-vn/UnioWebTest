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
    // ==================== Step 1: Login as INVITEE ====================
    WebUI.comment("=== Step 1: Login as invitee ===")
    println("=== Step 1: Login as invitee ===")

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
    
    // ==================== Step 1.5: Handle Enable notifications popup if appears ====================
    WebUI.comment("=== Step 1.5: Check and handle Enable notifications popup ===")
    println("=== Step 1.5: Check and handle Enable notifications popup ===")
    
    boolean enableNotiPopupExists = WebUI.verifyElementPresent(
        findTestObject('Object Repository/Enable notifications/div_Enable notifications'),
        5,
        FailureHandling.OPTIONAL
    )
    
    if (enableNotiPopupExists) {
        WebUI.comment("Enable notifications popup found. Clicking Enable button...")
        println("Enable notifications popup found. Clicking Enable button...")
        
        WebUI.click(findTestObject('Object Repository/Enable notifications/button_Enable'))
        WebUI.delay(1)
        WebUI.comment("Clicked Enable button")
        println("Clicked Enable button")
    } else {
        WebUI.comment("Enable notifications popup not found. Continuing...")
        println("Enable notifications popup not found. Continuing...")
    }

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

    // ==================== Step 4: Click General channel ====================
    WebUI.comment("=== Step 4: Click on General channel ===")
    println("=== Step 4: Click on General channel ===")

    String generalXPath = "//button[contains(normalize-space(.), 'General')]"
    TestObject generalBtn = new TestObject().addProperty('xpath', ConditionType.EQUALS, generalXPath)

    try {
        WebUI.waitForElementClickable(findTestObject('Object Repository/WS/ws-chat/button_General'), 8)
        WebUI.click(findTestObject('Object Repository/WS/ws-chat/button_General'))
        WebUI.comment("Clicked General using Object Repository")
        println("Clicked General using Object Repository")
    } catch (Exception e) {
        WebUI.comment("Object Repository failed, trying dynamic XPath...")
        println("Object Repository failed, trying dynamic XPath...")
        WebUI.waitForElementClickable(generalBtn, 10)
        WebUI.click(generalBtn)
        WebUI.comment("Clicked General using dynamic XPath")
        println("Clicked General using dynamic XPath")
    }

    WebUI.waitForPageLoad(15)
    WebUI.delay(2)

    // ==================== Step 5: Check and click Join this group if needed ====================
    WebUI.comment("=== Step 5: Check if Join this group button exists ===")
    println("=== Step 5: Check if Join this group button exists ===")

    boolean joinButtonExists = WebUI.verifyElementPresent(
        findTestObject('Object Repository/WS/ws-chat/button_Join this group'),
        5,
        FailureHandling.OPTIONAL
    )

    if (joinButtonExists) {
        WebUI.comment("Join this group button found. Clicking to join the group...")
        println("Join this group button found. Clicking to join the group...")
        
        WebUI.click(findTestObject('Object Repository/WS/ws-chat/button_Join this group'))
        WebUI.delay(2)
        WebUI.waitForPageLoad(10)
        WebUI.delay(1)
    } else {
        WebUI.comment("Already joined the group.")
        println("Already joined the group.")
    }

    // ==================== Step 6: Find the target message (Hi! from admin) ====================
    WebUI.comment("=== Step 6: Find the target message 'Hi!' from admin ===")
    println("=== Step 6: Find the target message 'Hi!' from admin ===")

    String messageXPath = "//div[contains(@class, 'tiptap') and contains(@class, 'wrap-break-word')]//p[contains(text(), 'Hi!')]"
    TestObject messageElement = new TestObject().addProperty('xpath', ConditionType.EQUALS, messageXPath)
    
    WebUI.waitForElementVisible(messageElement, 10)
    WebUI.comment("Found the 'Hi!' message")
    println("Found the 'Hi!' message")
    
    WebElement messageWebElement = WebUI.findWebElement(messageElement, 10)
    WebUI.comment("Message element found")
    println("Message element found")

    // ==================== Step 7: RIGHT CLICK on the message to open action menu ====================
    WebUI.comment("=== Step 7: Right click on the message to open action menu ===")
    println("=== Step 7: Right click on the message to open action menu ===")
    
    WebUI.delay(1)
    
    // Use Actions to right-click on the message
    Actions actions = new Actions(DriverFactory.getWebDriver())
    actions.contextClick(messageWebElement).perform()
    WebUI.delay(2)
    WebUI.comment("Right clicked on the message")
    println("Right clicked on the message")
    
    // ==================== Step 8: Verify action menu appears ====================
    WebUI.comment("=== Step 8: Verify action menu appears ===")
    println("=== Step 8: Verify action menu appears ===")
    
    try {
        WebUI.waitForElementVisible(findTestObject('Object Repository/WS/ws-chat/Action-menu/action-menu'), 5)
        WebUI.comment("Action menu appeared using Object Repository")
        println("Action menu appeared using Object Repository")
    } catch (Exception e) {
        WebUI.comment("Object Repository failed, trying dynamic XPath for action menu...")
        println("Object Repository failed, trying dynamic XPath for action menu...")
        
        String actionMenuXPath = "//div[contains(@class, 'dropdown-menu') or contains(@class, 'context-menu')]"
        TestObject actionMenu = new TestObject().addProperty('xpath', ConditionType.EQUALS, actionMenuXPath)
        WebUI.waitForElementVisible(actionMenu, 5)
        WebUI.comment("Action menu appeared using dynamic XPath")
        println("Action menu appeared using dynamic XPath")
    }

    // ==================== Step 9: Click on "Reply in discussion" ====================
    WebUI.comment("=== Step 9: Click on 'Reply in discussion' ===")
    println("=== Step 9: Click on 'Reply in discussion' ===")
    
    try {
        WebUI.click(findTestObject('Object Repository/WS/ws-chat/Action-menu/div_Reply in discussion'))
        WebUI.comment("Clicked 'Reply in discussion' using Object Repository")
        println("Clicked 'Reply in discussion' using Object Repository")
    } catch (Exception e) {
        WebUI.comment("Object Repository failed, trying dynamic XPath...")
        println("Object Repository failed, trying dynamic XPath...")
        
        String replyXPath = "//div[contains(text(), 'Reply in discussion')]"
        TestObject replyOption = new TestObject().addProperty('xpath', ConditionType.EQUALS, replyXPath)
        WebUI.waitForElementClickable(replyOption, 5)
        WebUI.click(replyOption)
        WebUI.comment("Clicked 'Reply in discussion' using dynamic XPath")
        println("Clicked 'Reply in discussion' using dynamic XPath")
    }
    
    WebUI.delay(2)

    // ==================== Step 10: Verify Discussion Panel appears ====================
    WebUI.comment("=== Step 10: Verify Discussion Panel appears ===")
    println("=== Step 10: Verify Discussion Panel appears ===")
    
    try {
        WebUI.waitForElementVisible(findTestObject('Object Repository/WS/ws-chat/Discussion Panel/Discussion Panel'), 10)
        WebUI.comment("Discussion Panel appeared using Object Repository")
        println("Discussion Panel appeared using Object Repository")
    } catch (Exception e) {
        WebUI.comment("Object Repository failed, trying dynamic XPath for Discussion Panel...")
        println("Object Repository failed, trying dynamic XPath for Discussion Panel...")
        
        String discussionPanelXPath = "//div[contains(@class, 'discussion') or contains(@class, 'thread')]"
        TestObject discussionPanel = new TestObject().addProperty('xpath', ConditionType.EQUALS, discussionPanelXPath)
        WebUI.waitForElementVisible(discussionPanel, 10)
        WebUI.comment("Discussion Panel appeared using dynamic XPath")
        println("Discussion Panel appeared using dynamic XPath")
    }

    // ==================== Step 11: Click on Discussion input area ====================
    WebUI.comment("=== Step 11: Click on Discussion input area ===")
    println("=== Step 11: Click on Discussion input area ===")
    
    try {
        WebUI.click(findTestObject('Object Repository/WS/ws-chat/Discussion Panel/input-Discussion-chat'))
        WebUI.comment("Clicked Discussion input using Object Repository")
        println("Clicked Discussion input using Object Repository")
    } catch (Exception e) {
        WebUI.comment("Object Repository failed, trying dynamic XPath for Discussion input...")
        println("Object Repository failed, trying dynamic XPath for Discussion input...")
        
        String discussionInputXPath = "//div[contains(@class, 'discussion') or contains(@class, 'thread')]//div[@contenteditable='true']"
        TestObject discussionInput = new TestObject().addProperty('xpath', ConditionType.EQUALS, discussionInputXPath)
        WebUI.waitForElementVisible(discussionInput, 10)
        WebUI.click(discussionInput)
        WebUI.comment("Clicked Discussion input using dynamic XPath")
        println("Clicked Discussion input using dynamic XPath")
    }
    
    WebUI.delay(1)
    WebUI.comment("Discussion input area focused")
    println("Discussion input area focused")

    // ==================== Step 12: Type message and press Enter ====================
    WebUI.comment("=== Step 12: Type message and press Enter ===")
    println("=== Step 12: Type message and press Enter ===")
    
    String discussionMessage = "This is a discussion reply from automation test!"
    
    try {
        TestObject discussionInput = findTestObject('Object Repository/WS/ws-chat/Discussion Panel/input-Discussion-chat')
        WebUI.sendKeys(discussionInput, discussionMessage)
        WebUI.delay(1)
        WebUI.sendKeys(discussionInput, Keys.chord(Keys.ENTER))
        WebUI.comment("Discussion message sent using Object Repository")
        println("Discussion message sent using Object Repository")
    } catch (Exception e) {
        WebUI.comment("Object Repository failed, trying dynamic XPath for sending message...")
        println("Object Repository failed, trying dynamic XPath for sending message...")
        
        String discussionInputXPath = "//div[contains(@class, 'discussion') or contains(@class, 'thread')]//div[@contenteditable='true']"
        TestObject discussionInput = new TestObject().addProperty('xpath', ConditionType.EQUALS, discussionInputXPath)
        WebUI.sendKeys(discussionInput, discussionMessage)
        WebUI.delay(1)
        WebUI.sendKeys(discussionInput, Keys.chord(Keys.ENTER))
        WebUI.comment("Discussion message sent using dynamic XPath")
        println("Discussion message sent using dynamic XPath")
    }
    
    WebUI.delay(2)
    WebUI.comment("Discussion message sent successfully")
    println("Discussion message sent successfully")

    WebUI.comment("=== Test Case TC_WS_Discussion_Chat completed successfully ===")
    println("=== Test Case TC_WS_Discussion_Chat completed successfully ===")

} catch (Exception e) {
    WebUI.comment("Test Case TC_WS_Discussion_Chat failed: " + e.getMessage())
    println("Test Case TC_WS_Discussion_Chat failed: " + e.getMessage())
    KeywordUtil.markFailed("Test Case failed: " + e.getMessage())
    throw e
}