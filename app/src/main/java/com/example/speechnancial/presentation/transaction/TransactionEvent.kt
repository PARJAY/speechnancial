package com.example.speechnancial.presentation.transaction

// CRUD presentation side
sealed interface TransactionEvent {
//    data class CreateTransaction(val customerId : String, val customer: CustomerModel):
//        TransactionEvent
//    data class UpdateTransaction(val customerId : String, val customer: CustomerModel):
//        TransactionEvent
    data class GetTransactionById (val customerId : String) : TransactionEvent
//    data class DeleteTransaction(val customer: CustomerModel): TransactionEvent
}