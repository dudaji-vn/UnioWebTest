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

    // ==================== Step 6: Find the latest message (Hi! from admin) ====================
    WebUI.comment("=== Step 6: Find the latest message 'Hi!' from admin ===")
    println("=== Step 6: Find the latest message 'Hi!' from admin ===")

    String messageXPath = "//div[contains(@class, 'tiptap') and contains(@class, 'wrap-break-word')]//p[contains(text(), 'Hi!')]"
    TestObject messageElement = new TestObject().addProperty('xpath', ConditionType.EQUALS, messageXPath)
    
    WebUI.waitForElementVisible(messageElement, 10)
    WebUI.comment("Found the 'Hi!' message")
    println("Found the 'Hi!' message")
    
    WebElement messageWebElement = WebUI.findWebElement(messageElement, 10)
    WebUI.comment("Message element found")
    println("Message element found")

    // ==================== Step 7: Hover over the message to show reaction button ====================
    WebUI.comment("=== Step 7: Hover over the message to show reaction button ===")
    println("=== Step 7: Hover over the message to show reaction button ===")
    
    WebUI.delay(1)
    
    Actions actions = new Actions(DriverFactory.getWebDriver())
    actions.moveToElement(messageWebElement).perform()
    WebUI.delay(2)
    WebUI.comment("Hovered over the message")
    println("Hovered over the message")
    
    // ==================== Step 8: Click on Add reaction button (smile face icon) ====================
    WebUI.comment("=== Step 8: Click on Add reaction button (smile face icon) ===")
    println("=== Step 8: Click on Add reaction button (smile face icon) ===")
    
    // Find the smile face button to open reaction popup
	
    WebUI.waitForElementVisible(findTestObject('Object Repository/WS/ws-chat/button_react'), 10)
    WebUI.click(findTestObject('Object Repository/WS/ws-chat/button_react'))
    WebUI.delay(1)
    WebUI.comment("Clicked on Add reaction button")
    println("Clicked on Add reaction button")
    
    // ==================== Step 9: Click on a reaction emoji from the popup ====================
    WebUI.comment("=== Step 9: Click on a reaction emoji from the popup ===")
    println("=== Step 9: Click on a reaction emoji from the popup ===")
    
    // Wait for reaction popup and click on first emoji
//    String emojiReactionXPath = "//div[contains(@class, 'flex') and contains(@class, 'gap-1')]//button[contains(@class, 'rounded')]"
//    TestObject emojiReaction = new TestObject().addProperty('xpath', ConditionType.EQUALS, emojiReactionXPath)
//    
//    WebUI.waitForElementVisible(emojiReaction, 5)
//    WebUI.click(emojiReaction)
	
	WebUI.waitForElementVisible(findTestObject('Object Repository/WS/ws-chat/2nd-react'), 10)
	WebUI.click(findTestObject('Object Repository/WS/ws-chat/2nd-react'))
    WebUI.delay(2)
    WebUI.comment("Clicked on a reaction emoji")
    println("Clicked on a reaction emoji")
    
    // ==================== Step 10: Verify reaction chip appears ====================
    WebUI.comment("=== Step 10: Verify reaction chip appears ===")
    println("=== Step 10: Verify reaction chip appears ===")
    
    // Check for reaction chip (contains the reaction and count)
    String reactionChipXPath = "//button[contains(@class, 'rounded-full') and contains(@class, 'border-primary')]//span[contains(@class, 'text-xs')]"
    TestObject reactionChip = new TestObject().addProperty('xpath', ConditionType.EQUALS, reactionChipXPath)
    
    boolean hasReactionChip = WebUI.verifyElementPresent(
        reactionChip,
        5,
        FailureHandling.OPTIONAL
    )
    
    if (hasReactionChip) {
        String chipText = WebUI.getText(reactionChip)
        WebUI.comment("Reaction chip found with text: " + chipText)
        println("Reaction chip found with text: " + chipText)
    } else {
        WebUI.comment("Reaction chip not found")
        println("Reaction chip not found")
    }
    WebUI.verifyEqual(hasReactionChip, true)
    WebUI.comment("Reaction chip successfully appeared")
    println("Reaction chip successfully appeared")
    
    WebUI.comment("=== Test Case TC_WS_Chat_React_To_Message completed successfully ===")
    println("=== Test Case TC_WS_Chat_React_To_Message completed successfully ===")

} catch (Exception e) {
    WebUI.comment("Test Case TC_WS_Chat_React_To_Message failed: " + e.getMessage())
    println("Test Case TC_WS_Chat_React_To_Message failed: " + e.getMessage())
    KeywordUtil.markFailed("Test Case failed: " + e.getMessage())
    throw e
}