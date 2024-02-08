package org.ragnards.testtask.pages

import org.openqa.selenium.NotFoundException
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.FluentWait
import java.time.Duration

class CookieWallPage(private val driver: WebDriver) {

    @FindBy(css = "#snigel-cmp-framework #accept-choices")
    private val acceptAllCookiesButton: WebElement? = null

    init {
        PageFactory.initElements(driver, this)
    }

    fun passCookieWallIfNeeded() {
        val wait = FluentWait(driver)
            .withTimeout(Duration.ofSeconds(4))
            .pollingEvery(Duration.ofMillis(500))
            .ignoring(NotFoundException::class.java)
        try {
            wait.until {
                acceptAllCookiesButton?.click()
                true
            }
        } catch (ignore: TimeoutException) {
        }
    }
}