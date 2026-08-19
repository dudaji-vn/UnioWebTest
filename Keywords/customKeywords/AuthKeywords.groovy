package customKeywords

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testcase.TestCaseFactory
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testdata.TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

import org.openqa.selenium.WebElement
import org.openqa.selenium.WebDriver
import org.openqa.selenium.By

import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.webui.driver.DriverFactory

import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObjectProperty

import com.kms.katalon.core.mobile.helper.MobileElementCommonHelper
import com.kms.katalon.core.util.KeywordUtil

import com.kms.katalon.core.webui.exception.WebElementNotFoundException


class AuthKeywords {
    
    /**
     * Refresh browser
     */
    @Keyword
    def refreshBrowser() {
        KeywordUtil.logInfo("Refreshing")
        WebDriver webDriver = DriverFactory.getWebDriver()
        webDriver.navigate().refresh()
        KeywordUtil.markPassed("Refresh successfully")
    }

    /**
     * Click element
     * @param to Katalon test object
     */
    @Keyword
    def clickElement(TestObject to) {
        try {
            WebElement element = WebUI.findWebElement(to)
            KeywordUtil.logInfo("Clicking element")
            element.click()
            KeywordUtil.markPassed("Element has been clicked")
        } catch (WebElementNotFoundException e) {
            KeywordUtil.markFailed("Element not found")
        } catch (Exception e) {
            KeywordUtil.markFailed("Fail to click on element")
        }
    }

    /**
     * Get all rows of HTML table
     * @param table Katalon test object represent for HTML table
     * @param outerTagName outer tag name of TR tag, usually is TBODY
     * @return All rows inside HTML table
     */
    @Keyword
    def List<WebElement> getHtmlTableRows(TestObject table, String outerTagName) {
        WebElement mailList = WebUI.findWebElement(table)
        List<WebElement> selectedRows = mailList.findElements(By.xpath("./" + outerTagName + "/tr"))
        return selectedRows
    }

    // ==================== AUTHENTICATION KEYWORDS ====================

    /**
     * Login to the application with provided credentials
     * @param email - user email
     * @param password - user password
     * @param baseUrl - base URL to navigate to (optional)
     * @return true if login successful, false otherwise
     */
    @Keyword
    def login(String email, String password, String baseUrl = null) {
        try {
            if (baseUrl == null) {
                baseUrl = GlobalVariable.BASE_URL
            }
            
            KeywordUtil.logInfo("Attempting to login with email: " + email)
            
            WebUI.openBrowser('')
            WebUI.navigateToUrl(baseUrl)
            WebUI.waitForPageLoad(10)
            
            WebUI.setText(findTestObject('Object Repository/Sign_In/input_email'), email)
            WebUI.setText(findTestObject('Object Repository/Sign_In/input_password'), password)
            WebUI.click(findTestObject('Object Repository/Sign_In/button_Sign in'))
            
            WebUI.waitForPageLoad(15)
            WebUI.delay(2)
            
            String currentUrl = WebUI.getUrl()
            boolean isLoggedIn = currentUrl.contains('/ws/dashboard')
            
            if (isLoggedIn) {
                KeywordUtil.logInfo("Login successful. Redirected to: " + currentUrl)
                KeywordUtil.markPassed("Login successful")
                return true
            } else {
                KeywordUtil.markWarning("Login failed. Current URL: " + currentUrl)
                KeywordUtil.markFailed("Login failed - URL does not contain '/ws/dashboard'")
                return false
            }
            
        } catch (Exception e) {
            KeywordUtil.markError("Error during login: " + e.getMessage())
            KeywordUtil.markFailed("Login error: " + e.getMessage())
            return false
        }
    }

    /**
     * Login using Global Variables
     * @return true if login successful, false otherwise
     */
    @Keyword
    def loginWithGlobalVariables() {
        return login(GlobalVariable.Email, GlobalVariable.Password)
    }

    /**
     * Login and verify success
     * @param email - user email
     * @param password - user password
     * @throws AssertionError if login fails
     */
    @Keyword
    def loginAndVerify(String email, String password) {
        boolean result = login(email, password)
        if (!result) {
            KeywordUtil.markFailed("Login verification failed")
        }
        return result
    }

    /**
     * Login using Global Variables and verify success
     * @throws AssertionError if login fails
     */
    @Keyword
    def loginWithGlobalVariablesAndVerify() {
        return loginAndVerify(GlobalVariable.Email, GlobalVariable.Password)
    }

    /**
     * Check if user is currently logged in by checking URL
     * @return true if logged in, false otherwise
     */
    @Keyword
    def isLoggedIn() {
        try {
            String currentUrl = WebUI.getUrl()
            boolean loggedIn = currentUrl != null && currentUrl.contains('/ws/dashboard')
            KeywordUtil.logInfo("User is " + (loggedIn ? "logged in" : "not logged in"))
            return loggedIn
        } catch (Exception e) {
            KeywordUtil.markWarning("Could not determine login status: " + e.getMessage())
            return false
        }
    }

    /**
     * Check if user is currently logged in by checking for logout button or user info
     * @param logoutTestObject - Test object for logout button (optional)
     * @return true if logged in, false otherwise
     */
    @Keyword
    def isLoggedInWithElement(TestObject logoutTestObject = null) {
        try {
            if (logoutTestObject != null) {
                WebElement element = WebUI.findWebElement(logoutTestObject, 5, FailureHandling.OPTIONAL)
                boolean hasLogout = element != null && element.isDisplayed()
                KeywordUtil.logInfo("User is " + (hasLogout ? "logged in" : "not logged in") + " (by element check)")
                return hasLogout
            } else {
                return isLoggedIn()
            }
        } catch (Exception e) {
            KeywordUtil.markWarning("Could not determine login status: " + e.getMessage())
            return false
        }
    }

    /**
     * Logout from the application
     * @param logoutButton - Test object for logout button
     * @param confirmLogout - Test object for confirm logout (optional)
     * @return true if logout successful, false otherwise
     */
    @Keyword
    def logout(TestObject logoutButton, TestObject confirmLogout = null) {
        try {
            KeywordUtil.logInfo("Attempting to logout")
            
            WebUI.click(logoutButton)
            WebUI.waitForPageLoad(5)
            
            if (confirmLogout != null) {
                WebUI.click(confirmLogout)
                WebUI.waitForPageLoad(5)
            }
            
            String currentUrl = WebUI.getUrl()
            boolean isLoggedOut = !currentUrl.contains('/ws/dashboard')
            
            if (isLoggedOut) {
                KeywordUtil.logInfo("Logout successful. Redirected to: " + currentUrl)
                KeywordUtil.markPassed("Logout successful")
                return true
            } else {
                KeywordUtil.markWarning("Logout failed. Still on: " + currentUrl)
                KeywordUtil.markFailed("Logout failed")
                return false
            }
            
        } catch (Exception e) {
            KeywordUtil.markError("Error during logout: " + e.getMessage())
            KeywordUtil.markFailed("Logout error: " + e.getMessage())
            return false
        }
    }

    /**
     * Quick login and close browser (for setup)
     */
    @Keyword
    def quickLogin() {
        loginWithGlobalVariables()
        WebUI.closeBrowser()
    }

    /**
     * Navigate to login page
     * @param baseUrl - base URL to navigate to (optional)
     */
    @Keyword
    def navigateToLoginPage(String baseUrl = null) {
        if (baseUrl == null) {
            baseUrl = GlobalVariable.BASE_URL
        }
        WebUI.openBrowser('')
        WebUI.navigateToUrl(baseUrl)
        WebUI.waitForPageLoad(10)
        KeywordUtil.logInfo("Navigated to login page: " + baseUrl)
    }

    /**
     * Fill login form without submitting
     * @param email - user email
     * @param password - user password
     */
    @Keyword
    def fillLoginForm(String email, String password) {
        WebUI.setText(findTestObject('Object Repository/Sign_In/input_email'), email)
        WebUI.setText(findTestObject('Object Repository/Sign_In/input_password'), password)
        KeywordUtil.logInfo("Login form filled")
    }

    /**
     * Submit login form
     * @return true if submission successful
     */
    @Keyword
    def submitLoginForm() {
        WebUI.click(findTestObject('Object Repository/Sign_In/button_Sign in'))
        WebUI.waitForPageLoad(15)
        KeywordUtil.logInfo("Login form submitted")
        return true
    }

    /**
     * Switch to a new browser window/tab
     * @param windowIndex - index of window to switch to (0-based)
     * @return true if successful
     */
    @Keyword
    def switchToWindow(int windowIndex = 1) {
        try {
            WebUI.switchToWindowIndex(windowIndex)
            WebUI.waitForPageLoad(10)
            KeywordUtil.logInfo("Switched to window index: " + windowIndex)
            return true
        } catch (Exception e) {
            KeywordUtil.markError("Failed to switch to window: " + e.getMessage())
            return false
        }
    }

    /**
     * Get current window handle
     * @return current window handle
     */
    @Keyword
    def String getCurrentWindowHandle() {
        return DriverFactory.getWebDriver().getWindowHandle()
    }

    /**
     * Open new tab and navigate to URL
     * @param url - URL to navigate to
     */
    @Keyword
    def openNewTab(String url) {
        try {
            WebUI.executeJavaScript("window.open('" + url + "','_blank')", null)
            WebUI.delay(2)
            WebUI.switchToWindowIndex(1)
            WebUI.waitForPageLoad(10)
            KeywordUtil.logInfo("Opened new tab with URL: " + url)
        } catch (Exception e) {
            KeywordUtil.markError("Failed to open new tab: " + e.getMessage())
        }
    }
}