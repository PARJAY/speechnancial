package com.example.speechnancial.presentation.speechToTransaction

// CRUD presentation side
sealed interface SpeechToTransactionEvent {
//    data class CreateTransaction(val customerId : String, val customer: CustomerModel):
//        TransactionEvent
//    data class UpdateTransaction(val customerId : String, val customer: CustomerModel):
//        TransactionEvent
    data class GetSpeechToTransactionById (val customerId : String) : SpeechToTransactionEvent
//    data class DeleteTransaction(val customer: CustomerModel): TransactionEvent
}