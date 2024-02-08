package org.ragnards.testtask.pages

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.ragnards.testtask.elements.ResultTableElement
import org.ragnards.testtask.setValue

class TrySqlPage(private val driver: WebDriver) {

    @FindBy(css = "#tryitform #code")
    private val sqlStatementField: WebElement? = null

    @FindBy(css = "button.ws-btn")
    private val runSqlButton: WebElement? = null

    @FindBy(id = "restoreDBBtn")
    private val restoreDbButton: WebElement? = null

    init {
        PageFactory.initElements(driver, this)
    }

    fun fillSqlStatement(sqlRequest: String) = apply {
        sqlStatementField?.setValue(driver, sqlRequest)
    }

    fun runSql() = apply {
        runSqlButton?.click()
    }

    fun restoreDatabase() = apply {
        restoreDbButton?.click()
        driver.switchTo().alert().accept()
    }

    fun goToResultTable() = ResultTableElement(driver)
}