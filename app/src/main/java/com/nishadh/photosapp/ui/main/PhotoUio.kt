package com.nishadh.photosapp.ui.main

data class PhotoUio(
    val id: String,
    val author: String,
    var imageUrl: String,
    val placeholder: Boolean = false
)