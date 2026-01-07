package com.example.composetutorialhw1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MainScreen() {
    Column(modifier =
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(text = "No terve!")
        Button(onClick = { }) {
            Text(text = "Go to Screen 2")
        }
    }
}

@Preview
@Composable
fun PreviewMain() {
    Column {
        Text(text = "No terve!")
        Button(onClick = { }) {
            Text(text = "Go to Screen 2")
        }
    }
}