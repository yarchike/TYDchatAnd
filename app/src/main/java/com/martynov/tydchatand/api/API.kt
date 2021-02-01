package com.martynov.tydchatand.api

import com.martynov.tydchatand.data.*
import com.martynov.tydchatand.model.AutorModel
import com.martynov.tydchatand.model.MessangeModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface API {
    @POST("/authentication")
    suspend fun authenticate(@Body authRequestParams: AuthRequestParams): Response<Token>

    @Multipart
    @POST("api/v1/foto")
    suspend fun uploadImageUser(@Part file: MultipartBody.Part):
            Response<AttachmentModel>
    @POST("/registration")
    suspend fun register(@Body registrationRequestParams: RegistrationRequestParams): Response<Token>

    @GET("api/v1/me")
    suspend fun getMe(): Response<AutorModel>

    @GET("api/v1/messange/getall")
    suspend fun getMessange(): Response<List<MessangeModel>>
    @POST("api/v1/messange/new")
    suspend fun newMessange(@Body messangeModel: MessangeModel): Response<List<MessangeModel>>
}