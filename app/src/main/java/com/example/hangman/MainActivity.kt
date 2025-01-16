package com.example.hangman

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hangman.ui.theme.HangmanTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HangmanTheme {
               Surface(modifier = Modifier.fillMaxSize()){
                   App()
                }
            }
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()

    // Define NavHost with the required arguments
    NavHost(
        navController = navController,
        startDestination = "welcomePage",
    ) {
        composable(route = "welcomePage") {
            welcomePage(onNextScreen = {navController.navigate("game")})
        }
        composable (route = "game"){
            game()
        }
    }
}

@Composable
fun game() {
    var word by remember {mutableStateOf("temporary")}
    var progress by remember {mutableStateOf("")}
    for(letter in word){
        progress += ("_")

    }
    println(progress)
    var InputLetter by remember {mutableStateOf("temporary")}

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ){
        Text(text = "Hangman Game",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(top = 100.dp))

        Text(text = "*".repeat(word.length),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 100.dp))


        // User InputLetter Field
        OutlinedTextField(
            value = InputLetter,
            onValueChange = {
                InputLetter = it },
            label = { Text("type a letter") },
            modifier = Modifier
                .fillMaxWidth(0.5f),
            keyboardOptions = KeyboardOptions.Default
        )
    }
}

@Composable
fun welcomePage(onNextScreen:()->Unit){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ){
        Text(
            text = "Welcome to Hangman",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(top = 100.dp)
        )
        Button(
            onClick = {
                onNextScreen()
        },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        ){
            Text(text = stringResource(R.string.button_label))
        }
    }
}




@Preview(showBackground = true)
@Composable
fun PreviewApp(){
    HangmanTheme {
        App()
    }
}