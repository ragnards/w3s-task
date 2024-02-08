package org.ragnards.testtask

import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions

fun WebElement.setValue(driver: WebDriver, value: String) {
    (driver as JavascriptExecutor).executeScript("window.editor.setValue(\"$value\")")
}

fun WebElement.hasElement(by: By, predicate: (WebElement) -> Boolean): Boolean {
    this.findElements(by).let {
        if (it.size != 0) {
            return predicate(it.first())
        }
    }
    return false
}

fun WebDriver.scrollToElement(element: WebElement) {
    Actions(this).moveToElement(element).perform()
}