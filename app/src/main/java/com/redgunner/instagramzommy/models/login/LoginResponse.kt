package com.redgunner.instagramzommy.models.login

data class LoginResponse(
    val authenticated: Boolean,
    val status: String,
    val user: Boolean,
    val userId: String
)