package com.hw_wk4.gpacalcapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.hw_wk4.gpacalcapp.ui.theme.*

private val CustomColorScheme = darkColorScheme(
    primary = light_grey,
    onPrimary = black,
    background = dark_grey,
    onBackground = white,
    secondary = soft_red,
    onSecondary = light_grey
)

@Composable
fun GPACalcAppTheme(content: @Composable () -> Unit){
    MaterialTheme(
        colorScheme = CustomColorScheme,
        typography = Typography,
        content = content
    )
}