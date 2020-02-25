package io.github.citram.mycampus.api

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Api {
    @FormUrlEncoded
    @POST("userlogin")
    fun userlogin(
        @Field("email") email: String,
        @Field("password") password: String
    )
}