package com.martynov.tydchatand.repository

import android.graphics.Bitmap
import com.martynov.tydchatand.data.AttachmentModel
import com.martynov.tydchatand.data.Token
import com.martynov.tydchatand.model.AutorModel
import com.martynov.tydchatand.model.MessangeModel
import retrofit2.Response

interface Repository {
    suspend fun authenticate(login: String, password: String): Response<Token>
    suspend fun uploadUser(bitmap: Bitmap): Response<AttachmentModel>
    suspend fun register(
            login: String,
            password: String,
            attachmentModel: AttachmentModel?
    ): Response<Token>
    suspend fun getMe():Response<AutorModel>
    suspend fun getAllMessange(): Response<List<MessangeModel>>
    suspend fun newMessange(messangeModel: MessangeModel): Response<List<MessangeModel>>
}