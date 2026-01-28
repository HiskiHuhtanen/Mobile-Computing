package com.example.composetutorialhw1


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.serialization.Serializable

import kotlinx.coroutines.launch

@Serializable
object MessagesRoute
//data class Message(val author: String, val body: String)

//how to stop scaffold from getting in the way
//https://slack-chats.kotlinlang.org/t/16382445/when-i-use-scaffold-it-draws-over-my-existing-composable-i-w
//https://developer.android.com/develop/ui/compose/components/scaffold#example

@Composable
fun Conversations(messages: List<MessageData>, modifier: Modifier = Modifier) {
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
fun MessagesCard(msg: MessageData) {

    val profilePicUri = User.profilePic.value
    //Padding
    Row(modifier = Modifier.padding(all = 8.dp)) {
        //USED TO BE IMAGE NOW NOT, BECAUSE WE CAN SET IT
        AsyncImage(
            model = profilePicUri ?: R.drawable.defaultprofilepic,
            contentDescription = "Contact profile picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
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
                text = msg.author ?: "",
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
                    text = msg.text ?: "",
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

//A LOT FROM THE ANDROID STUDIO DOCUMENTATION
//PROBLEMS!!!:
//-SEND MESSAGE BUTTON IS NOT STILL
//-CAN SEND EMPTY MESSAGES
//-CAN SEND MESSAGES WITHOUT USENAME <- "fix" make username be "Blank" normally
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(onBack: () -> Unit, messageDao: MessageDao, ) {

    //https://developer.android.com/training/data-storage/room
    val messages by messageDao.getAllUpdates().collectAsState(initial = emptyList())
    var input by rememberSaveable { mutableStateOf("") }

    val username = User.username.value
    val scope = rememberCoroutineScope()

//    //https://www.youtube.com/watch?v=mNKQ9dc1knI
//    LaunchedEffect(Unit) {
//        messages = messageDao.getAll()
//    }

    
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
        //NEW MESSAGE
        bottomBar = { //https://developer.android.com/develop/ui/compose/quick-guides/content/display-bottom-app-bar
            BottomAppBar {
                TextField(
                    value = input,
                    onValueChange = { input = it },
                    //modifier = Modifier.weight(1f).padding(start = 8.dp),
                    placeholder = { Text("Send a message…") },
                )
                IconButton( //https://developer.android.com/training/data-storage/room
                    onClick = {
                            val userText = input.trim()
                            val newMessage = MessageData(author = username, text = input)
                            scope.launch {
                                messageDao.insertMessage(newMessage)
                                val symbol = checkUserInput(userText)   //pretty bad, should move this out of here
                                if (symbol != null) {
                                    val reply = if(checkIfStockExists(symbol)) {
                                        lookUpStock(symbol)
                                    } else {
                                        "Uuuh, dunno about that"
                                    }
                                    messageDao.insertMessage(MessageData(author = "StockBot", text = reply))
                                }
                            }
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
