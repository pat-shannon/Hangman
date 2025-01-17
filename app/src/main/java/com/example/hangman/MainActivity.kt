package com.example.hangman
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hangman.ui.theme.HangmanTheme
import java.io.IOException
import kotlin.random.Random


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
            game(
                gameOverScreen = { word: String -> navController.navigate("gameOver/${word}")},
                welcomeScreen = {navController.navigate("welcomePage")},
                {navController.navigate("winPage")})
        }
        composable (route = "gameOver/{word}"){
            val word = it.arguments?.getString("word")
            gameOver(word= word.toString(), onNextScreen = {navController.navigate("game")},
                welcomeScreen = {navController.navigate("welcomePage")})
        }
        composable (route = "winPage"){
            winPage(onNextScreen = {navController.navigate("game")},
                welcomeScreen = {navController.navigate("welcomePage")})
        }
    }
}

public fun getWords(context: Context): MutableList<String>{
    var wordList: MutableList<String> = mutableListOf()


    try{
        val assetManager = context.assets
        val inputStream = assetManager.open("Words.txt")
        inputStream.bufferedReader().forEachLine { wordList.add(it.uppercase())}
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return wordList
}





@Composable
fun game(gameOverScreen: (String) -> Unit, welcomeScreen: () -> Unit, winPage: () -> Unit) {

    val context = LocalContext.current
    val wordList = getWords(context)
    val randomIndex = Random.nextInt(wordList.size)

    var word by remember {mutableStateOf(wordList[randomIndex])}
    var progress by remember {mutableStateOf("_".repeat(word.length))}
    var incorrectCounter by remember { mutableIntStateOf(0)}
    val maxCounter = 8
    println(progress)
    var InputLetter by remember {mutableStateOf("")}
    var incorrectLetters by remember {mutableStateOf("")}
    val image = when(incorrectCounter){
        0 -> R.drawable.h0
        1 -> R.drawable.h1
        2 -> R.drawable.h2
        3 -> R.drawable.h3
        4 -> R.drawable.h4
        5 -> R.drawable.h5
        6 -> R.drawable.h6
        7 -> R.drawable.h7
        else -> {R.drawable.h0}}


    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ){
        Text(text = "Hangman Game",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(top = 100.dp))

        Text(text = progress,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 100.dp),
            letterSpacing = 2.sp
        )



        // User InputLetter Field
        OutlinedTextField(
            value = InputLetter.uppercase(),
            onValueChange = { if(it.length < 2) {
                InputLetter = it.uppercase() }
            },
            label = { Text("type a letter") },
            modifier = Modifier
                .fillMaxWidth(0.5f),
            keyboardOptions = KeyboardOptions.Default

        )
        Button(
            onClick = {
                var checkWord = word

                if (InputLetter in word){
                    for (letter in word){
                        if (InputLetter == letter.toString()){
                            var index = checkWord.indexOf(letter)
                            println("checkWord was "+ checkWord)
                            checkWord = checkWord.replaceRange(index, index+1, "*")
                            println("checkWord goes to -> "+ checkWord)
                            progress = progress.replaceRange(index, index+1, letter.toString())
                        }
                    }
                } else{
                    if(InputLetter !in incorrectLetters){
                        incorrectCounter+=1
                        incorrectLetters = "${incorrectLetters} ${InputLetter}"
                    }
                    if (incorrectCounter == maxCounter){
                        // User has lost
                        gameOverScreen(word)

                    }
                }
                InputLetter = ""
                if (word == progress) {
                winPage()
            }

            }
        ) {
            Text(text="Submit")
        }
        Text(text="Incorrect Attempts: ${incorrectCounter}")
        Image(painter = painterResource(id = image),
            contentDescription = null)
        Text(text="Incorrect Letters:\n${incorrectLetters}")
        Button(
            onClick = {welcomeScreen()}
        ){
            Text(text="Exit")
        }
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

@Composable
fun gameOver(word: String, onNextScreen: () -> Unit, welcomeScreen: () -> Unit){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ){
        Text(text = "Game Over\nThe word was: $word",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(top = 100.dp))

        Button(
            onClick = {onNextScreen()}
        ){
            Text(text="Try Again")
        }
        Button(
            onClick = {welcomeScreen()}
        ){
            Text(text="Exit")
        }

    }

        }

@Composable
fun winPage(onNextScreen: () -> Unit, welcomeScreen: () -> Unit){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ){
        Text(text = "You win!\n💪🏽",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(top = 100.dp))

        Button(
            onClick = {onNextScreen()}
        ){
            Text(text="Try Again")
        }
        Button(
            onClick = {welcomeScreen()}
        ){
            Text(text="Exit")
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