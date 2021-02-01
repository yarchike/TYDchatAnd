package com.martynov.tydchatand.model

import com.martynov.tydchatand.data.AttachmentModel

data class AutorModel (
        val id: Long = 0,
        val username: String,
        val attachment: AttachmentModel? = null
        )