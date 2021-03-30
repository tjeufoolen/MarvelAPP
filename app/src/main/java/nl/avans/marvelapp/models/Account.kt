package nl.avans.marvelapp.models

import android.graphics.Bitmap

data class Account(
    val id: Int,
    var name: String,
    var email: String,
    var image: Bitmap?
)


