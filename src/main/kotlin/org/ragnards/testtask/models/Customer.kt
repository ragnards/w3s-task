package org.ragnards.testtask.models

data class Customer(
    val customerId: String? = null,
    val customerName: String,
    val contactName: String,
    val address: String,
    val city: String,
    val postalCode: String,
    val country: String
) {
    fun toInsertableSqlString(): String {
        return "'$customerName', '$contactName', '$address', '$city', '$postalCode', '$country'"
    }

    fun toUpdatableSqlString(): String {
        return "CustomerName='$customerName', ContactName='$contactName', Address='$address', " +
                "City='$city', PostalCode='$postalCode', Country='$country'"
    }

    companion object {
        fun List<String>.toCustomer(): Customer {
            return Customer(
                customerId = this[0],
                customerName = this[1],
                contactName = this[2],
                address = this[3],
                city = this[4],
                postalCode = this[5],
                country = this[6]
            )
        }
    }
}
