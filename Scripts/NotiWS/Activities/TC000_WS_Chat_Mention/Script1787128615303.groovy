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

    // Click on the station card from dashboard
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

    // More flexible XPath (ignore extra spaces)
    String generalXPath = "//button[contains(normalize-space(.), 'General')]"
    TestObject generalBtn = new TestObject().addProperty('xpath', ConditionType.EQUALS, generalXPath)

    // Fallback: try the Object Repository first, then dynamic
    boolean clickedGeneral = false
    try {
        WebUI.waitForElementClickable(findTestObject('Object Repository/WS/ws-chat/button_General'), 8)
        WebUI.click(findTestObject('Object Repository/WS/ws-chat/button_General'))
        clickedGeneral = true
        WebUI.comment("Clicked General using Object Repository")
        println("Clicked General using Object Repository")
    } catch (Exception e) {
        WebUI.comment("Object Repository failed, trying dynamic XPath...")
        println("Object Repository failed, trying dynamic XPath...")
        WebUI.waitForElementClickable(generalBtn, 10)
        WebUI.click(generalBtn)
        clickedGeneral = true
        WebUI.comment("Clicked General using dynamic XPath")
        println("Clicked General using dynamic XPath")
    }

    WebUI.waitForPageLoad(15)
    WebUI.delay(2)

    String currentUrlAfterChat = WebUI.getUrl()
    WebUI.comment("Current URL after clicking General: " + currentUrlAfterChat)
    println("Current URL after clicking General: " + currentUrlAfterChat)

    // Verify chat URL format (.../ws/{WS_ID}/{chat_ID})
    boolean isValidChatUrl = currentUrlAfterChat.matches("https://app.unio.chat/ws/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")
    if (!isValidChatUrl) {
        WebUI.comment("URL is not a valid chat URL: " + currentUrlAfterChat)
        println("URL is not a valid chat URL: " + currentUrlAfterChat)
    }
    WebUI.verifyEqual(isValidChatUrl, true)
    WebUI.comment("Chat URL format is valid")
    println("Chat URL format is valid")

    // ==================== Step 5: Check and click Join this group if needed ====================
    WebUI.comment("=== Step 5: Check if Join this group button exists ===")
    println("=== Step 5: Check if Join this group button exists ===")

    // Check if Join button exists
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
        WebUI.comment("Clicked Join this group button")
        println("Clicked Join this group button")
        
        // Wait for the page to update after joining
        WebUI.waitForPageLoad(10)
        WebUI.delay(1)
    } else {
        WebUI.comment("Join this group button not found. Already joined the group.")
        println("Join this group button not found. Already joined the group.")
    }

    // ==================== Step 6: Click on chat input area ====================
    WebUI.comment("=== Step 6: Click on chat input area ===")
    println("=== Step 6: Click on chat input area ===")

    // Get the input field
    TestObject inputField = findTestObject('Object Repository/WS/ws-chat/div_Input-chat-content')
    WebUI.click(inputField)
    WebUI.delay(1)
    WebUI.comment("Chat input area focused")
    println("Chat input area focused")

    // ==================== Step 7: Click Mention button ====================
    WebUI.comment("=== Step 7: Click Mention button ===")
    println("=== Step 7: Click Mention button ===")

    WebUI.click(findTestObject('Object Repository/WS/ws-chat/button_Mention'))
    WebUI.delay(1)
    WebUI.comment("Mention button clicked")
    println("Mention button clicked")

    // ==================== Step 8: Verify mention suggestions appear ====================
    WebUI.comment("=== Step 8: Verify mention suggestions appear ===")
    println("=== Step 8: Verify mention suggestions appear ===")

    WebUI.waitForElementVisible(findTestObject('Object Repository/WS/ws-chat/mention-Suggestions'), 10)
    WebUI.comment("Mention suggestions appeared")
    println("Mention suggestions appeared")

    // ==================== Step 9: Select invitee username ====================
    WebUI.comment("=== Step 9: Select invitee username from suggestions ===")
    println("=== Step 9: Select invitee username from suggestions ===")

    String inviterUsername = GlobalVariable.Name

    // Based on the HTML you provided
    String mentionItemXPath = "//div[@data-slot='command-item']//span[contains(@class, 'truncate') and normalize-space(text())='" + inviterUsername + "']"
    TestObject mentionItem = new TestObject().addProperty('xpath', ConditionType.EQUALS, mentionItemXPath)

    WebUI.waitForElementVisible(mentionItem, 10)
    WebUI.click(mentionItem)
    WebUI.comment("Selected username: " + inviterUsername)
    println("Selected username: " + inviterUsername)

    WebUI.delay(1)

    // ==================== Step 10: Send the message using ENTER key ====================
    WebUI.comment("=== Step 10: Send the message using ENTER key ===")
    println("=== Step 10: Send the message using ENTER key ===")

    // Send ENTER key to the input field to send message
    WebUI.sendKeys(inputField, Keys.chord(Keys.ENTER))
    WebUI.delay(2)
    WebUI.comment("Message sent successfully using ENTER key")
    println("Message sent successfully using ENTER key")

    WebUI.comment("=== Test Case TC000_WS_Chat_Mention completed successfully ===")
    println("=== Test Case TC000_WS_Chat_Mention completed successfully ===")

} catch (Exception e) {
    WebUI.comment("Test Case TC000_WS_Chat_Mention failed: " + e.getMessage())
    println("Test Case TC000_WS_Chat_Mention failed: " + e.getMessage())
    KeywordUtil.markFailed("Test Case failed: " + e.getMessage())
    throw e
}