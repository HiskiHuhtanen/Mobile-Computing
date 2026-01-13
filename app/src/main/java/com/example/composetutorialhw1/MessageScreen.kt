package com.example.composetutorialhw1


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composetutorialhw1.ui.theme.ComposeTutorialHW1Theme
import kotlinx.serialization.Serializable

import com.example.composetutorialhw1.SignInScreen
@Serializable
object MessagesRoute
data class Message(val author: String, val body: String)

//how to stop scaffold from getting in the way
//https://slack-chats.kotlinlang.org/t/16382445/when-i-use-scaffold-it-draws-over-my-existing-composable-i-w
//https://developer.android.com/develop/ui/compose/components/scaffold#example

@Composable
fun Conversations(messages: List<Message>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
    ) {
        items(messages) { message ->
            MessagesCard(message)
        }
    }
}
//speed
//https://stackoverflow.com/questions/70592694/laggy-lazy-column-android-compose
@Composable
fun MessagesCard(msg: Message) {
    //Padding
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(R.drawable.linkedin),
            contentDescription = "Contact profile picture",
            //missing from tutorial
            //https://developer.android.com/develop/ui/compose/graphics/images/customize
            contentScale = ContentScale.Crop,
            modifier = Modifier
                //image size 40dp
                .size(40.dp)
                //Clip to circle
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )
        //Horizontal space between image and column
        Spacer(modifier = Modifier.width(8.dp))

        //Keep track if expanded or not
        var isExpanded by remember { mutableStateOf(false) }
        //surfaceColor will be updated gradually from one color to the other
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        )
        //toggle the isExpanded variable when clicked on collider
        Column(modifier = Modifier.clickable { isExpanded = !isExpanded}) {
            Text(
                text = msg.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )
            //Vertical space between author and message
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                color = surfaceColor,
                modifier = Modifier.animateContentSize().padding(1.dp)) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(initialMessages: List<Message>, onBack: () -> Unit,
) {
    var messages by remember { mutableStateOf(initialMessages) }
    var input by rememberSaveable { mutableStateOf("") }

    val username = viewModel.username
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text("Messages") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = { //https://developer.android.com/develop/ui/compose/quick-guides/content/display-bottom-app-bar
            BottomAppBar {
                TextField(
                    value = input,
                    onValueChange = { input = it },
                    //modifier = Modifier.weight(1f).padding(start = 8.dp),
                    placeholder = { Text("Send a message…") },
                )
                IconButton(
                    onClick = {
                            messages = messages + Message(author = username, body = input
                            )
                            input = ""
                    }
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send")
                }
            }
        }
    ) { innerPadding ->
        Conversations(
            messages = messages,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
