package com.example.speechnancial.data.firebase.model

data class UserModel (
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val phone_number: String = "",
    val pair_code: String = ""
)

data class EditedUserModel (
    val id : String = "",
    val address : String = "",
    val phone_number : String = ""
)