package com.martynov.tydchatand.data

import com.martynov.tydchatand.BASE_URL

enum class AttachmentType {
    IMAGE
}

data class AttachmentModel(val id: String, val mediaType: AttachmentType) {
    val url
        get() = "$BASE_URL/api/v1/static/$id"
    val urlUser
        get() = "$BASE_URL/api/v1/static/user/$id"
}