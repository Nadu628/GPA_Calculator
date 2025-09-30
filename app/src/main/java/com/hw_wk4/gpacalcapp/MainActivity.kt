package com.hw_wk4.gpacalcapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hw_wk4.gpacalcapp.ui.theme.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.hw_wk4.gpacalcapp.ui.theme.GPACalcAppTheme


/*
1. For the calculator UI,
    COLUMN
        - 5 lables (textviews) and textfields (math, science, art, english, history)
        - put calculator image at top of screen
        - button: calculate gpa (average (sum of grades/5))
            - after computing, change text to clear all inputs
            - when user trys to input again, change text back to compute
        - label to display calculated gpa
            - if <60 (text-RED), 61<= GPA <= 79 (text-YELLOW),
2. NO EMPTY FIELDS -> make errorMessage
    - if input data is incorrect (not a number), make RED


 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GPACalcAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    GPACalcScreen()
                }

            }
        }
    }
}

@Composable
fun GPACalcScreen() {
    //rememberSaveable : preserves across rotation
    var math by rememberSaveable { mutableStateOf("") }
    var science by rememberSaveable { mutableStateOf("") }
    var english by rememberSaveable { mutableStateOf("") }
    var art by rememberSaveable { mutableStateOf("") }
    var history by rememberSaveable { mutableStateOf("") }
    var errorMessage by rememberSaveable { mutableStateOf("") }
    var average by rememberSaveable { mutableStateOf<Float?>(null) }
    var calculated by rememberSaveable {mutableStateOf(false)}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "GPA Calculator",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
        Courses(
            math = math,
            mathChange = { math = it },
            science = science,
            scienceChange = { science = it },
            english = english,
            englishChange = { english = it },
            art = art,
            artChange = { art = it },
            history = history,
            historyChange = { history = it }
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                if(calculated){
                    math=""
                    science=""
                    english=""
                    art=""
                    history=""
                    average=null
                    errorMessage=""
                    calculated=false
                }else{
                    val gpa = CalculateGPA(math, science, english, art, history)
                    if (gpa == null) {
                        errorMessage = "enter valid grade between 0-100"
                        average = null
                        calculated = false
                    } else {
                        errorMessage = ""
                        average = gpa
                        calculated = true
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Text(if(calculated) "Clear" else "Calculate")
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = Color.Red)
        }
        average?.let {
            val gpaColor = when {
                it <= 60.0 -> Color(0xffd32f2f)
                it >= 61.0 && it <= 79.0 -> Color(0xffffff00)
                it >= 80.0 -> Color(0xff388e3c)
                else -> MaterialTheme.colorScheme.onPrimary
            }
            Text("your GPA is: %.2f".format(it),
                color = gpaColor,
                style = MaterialTheme.typography.bodySmall
            )

        }
    }

}


@Composable
fun Courses(
    math: String,
    mathChange: (String) -> Unit,
    science: String,
    scienceChange: (String) -> Unit,
    english: String,
    englishChange: (String) -> Unit,
    art: String,
    artChange: (String) -> Unit,
    history: String,
    historyChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 120.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_stat_name),
            contentDescription = "calc logo",
            modifier = Modifier.size(120.dp),
            contentScale = ContentScale.Fit
        )

        //math
        OutlinedTextField(
            value = math,
            onValueChange = { mathChange(it) },
            label = { Text("math:") },
            singleLine = true
        )

        //science
        OutlinedTextField(
            value = science,
            onValueChange = { scienceChange(it) },
            label = { Text("science:") },
            singleLine = true
        )

        //english
        OutlinedTextField(
            value = english,
            onValueChange = { englishChange(it) },
            label = { Text("english:") },
            singleLine = true
        )

        //art
        OutlinedTextField(
            value = art,
            onValueChange = { artChange(it) },
            label = { Text("art:") },
            singleLine = true
        )

        //history
        OutlinedTextField(
            value = history,
            onValueChange = { historyChange(it) },
            label = { Text("history:") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

    }
}


fun CalculateGPA(vararg inputs: String): Float?{
    val grades = inputs.mapNotNull {
        val value = it.toIntOrNull()
        if (value != null && value in 0..100) value else null
    }
    return if (grades.size == inputs.size) grades.average().toFloat() else null
}
