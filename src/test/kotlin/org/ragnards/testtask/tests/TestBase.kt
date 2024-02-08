package org.ragnards.testtask.tests

import org.openqa.selenium.PageLoadStrategy
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import org.ragnards.testtask.pages.CookieWallPage
import org.testng.annotations.*
import java.net.URI
import java.time.Duration

abstract class TestBase {

    lateinit var driver: WebDriver
        private set

    lateinit var baseUrl: String
        private set

    @BeforeSuite
    @Parameters("site.url")
    fun setupWebDriver(@Optional baseUrl: String?) {
        this.baseUrl = baseUrl ?: System.getProperty("site.url")
        val options = ChromeOptions()
        options.setPageLoadStrategy(PageLoadStrategy.EAGER)
        options.setCapability("selenoid:options", mapOf("enableVNC" to true))
        driver = RemoteWebDriver(URI("http://selenoid:4444/wd/hub").toURL(), options)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10))
        driver.manage().window().maximize()
        driver.get(baseUrl)
    }

    @BeforeClass
    fun passCookieWall() {
        CookieWallPage(driver).passCookieWallIfNeeded()
    }

    @AfterSuite
    fun closeDriver() {
        driver.quit()
    }
}