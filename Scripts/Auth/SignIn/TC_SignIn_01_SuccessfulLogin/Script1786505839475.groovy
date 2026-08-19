import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable

WebUI.openBrowser('')
WebUI.navigateToUrl(GlobalVariable.BASE_URL)

// Enter email
WebUI.setText(findTestObject('Object Repository/Sign_In/input_email'), GlobalVariable.Email)

// Enter password
WebUI.setText(findTestObject('Object Repository/Sign_In/input_password'), GlobalVariable.Password)

// Click Sign In button
WebUI.click(findTestObject('Object Repository/Sign_In/button_Sign in'))

// Wait for redirect to complete
WebUI.waitForPageLoad(15)

// Add extra delay to ensure redirect finishes
WebUI.delay(3)

// Get current URL
String currentUrl = WebUI.getUrl()

// Print current URL to console
println("Current URL: " + currentUrl)

// Comment to show current URL in test report
WebUI.comment("Current URL: " + currentUrl)

// Verify URL redirects to ws/dashboard
WebUI.verifyEqual(currentUrl.contains('/ws/dashboard'), true)

WebUI.closeBrowser()