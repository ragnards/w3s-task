package org.ragnards.testtask.utils

import io.qameta.allure.Attachment
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.WebDriver
import org.ragnards.testtask.tests.TestBase
import org.testng.ITestListener
import org.testng.ITestResult

class TestListener : TestBase(), ITestListener {

    @Attachment(value = "Page screenshot", type = "image/png")
    fun saveScreenshotPNG(driver: WebDriver): ByteArray {
        return (driver as TakesScreenshot).getScreenshotAs(OutputType.BYTES)
    }

    override fun onTestFailure(iTestResult: ITestResult) {
        val testClass = iTestResult.instance
        val driver = (testClass as TestBase).driver
        println("Screenshot captured for:" + getTestMethodName(iTestResult))
        saveScreenshotPNG(driver)
    }

    private fun getTestMethodName(iTestResult: ITestResult): String {
        return iTestResult.method.constructorOrMethod.name
    }
}