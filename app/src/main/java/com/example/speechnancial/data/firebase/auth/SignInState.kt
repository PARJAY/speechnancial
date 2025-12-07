package com.example.speechnancial.data.firebase.auth

data class SignInState (
    val isSignInSuccessful: Boolean = false,
    val userData: UserData? = UserData(),
    val signInErrorMessage: String? = null
)