package com.example.composetutorialhw1

import android.net.Uri
import androidx.compose.runtime.mutableStateOf

object User {
    var username = mutableStateOf("Blank")
    var profilePic = mutableStateOf<Uri?>(null)

    var evenPic = true //Very dumb solution, do not read further if you do not want to know this stupidity,               so profile pic didnt get swapped because the save name is the same, so we just use this bool to swap the name of the file that we save to, forcing recomposition
}