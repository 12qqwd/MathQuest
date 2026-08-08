package com.example.mathquest

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mathquest.ui.theme.MathQuestTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MathQuestTheme(
                darkTheme = false,
                dynamicColor = false
            ) {
                MathQuestNavigation()
            }
        }
    }
}

@Composable
fun MathQuestNavigation() {

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.destination?.route

    val context = LocalContext.current

    val database = remember {
        MathQuestDatabase.getDatabase(context)
    }

    val repository = remember {
        QuestionRepository(database.questionDao())
    }

    val questionViewModel: QuestionViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T {
                return QuestionViewModel(repository) as T
            }
        }
    )

    Scaffold(

        bottomBar = {

            NavigationBar {

                NavigationBarItem(
                    selected = currentRoute == "home",
                    onClick = {
                        navController.navigate("home") {
                            popUpTo("home")
                            launchSingleTop = true
                        }
                    },
                    icon = {},
                    label = {
                        Text("Home")
                    }
                )

                NavigationBarItem(
                    selected = currentRoute == "game",
                    onClick = {
                        navController.navigate("game") {
                            launchSingleTop = true
                        }
                    },
                    icon = {},
                    label = {
                        Text("Game")
                    }
                )

                NavigationBarItem(
                    selected = currentRoute == "settings",
                    onClick = {
                        navController.navigate("settings") {
                            launchSingleTop = true
                        }
                    },
                    icon = {},
                    label = {
                        Text("Settings")
                    }
                )
            }
        }

    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {

            composable("home") {

                HomeScreen(
                    onStartGame = {
                        navController.navigate("game")
                    }
                )
            }

            composable("game") {

                GameScreen(
                    viewModel = questionViewModel
                )
            }

            composable("settings") {

                SettingsScreen()
            }
        }
    }
}

@Composable
fun HomeScreen(
    onStartGame: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "MATHQUEST",
            fontSize = 36.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "Math Learning Adventure",
            fontSize = 18.sp,
            color = Color.DarkGray
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Button(
            onClick = onStartGame,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "START GAME",
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun GameScreen(
    viewModel: QuestionViewModel
) {

    val questions by viewModel.questions.collectAsState()

    val context = LocalContext.current

    val preferences = remember {
        context.getSharedPreferences(
            "mathquest_preferences",
            Context.MODE_PRIVATE
        )
    }

    var currentQuestionIndex by remember {
        mutableIntStateOf(0)
    }

    var score by remember {
        mutableIntStateOf(0)
    }

    var selectedAnswer by remember {
        mutableStateOf<Int?>(null)
    }

    var answerChecked by remember {
        mutableStateOf(false)
    }

    var gameFinished by remember {
        mutableStateOf(false)
    }

    var bestScore by remember {
        mutableIntStateOf(
            preferences.getInt("best_score", 0)
        )
    }

    LaunchedEffect(Unit) {
        viewModel.loadQuestions()
    }

    if (questions.isEmpty()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),

            horizontalAlignment = Alignment.CenterHorizontally,

            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Loading questions...",
                fontSize = 20.sp,
                color = Color.DarkGray
            )
        }

        return
    }

    if (gameFinished) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(24.dp),

            horizontalAlignment = Alignment.CenterHorizontally,

            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "GAME COMPLETE!",
                fontSize = 32.sp,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Text(
                text = "Your Score",
                fontSize = 22.sp,
                color = Color.DarkGray
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "$score / ${questions.size}",
                fontSize = 40.sp,
                color = Color.Black
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Text(
                text = "Best Score",
                fontSize = 22.sp,
                color = Color.DarkGray
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "$bestScore / ${questions.size}",
                fontSize = 32.sp,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            Button(
                onClick = {

                    currentQuestionIndex = 0
                    score = 0
                    selectedAnswer = null
                    answerChecked = false
                    gameFinished = false
                },

                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "PLAY AGAIN",
                    fontSize = 18.sp
                )
            }
        }

        return
    }

    val currentQuestion = questions[currentQuestionIndex]

    val options = listOf(
        currentQuestion.option1,
        currentQuestion.option2,
        currentQuestion.option3,
        currentQuestion.option4
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "MathQuest",
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "Question ${currentQuestionIndex + 1} / ${questions.size}",
            fontSize = 18.sp,
            color = Color.DarkGray
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Score: $score",
            fontSize = 18.sp,
            color = Color.DarkGray
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        Text(
            text = currentQuestion.question,
            fontSize = 26.sp,
            color = Color.Black
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        options.forEach { answer ->

            Button(
                onClick = {

                    if (!answerChecked) {

                        selectedAnswer = answer
                        answerChecked = true

                        if (answer == currentQuestion.answer) {

                            score++

                            if (score > bestScore) {

                                bestScore = score

                                preferences.edit()
                                    .putInt("best_score", bestScore)
                                    .apply()
                            }
                        }
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {

                Text(
                    text = answer.toString(),
                    fontSize = 18.sp
                )
            }
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        if (answerChecked) {

            if (selectedAnswer == currentQuestion.answer) {

                Text(
                    text = "Correct! 🎉",
                    fontSize = 22.sp,
                    color = Color(0xFF2E7D32)
                )

            } else {

                Text(
                    text = "Incorrect!",
                    fontSize = 22.sp,
                    color = Color(0xFFC62828)
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Correct answer: ${currentQuestion.answer}",
                    fontSize = 18.sp,
                    color = Color.DarkGray
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Button(
                onClick = {

                    if (currentQuestionIndex < questions.lastIndex) {

                        currentQuestionIndex++

                        selectedAnswer = null

                        answerChecked = false

                    } else {

                        gameFinished = true
                    }
                },

                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = if (currentQuestionIndex < questions.lastIndex) {
                        "NEXT QUESTION"
                    } else {
                        "SEE RESULT"
                    },
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun SettingsScreen() {

    val context = LocalContext.current

    val preferences = remember {
        context.getSharedPreferences(
            "mathquest_preferences",
            Context.MODE_PRIVATE
        )
    }

    var bestScore by remember {
        mutableIntStateOf(
            preferences.getInt("best_score", 0)
        )
    }

    var soundEnabled by remember {
        mutableStateOf(
            preferences.getBoolean("sound_enabled", true)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Text(
            text = "SETTINGS",
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Text(
            text = "Best Score",
            fontSize = 20.sp,
            color = Color.DarkGray
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "$bestScore / 5",
            fontSize = 32.sp,
            color = Color.Black
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Text(
            text = "Sound",
            fontSize = 20.sp,
            color = Color.DarkGray
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Switch(
            checked = soundEnabled,

            onCheckedChange = { enabled ->

                soundEnabled = enabled

                preferences.edit()
                    .putBoolean("sound_enabled", enabled)
                    .apply()
            }
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Button(
            onClick = {

                preferences.edit()
                    .remove("best_score")
                    .apply()

                bestScore = 0
            },

            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "RESET BEST SCORE"
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "MathQuest v1.0",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}
