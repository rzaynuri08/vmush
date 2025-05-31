package com.example.vmush.model

data class LinkFirebase(
    val link: String
)

data class LinkFirebaseResponse(
    val DataLinkFirebase: List<LinkFirebase>  // Change this to match the actual response field
)
