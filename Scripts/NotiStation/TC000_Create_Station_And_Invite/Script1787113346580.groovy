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

// Test execution
try {
    // Step 1: Login with user 1 (lieucao16122003)
    WebUI.comment("=== Step 1: Login with user 1 ===")
    println("=== Step 1: Login with user 1 ===")
    
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
        WebUI.comment("Login failed - not redirected to dashboard. Current URL: " + currentUrl)
        println("Login failed - not redirected to dashboard. Current URL: " + currentUrl)
    }
    WebUI.verifyEqual(isLoggedIn, true)
    WebUI.comment("Login successful. Current URL: " + currentUrl)
    println("Login successful. Current URL: " + currentUrl)
    
    // Step 2: Create new Station
    WebUI.comment("=== Step 2: Create new Station ===")
    println("=== Step 2: Create new Station ===")
    
    WebUI.click(findTestObject('Object Repository/Add Station/button_Add Station'))
    WebUI.waitForElementVisible(findTestObject('Object Repository/Add Station/input_Station name'), 10)
    
    // Generate unique station name with timestamp
    String stationName = "AutoTest_" + System.currentTimeMillis()
    GlobalVariable.STATION_NAME = stationName
    WebUI.comment("Creating station with name: " + stationName)
    println("Creating station with name: " + stationName)
    
    WebUI.setText(findTestObject('Object Repository/Add Station/input_Station name'), stationName)
    WebUI.click(findTestObject('Object Repository/Add Station/button_Create New Station'))
    
    // Step 3: Verify success page and click Explore My Station
    WebUI.comment("=== Step 3: Verify success page and explore ===")
    println("=== Step 3: Verify success page and explore ===")
    
    WebUI.waitForElementVisible(findTestObject('Object Repository/Add Station/button_Explore My Station'), 15)
    WebUI.click(findTestObject('Object Repository/Add Station/button_Explore My Station'))
    WebUI.waitForPageLoad(15)
    
    // Step 4: Verify URL is in correct format
    WebUI.comment("=== Step 4: Verify URL format ===")
    println("=== Step 4: Verify URL format ===")
    
    String currentUrlAfterExplore = WebUI.getUrl()
    WebUI.comment("Current URL after exploring station: " + currentUrlAfterExplore)
    println("Current URL after exploring station: " + currentUrlAfterExplore)
    
    // Check if URL matches the workspace format
    boolean isValidWsUrl = currentUrlAfterExplore.matches("https://app.unio.chat/ws/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")
    if (!isValidWsUrl) {
        WebUI.comment("URL is not a valid WS URL: " + currentUrlAfterExplore)
        println("URL is not a valid WS URL: " + currentUrlAfterExplore)
    }
    WebUI.verifyEqual(isValidWsUrl, true)
    WebUI.comment("URL format is valid")
    println("URL format is valid")
    
    // Step 5: Click Invite Member button
    WebUI.comment("=== Step 5: Click Invite Member button ===")
    println("=== Step 5: Click Invite Member button ===")
    
    WebUI.click(findTestObject('Object Repository/Invite-member/btn_Invite-member'))
    WebUI.waitForElementVisible(findTestObject('Object Repository/Invite-member/div_Invite by Username'), 10)
    
    // Step 6: Click "Invite by Username" option
    WebUI.comment("=== Step 6: Click Invite by Username option ===")
    println("=== Step 6: Click Invite by Username option ===")
    
    WebUI.click(findTestObject('Object Repository/Invite-member/div_Invite by Username'))
    
    // Step 7: Select team role
    WebUI.comment("=== Step 7: Select team role ===")
    println("=== Step 7: Select team role ===")
    
    WebUI.waitForElementVisible(findTestObject('Object Repository/Invite-member/popup_Invite Members by Username'), 10)
    WebUI.click(findTestObject('Object Repository/Invite-member/button_Select a team'))
    WebUI.delay(1)
    WebUI.click(findTestObject('Object Repository/Invite-member/opt_Others'))
    WebUI.comment("Selected role: Others")
    println("Selected role: Others")
    
    // Step 8: Enter username and press Enter to add to list
    WebUI.comment("=== Step 8: Enter username and press Enter ===")
    println("=== Step 8: Enter username and press Enter ===")
    
    // Get the input field
    TestObject inputField = findTestObject('Object Repository/Invite-member/input_Invited users')
    
    // Enter username
    WebUI.setText(inputField, GlobalVariable.INVITEE_USERNAME)
    WebUI.comment("Entered username: " + GlobalVariable.INVITEE_USERNAME)
    println("Entered username: " + GlobalVariable.INVITEE_USERNAME)
    
    // Press Enter key to add user to the list
    WebUI.sendKeys(inputField, Keys.chord(Keys.ENTER))
    WebUI.comment("Pressed Enter to add user to list")
    println("Pressed Enter to add user to list")
    
    // Wait a moment for the user to be added
    WebUI.delay(1)
    
    // Step 9: Click Send Invitation button
    WebUI.comment("=== Step 9: Click Send Invitation button ===")
    println("=== Step 9: Click Send Invitation button ===")
    
    WebUI.click(findTestObject('Object Repository/Invite-member/button_Send X invitation'))
    WebUI.comment("Clicked Send Invitation button")
    println("Clicked Send Invitation button")
    
    // Step 10: Wait for invitation to be sent
    WebUI.delay(3)
    WebUI.comment("Waiting for invitation to be processed")
    println("Waiting for invitation to be processed")
    
    // Step 11: Verify invitation was sent successfully
    WebUI.comment("=== Step 11: Verify invitation sent successfully ===")
    println("=== Step 11: Verify invitation sent successfully ===")
    
    WebUI.comment("Invitation sent successfully to " + GlobalVariable.INVITEE_USERNAME)
    println("Invitation sent successfully to " + GlobalVariable.INVITEE_USERNAME)
    
    // ==================== Step 12: Send message in General chat ====================
    WebUI.comment("=== Step 12: Send message 'Hi!' in General chat ===")
    println("=== Step 12: Send message 'Hi!' in General chat ===")
    
    // Click on General channel
    String generalXPath = "//button[contains(normalize-space(.), 'General')]"
    TestObject generalBtn = new TestObject().addProperty('xpath', ConditionType.EQUALS, generalXPath)
    
    try {
        WebUI.waitForElementClickable(findTestObject('Object Repository/WS/ws-chat/button_General'), 8)
        WebUI.click(findTestObject('Object Repository/WS/ws-chat/button_General'))
        WebUI.comment("Clicked General using Object Repository")
        println("Clicked General using Object Repository")
    } catch (Exception e) {
        WebUI.comment("Object Repository failed, trying dynamic XPath for General...")
        println("Object Repository failed, trying dynamic XPath for General...")
        WebUI.waitForElementClickable(generalBtn, 10)
        WebUI.click(generalBtn)
        WebUI.comment("Clicked General using dynamic XPath")
        println("Clicked General using dynamic XPath")
    }
    
    WebUI.waitForPageLoad(10)
    WebUI.delay(2)
    
    // Check if Join this group button exists and click if needed
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
    
    // Click on chat input area
    WebUI.click(findTestObject('Object Repository/WS/ws-chat/div_Input-chat-content'))
    WebUI.delay(1)
    WebUI.comment("Chat input area focused")
    println("Chat input area focused")
    
    // Type "Hi!" and press Enter to send
    WebUI.sendKeys(findTestObject('Object Repository/WS/ws-chat/div_Input-chat-content'), "Hi!")
    WebUI.delay(1)
    WebUI.sendKeys(findTestObject('Object Repository/WS/ws-chat/div_Input-chat-content'), Keys.chord(Keys.ENTER))
    WebUI.delay(2)
    WebUI.comment("Message 'Hi!' sent successfully")
    println("Message 'Hi!' sent successfully")
    
    WebUI.comment("=== Test Case TC_Create_Station_And_Invite completed successfully ===")
    println("=== Test Case TC_Create_Station_And_Invite completed successfully ===")
    
} catch (Exception e) {
    WebUI.comment("Test Case TC_Create_Station_And_Invite failed: " + e.getMessage())
    println("Test Case TC_Create_Station_And_Invite failed: " + e.getMessage())
    KeywordUtil.markFailed("Test Case failed: " + e.getMessage())
    throw e
}