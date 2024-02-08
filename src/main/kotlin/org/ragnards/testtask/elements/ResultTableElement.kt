package org.ragnards.testtask.elements

import org.assertj.core.api.Assertions.assertThat
import org.openqa.selenium.By.cssSelector
import org.openqa.selenium.NotFoundException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.ragnards.testtask.hasElement
import org.ragnards.testtask.models.Customer
import org.ragnards.testtask.models.Customer.Companion.toCustomer
import org.ragnards.testtask.scrollToElement

class ResultTableElement(private val driver: WebDriver) {

    @FindBy(css = ".ws-table-all th")
    private val columnsHeaders: List<WebElement>? = null

    @FindBy(css = ".ws-table-all tr")
    private val rows: List<WebElement>? = null

    init {
        PageFactory.initElements(driver, this)
    }

    fun checkNumberOfRecords(number: Int) = apply {
        assertThat(rows).hasSize(number + 1)
    }

    fun findRowByColumnValue(column: String, value: String): Row {
        val rowNumber = getRowNumberByValue(columnName = column, columnValue = value)
        return Row(rows!![rowNumber])
    }

    fun checkAllRowsHaveValue(column: String, expectedValue: String) = apply {
        val columnNumber = getColumnNumberByName(column)
        rows!!.drop(1).forEach {
            val actualValue = it.findElement(cssSelector("td:nth-child($columnNumber)")).text.trim()
            assertThat(actualValue).isEqualTo(expectedValue)
        }
    }

    private fun getColumnNumberByName(columnName: String): Int {
        val index = columnsHeaders!!.indexOfFirst { element -> element.text == columnName }
            .takeIf { it != -1 }
        return index?.inc() ?: throw NotFoundException("Column with name '$columnName' not found")
    }

    private fun getRowNumberByValue(columnName: String, columnValue: String): Int {
        val columnNumber = getColumnNumberByName(columnName)
        val rowNumber = rows!!.indexOfFirst { element ->
            element.hasElement(cssSelector("td:nth-child($columnNumber)")) { e -> e.text.trim() == columnValue }
        }.takeIf { it != -1 }
        return rowNumber ?: throw NotFoundException("Row with value '$columnValue' in column '$columnName' not found")
    }

    inner class Row(private val row: WebElement) {

        fun checkRowValueByColumnName(column: String, expectedValue: String) {
            driver.scrollToElement(row)
            val columnNumber = getColumnNumberByName(column)
            val actualColumnValue = row.findElement(cssSelector("td:nth-child($columnNumber)")).text.trim()
            assertThat(actualColumnValue).isEqualTo(expectedValue)
        }

        fun checkCustomerRecord(expected: Customer) {
            driver.scrollToElement(row)
            val rowValues = row.findElements(cssSelector("td")).map { element -> element.text.trim() }.toList()
            val actualCustomer = rowValues.toCustomer()
            assertThat(actualCustomer).usingRecursiveComparison()
                .ignoringFields("customerId")
                .isEqualTo(expected)
            assertThat(actualCustomer.customerId).isNotNull()
        }
    }
}