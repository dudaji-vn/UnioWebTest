package customKeywords

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import internal.GlobalVariable

public class NotificationKeywords {
    
    /**
     * Check if notification bell has badge (unread count)
     * @param bellButton - Test object for notification bell button
     * @return true if badge exists
     */
    @Keyword
    def hasNotificationBadge(TestObject bellButton) {
        try {
            TestObject badge = findTestObject('Object Repository/Dashboard/div_Notification_Badge')
            boolean hasBadge = WebUI.verifyElementPresent(badge, 3, FailureHandling.OPTIONAL)
            
            if (hasBadge) {
                String badgeText = WebUI.getText(badge)
                KeywordUtil.logInfo("Notification badge found with count: " + badgeText)
                return true
            } else {
                KeywordUtil.logInfo("No notification badge found")
                return false
            }
        } catch (Exception e) {
            KeywordUtil.markWarning("Could not check notification badge: " + e.getMessage())
            return false
        }
    }
    
    /**
     * Open notification popover and wait for it to load
     * @param bellButton - Test object for notification bell button
     * @return true if popover opened successfully
     */
    @Keyword
    def openNotificationPopover(TestObject bellButton) {
        try {
            WebUI.click(bellButton)
            WebUI.waitForElementVisible(
                findTestObject('Object Repository/Dashboard/div_Notification_Popover'),
                10,
                FailureHandling.OPTIONAL
            )
            KeywordUtil.logInfo("Notification popover opened")
            return true
        } catch (Exception e) {
            KeywordUtil.markError("Failed to open notification popover: " + e.getMessage())
            return false
        }
    }
    
    /**
     * Verify notification content contains expected text
     * @param expectedText - text to search for
     * @return true if found
     */
    @Keyword
    def verifyNotificationContent(String expectedText) {
        try {
            TestObject content = findTestObject('Object Repository/Dashboard/div_Notification_Content')
            WebUI.waitForElementVisible(content, 5)
            String actualText = WebUI.getText(content)
            
            boolean contains = actualText.contains(expectedText)
            if (contains) {
                KeywordUtil.logInfo("Found expected text in notification: " + expectedText)
            } else {
                KeywordUtil.markWarning("Expected text not found. Actual: " + actualText)
            }
            return contains
        } catch (Exception e) {
            KeywordUtil.markError("Failed to verify notification content: " + e.getMessage())
            return false
        }
    }
    
    /**
     * Accept invitation from notification
     * @return true if accepted successfully
     */
    @Keyword
    def acceptInvitation() {
        try {
            TestObject acceptButton = findTestObject('Object Repository/Dashboard/button_Accept_Invitation')
            WebUI.waitForElementVisible(acceptButton, 10)
            WebUI.click(acceptButton)
            WebUI.waitForPageLoad(15)
            KeywordUtil.logInfo("Invitation accepted successfully")
            return true
        } catch (Exception e) {
            KeywordUtil.markError("Failed to accept invitation: " + e.getMessage())
            return false
        }
    }
    
    /**
     * Reject invitation from notification
     * @return true if rejected successfully
     */
    @Keyword
    def rejectInvitation() {
        try {
            TestObject rejectButton = findTestObject('Object Repository/Dashboard/button_Reject_Invitation')
            WebUI.waitForElementVisible(rejectButton, 10)
            WebUI.click(rejectButton)
            WebUI.waitForPageLoad(5)
            KeywordUtil.logInfo("Invitation rejected successfully")
            return true
        } catch (Exception e) {
            KeywordUtil.markError("Failed to reject invitation: " + e.getMessage())
            return false
        }
    }
    
    /**
     * Get count of unread notifications from badge
     * @return count as integer, or -1 if not found
     */
    @Keyword
    def getUnreadCount() {
        try {
            TestObject badge = findTestObject('Object Repository/Dashboard/div_Notification_Badge')
            if (WebUI.verifyElementPresent(badge, 3, FailureHandling.OPTIONAL)) {
                String text = WebUI.getText(badge)
                return Integer.parseInt(text.trim())
            }
            return 0
        } catch (Exception e) {
            KeywordUtil.markWarning("Could not get unread count: " + e.getMessage())
            return -1
        }
    }
    
    /**
     * Verify unread notification has dot indicator
     * @return true if unread dot exists
     */
    @Keyword
    def hasUnreadDot() {
        try {
            TestObject unreadDot = findTestObject('Object Repository/Dashboard/div_Unread_Dot')
            boolean hasDot = WebUI.verifyElementPresent(unreadDot, 3, FailureHandling.OPTIONAL)
            KeywordUtil.logInfo("Unread dot " + (hasDot ? "present" : "not present"))
            return hasDot
        } catch (Exception e) {
            KeywordUtil.markWarning("Could not check unread dot: " + e.getMessage())
            return false
        }
    }
}