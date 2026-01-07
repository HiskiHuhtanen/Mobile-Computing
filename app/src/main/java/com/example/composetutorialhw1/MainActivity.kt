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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination =  ScreenA
            ) {
                composable<ScreenA> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(onClick = {
                            navController.navigate(ScreenB(
                                name = "Hiski",
                                age = 900
                            ))
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
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "${args.name}, ${args.age} years old")
                    }
                }

                composable<MessagesRoute> {
                    Conversations(SampleData.conversationSample)
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