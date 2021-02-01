package com.martynov.tydchatand.data

data class RegistrationRequestParams(
        val username: String,
        val password: String,
        val attachmentModel: AttachmentModel?
)