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
import org.openqa.selenium.By

try {
    String stationName = GlobalVariable.STATION_NAME

    // ==================== Step 1: Ensure popover is open ====================
    WebUI.comment("=== Step 1: Ensure notification popover is open ===")
    println("=== Step 1: Ensure notification popover is open ===")

    boolean isPopoverOpen = WebUI.verifyElementPresent(
        findTestObject('Object Repository/Dashboard/div_Noti-Station_Popover'),
        3,
        FailureHandling.OPTIONAL
    )

    if (!isPopoverOpen) {
        WebUI.click(findTestObject('Object Repository/Dashboard/button_Noti-Station_Dashboard'))
        WebUI.waitForElementVisible(findTestObject('Object Repository/Dashboard/div_Noti-Station_Popover'), 10)
        WebUI.comment("Notification popover opened")
        println("Notification popover opened")
    } else {
        WebUI.comment("Notification popover is already open")
        println("Notification popover is already open")
    }

    WebUI.delay(1)

    // ==================== Step 2: Find a notification that has unread dot ====================
    WebUI.comment("=== Step 2: Find a notification that has unread dot ===")
    println("=== Step 2: Find a notification that has unread dot ===")

    // Prefer: "left Station" notification of the current station that still has unread dot
    String targetNotiXPath = "//div[@data-slot='popover-content']//div[contains(@class, 'p-4')]" +
                             "[contains(., 'left Station') and contains(., '" + stationName + "')]" +
                             "[.//div[contains(@class, 'mt-1.5') and contains(@class, 'h-2') and contains(@class, 'w-2') and contains(@class, 'rounded-full') and contains(@class, 'bg-primary')]]"

    TestObject targetNoti = new TestObject().addProperty('xpath', ConditionType.EQUALS, targetNotiXPath)
    boolean foundTarget = WebUI.verifyElementPresent(targetNoti, 5, FailureHandling.OPTIONAL)

    if (!foundTarget) {
        WebUI.comment("Could not find 'left Station' of current station → falling back to any notification that has unread dot")
        println("Could not find 'left Station' of current station → falling back to any notification that has unread dot")

        targetNotiXPath = "//div[@data-slot='popover-content']//div[contains(@class, 'p-4')]" +
                          "[.//div[contains(@class, 'mt-1.5') and contains(@class, 'h-2') and contains(@class, 'w-2') and contains(@class, 'rounded-full') and contains(@class, 'bg-primary')]]"
        targetNoti = new TestObject().addProperty('xpath', ConditionType.EQUALS, targetNotiXPath)
        WebUI.waitForElementVisible(targetNoti, 10)
    } else {
        WebUI.comment("Found target: left Station of " + stationName)
        println("Found target: left Station of " + stationName)
    }

    // Get text content of the target item for later verification
    WebElement targetElement = WebUI.findWebElement(targetNoti, 10)
    String targetText = targetElement.getText().trim()
    WebUI.comment("Target notification text: " + targetText)
    println("Target notification text: " + targetText)

    // XPath of the unread dot of this item
    String unreadDotXPath = targetNotiXPath + "//div[contains(@class, 'mt-1.5') and contains(@class, 'h-2') and contains(@class, 'w-2') and contains(@class, 'rounded-full') and contains(@class, 'bg-primary')]"

    boolean hasDotBefore = WebUI.verifyElementPresent(
        new TestObject().addProperty('xpath', ConditionType.EQUALS, unreadDotXPath),
        5,
        FailureHandling.OPTIONAL
    )
    WebUI.comment("Has unread dot BEFORE click: " + hasDotBefore)
    println("Has unread dot BEFORE click: " + hasDotBefore)
    WebUI.verifyEqual(hasDotBefore, true)

    // ==================== Step 3: Click on the notification item ====================
    WebUI.comment("=== Step 3: Click on the notification item ===")
    println("=== Step 3: Click on the notification item ===")

    String clickableXPath = targetNotiXPath + "//button[contains(@class, 'flex') and contains(@class, 'items-start')]"
    TestObject clickableBtn = new TestObject().addProperty('xpath', ConditionType.EQUALS, clickableXPath)

    WebUI.waitForElementClickable(clickableBtn, 10)
    WebUI.click(clickableBtn)
    WebUI.delay(2)
    WebUI.comment("Clicked on the notification item")
    println("Clicked on the notification item")

    // ==================== Step 4: Verify unread dot disappeared ====================
    WebUI.comment("=== Step 4: Verify unread dot disappeared ===")
    println("=== Step 4: Verify unread dot disappeared ===")

    // Re-open popover if it was closed after clicking
    boolean isPopoverStillOpen = WebUI.verifyElementPresent(
        findTestObject('Object Repository/Dashboard/div_Noti-Station_Popover'),
        3,
        FailureHandling.OPTIONAL
    )

    if (!isPopoverStillOpen) {
        WebUI.comment("Popover closed after click → re-opening...")
        println("Popover closed after click → re-opening...")
        WebUI.click(findTestObject('Object Repository/Dashboard/button_Noti-Station_Dashboard'))
        WebUI.waitForElementVisible(findTestObject('Object Repository/Dashboard/div_Noti-Station_Popover'), 10)
        WebUI.delay(1)
    }

    // Build a more precise search key based on the original target text
    String searchKey = stationName
    if (targetText.contains("left Station")) {
        searchKey = "left Station: " + stationName
    } else if (targetText.contains("has joined Station")) {
        searchKey = "has joined Station: " + stationName
    }

    String afterClickXPath = "//div[@data-slot='popover-content']//div[contains(@class, 'p-4')][contains(., '" + searchKey + "')]" +
                             "[.//div[contains(@class, 'mt-1.5') and contains(@class, 'h-2') and contains(@class, 'w-2') and contains(@class, 'rounded-full') and contains(@class, 'bg-primary')]]"

    boolean hasDotAfter = WebUI.verifyElementPresent(
        new TestObject().addProperty('xpath', ConditionType.EQUALS, afterClickXPath),
        3,
        FailureHandling.OPTIONAL
    )

    WebUI.comment("Has unread dot AFTER click: " + hasDotAfter)
    println("Has unread dot AFTER click: " + hasDotAfter)

    WebUI.verifyEqual(hasDotAfter, false)
    WebUI.comment("SUCCESS: Unread dot has disappeared after clicking")
    println("SUCCESS: Unread dot has disappeared after clicking")

    WebUI.comment("=== Test Case TC_Check_Notification_Click_To_Mark_As_Read completed successfully ===")
    println("=== Test Case TC_Check_Notification_Click_To_Mark_As_Read completed successfully ===")

} catch (Exception e) {
    WebUI.comment("Test Case failed: " + e.getMessage())
    println("Test Case failed: " + e.getMessage())
    KeywordUtil.markFailed("Test Case failed: " + e.getMessage())
    throw e
}