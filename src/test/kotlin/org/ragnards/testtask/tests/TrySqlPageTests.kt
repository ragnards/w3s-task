package org.ragnards.testtask.tests

import org.ragnards.testtask.models.Customer
import org.ragnards.testtask.pages.TrySqlPage
import org.ragnards.testtask.utils.TestListener
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Listeners
import org.testng.annotations.Test

@Listeners(TestListener::class)
class TrySqlPageTests : TestBase() {

    private lateinit var trySqlPage: TrySqlPage

    @BeforeClass
    fun setup() {
        trySqlPage = TrySqlPage(driver)
    }

    @BeforeMethod
    fun openPage() {
        driver.get("$baseUrl/sql/trysql.asp?filename=trysql_select_all")
    }

    @Test
    fun `Customers table should has customer with expected address`() {
        trySqlPage.fillSqlStatement("SELECT * FROM Customers;")
            .runSql()
            .goToResultTable()
            .findRowByColumnValue(column = "ContactName", value = "Giovanni Rovelli")
            .checkRowValueByColumnName(column = "Address", expectedValue = "Via Ludovico il Moro 22")
    }

    @Test
    fun `Customers table should has only 6 customers from London`() {
        trySqlPage.fillSqlStatement("SELECT * FROM Customers WHERE City = \'London\';")
            .runSql()
            .goToResultTable()
            .checkNumberOfRecords(6)
            .checkAllRowsHaveValue(column = "City", expectedValue = "London")
    }

    @Test
    fun `Customers table should contain a new record when it's added`() {
        val newCustomer = Customer(
            customerName = "Central Bank",
            contactName = "Stavros Agrotis",
            address = "80, Kennedy Avenue",
            city = "Nicosia",
            postalCode = "1076",
            country = "Cyprus"
        )
        trySqlPage.fillSqlStatement(
            "INSERT INTO Customers (CustomerName, ContactName, Address, City, PostalCode, Country) " +
                    "VALUES (${newCustomer.toInsertableSqlString()});"
        )
            .runSql()
            .fillSqlStatement("SELECT * FROM Customers;")
            .runSql()
            .goToResultTable()
            .findRowByColumnValue(column = "CustomerName", value = newCustomer.customerName)
            .checkCustomerRecord(expected = newCustomer)
    }

    @Test
    fun `Customers table should contain an updated record when it's updated`() {
        val newCustomer = Customer(
            customerName = "Central Bank",
            contactName = "Stavros Agrotis",
            address = "80, Kennedy Avenue",
            city = "Nicosia",
            postalCode = "1076",
            country = "Cyprus"
        )

        trySqlPage.fillSqlStatement(
            "UPDATE Customers " +
                    "SET ${newCustomer.toUpdatableSqlString()} " +
                    "WHERE CustomerID = 5;"
        )
            .runSql()
            .fillSqlStatement("SELECT * FROM Customers WHERE CustomerID = 5;")
            .runSql()
            .goToResultTable()
            .checkNumberOfRecords(1)
            .findRowByColumnValue(column = "CustomerID", value = "5")
            .checkCustomerRecord(expected = newCustomer)
    }

    @Test
    fun `Customer table can be restored using the restore button`() {
        trySqlPage.fillSqlStatement("DELETE FROM Customers WHERE CustomerID = 10;")
            .runSql()
            .restoreDatabase()
            .fillSqlStatement("SELECT * FROM Customers;")
            .runSql()
            .goToResultTable()
            .checkNumberOfRecords(91)
    }
}