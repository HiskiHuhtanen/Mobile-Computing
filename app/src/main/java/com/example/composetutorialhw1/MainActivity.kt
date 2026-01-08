package com.example.composetutorialhw1

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute

import com.example.composetutorialhw1.ui.theme.ComposeTutorialHW1Theme
import kotlinx.serialization.Serializable


//navigation tutorial
//https://www.youtube.com/watch?v=AIC_OFQ1r3k&t=450s
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeTutorialHW1Theme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = ScreenA
                ) {
                    composable<ScreenA> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(onClick = {
                                navController.navigate(
                                    ScreenB(
                                        name = "Hiski",
                                        age = 900
                                    )
                                )
                            }) {
                                Text(text = "Go to ScreenB ")
                            }
                            Spacer(Modifier.height(16.dp))
                            Button(onClick = {
                                navController.navigate(MessagesRoute)
                            }) {
                                Text("Go to Messages")
                            }
                        }
                    }
                    composable<ScreenB> {
                        val args = it.toRoute<ScreenB>()
                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    colors = topAppBarColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        titleContentColor = MaterialTheme.colorScheme.primary,
                                    ),
                                    title = {
                                        Text("Sacred and Secretive Knowledge")
                                    },
                                    navigationIcon = {
                                        IconButton(onClick = { navController.navigateUp() }) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                                contentDescription = "Lol local"
                                            )
                                        }
                                    }
                                )
                            }
                        ) { innerPadding ->
                            Column(
                                modifier = Modifier.fillMaxSize().padding(innerPadding),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally

                            ) {
                                Text(text = "${args.name}, ${args.age} years old")
                            }
                        }
                    }
                    //navigation, but backwards!!!
                    //https://developer.android.com/develop/ui/compose/components/app-bars-navigate
                    composable<MessagesRoute> {
                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    colors = topAppBarColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        titleContentColor = MaterialTheme.colorScheme.primary,
                                    ),
                                    title = {
                                        Text("Messages")
                                    },
                                    navigationIcon = {
                                        IconButton(onClick = { navController.navigateUp() }) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                                contentDescription = "Local lol"
                                            )
                                        }
                                    }
                                )
                            }
                        ) { innerPadding ->
                            Conversations(
                                SampleData.conversationSample,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }

                }
            }
        }
    }
}

@Serializable
object ScreenA

@Serializable
data class ScreenB(
    val name: String?,
    val age: Int
)



//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//           ComposeTutorialHW1Theme {
//                Conversation(SampleData.conversationSample)
//            }
//        }
//    }
//}