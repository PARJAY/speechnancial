package com.example.speechnancial.presentation.speechToTransaction

import androidx.lifecycle.ViewModel

// todo : not yet implemented
// viwemodel
class SpeechToTransactionViewModel(
//    private val customerRepository: CustomerRepositoryImpl,
//    customerId: String
): ViewModel() {

//    private val _state = MutableStateFlow(TransactionState())
//    val state = _state.asStateFlow()
//    private fun setState(newState: TransactionState) {
//        _state.value = newState
//    }
//
//    private val _effect: Channel<PesananSideEffects> = Channel()
//    val effect = _effect.receiveAsFlow()
//
//    private fun setEffect(builder: () -> PesananSideEffects) {
//        val effectValue = builder()
//        viewModelScope.launch { _effect.send(effectValue) }
//    }
//
//    init { onEvent(TransactionEvent.GetTransactionById(customerId)) }
//
//    fun onEvent(event: TransactionEvent) {
//        when (event) {
//            is TransactionEvent.DeleteTransaction -> {
//                viewModelScope.launch {
//                    // TODO : not yet implemented
//                }
//            }
//
//            is TransactionEvent.UpdateTransaction -> {
//                viewModelScope.launch {
//                    customerRepository.addOrUpdateCustomer(event.customerId, event.customer)
//                }
//            }
//
//            is TransactionEvent.GetTransactionById -> {
//                viewModelScope.launch {
//                    customerRepository.getCustomerById(event.customerId)
//                }
//            }
//
//            is TransactionEvent.CreateTransaction -> {
//                viewModelScope.launch {
//                    customerRepository.addOrUpdateCustomer(event.customerId, event.customer)
//                }
//            }
//        }
//    }
//
//    fun tambahDataCustomer(idCustomer: String, alamat : String, phoneNumber : String, name: String) {
//        viewModelScope.launch {
//            setState(_state.value.copy(isLoading = true))
//
//            try {
//                customerRepository.addOrUpdateCustomer(customerId = idCustomer, CustomerModel(address = alamat, phone_number = phoneNumber, id = idCustomer, name = name)  )
//                setState(_state.value.copy(isLoading = false))
//                setEffect { PesananSideEffects.ShowSnackBarMessage(message = "Tambah Catatan successfully") }
//            } catch (e: Exception) {
//                setState(_state.value.copy(isLoading = false, errorMessage = e.localizedMessage))
//                setEffect { PesananSideEffects.ShowSnackBarMessage(e.message ?: "Tambah Catatan GAGAl ") }
//                }
//            }
//        }

}