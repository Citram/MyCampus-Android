package io.github.citram.mycampus.models

data class LoginResponse(val error: Boolean, val message:String, val user: User)