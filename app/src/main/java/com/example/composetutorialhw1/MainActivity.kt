package com.example.composetutorialhw1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
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
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.room.Room

import com.example.composetutorialhw1.ui.theme.ComposeTutorialHW1Theme
import kotlinx.serialization.Serializable
import java.io.File


//navigation tutorial
//https://www.youtube.com/watch?v=AIC_OFQ1r3k&t=450s
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //ingenious solution
        val evenFile = File(filesDir, "profilepic_even.png")
        val oddFile = File(filesDir, "profilepic_odd.png")
        when {
            evenFile.exists() -> {
                User.evenPic = true
                User.profilePic.value = evenFile.toUri()
            }
            oddFile.exists() -> {
                User.evenPic = false
                User.profilePic.value = oddFile.toUri()
            }
        }
        enableEdgeToEdge()
        setContent {
            ComposeTutorialHW1Theme {
                val navController = rememberNavController()

                val db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java, "database-name"
                )
                    .enableMultiInstanceInvalidation() //I am so grateful for the Android devs
                    .build()
                //If your app runs in multiple processes, include enableMultiInstanceInvalidation() in your database builder invocation.
                // That way, when you have an instance of AppDatabase in each process, you can invalidate the shared database file in one process,
                // and this invalidation automatically propagates to the instances of AppDatabase within other processes.

                LaunchedEffect(Unit) {
                    val intent = Intent(this@MainActivity, CatFactService::class.java).apply {
                        action = CatFactService.Actions.START.name
                    }
                    ContextCompat.startForegroundService(this@MainActivity, intent)
                }

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
                            Spacer(Modifier.height(16.dp))
                            Button(onClick = {
                                navController.navigate(SignInScreen)
                            }) {
                                Text("Settings")
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
                        MessagesScreen(
                            messageDao = db.messageDao(),
                            onBack = { navController.navigateUp() }
                        )
                    }

                    composable<SignInScreen> {
                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    colors = topAppBarColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        titleContentColor = MaterialTheme.colorScheme.primary,
                                    ),
                                    title = {
                                        Text("Set Username")
                                    },
                                    navigationIcon = {
                                        IconButton(onClick = { navController.navigateUp() }) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                                contentDescription = "Signing"
                                            )
                                        }
                                    }
                                )
                            }
                        ) { innerPadding ->
                            UserNameInput(
                                modifier = Modifier.padding((innerPadding))
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
