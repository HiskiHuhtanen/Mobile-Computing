package com.example.composetutorialhw1

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import kotlinx.serialization.Serializable
import java.io.File


@Serializable
object SignInScreen

@Composable
fun UserNameInput( modifier: Modifier) {
    var username by remember { mutableStateOf("") }
    val context = LocalContext.current

    //https://www.youtube.com/watch?v=uHX5NB6wHao
    //https://medium.com/@yogesh_shinde/implementing-image-video-documents-picker-in-jetpack-compose-73ef846cfffb
    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            val newUri = saveImage(context, uri)
            User.profilePic.value = newUri
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //https://www.youtube.com/watch?v=qQVCtkg-O7w
        //https://stackoverflow.com/questions/75225853/get-image-uri-and-display-it-with-coil-in-compose-android
        AsyncImage(model = User.profilePic.value ?: R.drawable.defaultprofilepic,
            contentDescription = "Picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        Button(
            onClick = {
                pickMedia.launch(
                    PickVisualMediaRequest(PickVisualMedia.ImageOnly)
                )
            }
        ) {
            Text("Set Profile Pic")
        }


        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { User.username.value = username},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Set Username")
        }
    }

}
//https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.io/copy-to.html
//https://developer.android.com/training/data-storage/shared/media#kotlin
//https://developer.android.com/training/data-storage/app-specific#internal-access-files
//https://stackoverflow.com/questions/72025124/save-and-show-picked-picture-by-uri-after-app-restart
//yhyy
fun saveImage(context: Context, uri : Uri) : Uri {
    val resolver = context.contentResolver
    val outputFile = if (User.evenPic) {
        File(context.filesDir, "profilepic_odd.png")
    } else {
        File(context.filesDir, "profilepic_even.png")
    }

    resolver.openInputStream(uri)?.use { input ->
        outputFile.outputStream().use { output ->
            input.copyTo(output)
        }
    }
    User.evenPic = !User.evenPic
    return outputFile.toUri()
}