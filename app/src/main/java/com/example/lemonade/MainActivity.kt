package com.example.lemonade

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lemonade.ui.theme.LemonadeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LemonadeApp()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun LemonadeApp() {
    LemonadeTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            LemonadeScreen()
        }
    }
}

enum class LemonadeAppState {
    SelectLemon,
    SqueezeLemon,
    DrinkLemonade,
    NoLemonadeLeft;

    fun nextState(): LemonadeAppState {
        return when (this) {
            SelectLemon -> SqueezeLemon
            SqueezeLemon -> DrinkLemonade
            DrinkLemonade -> NoLemonadeLeft
            NoLemonadeLeft -> SelectLemon
        }
    }
}

@Composable
fun LemonadeScreen() {
    var appState by remember { mutableStateOf(LemonadeAppState.SelectLemon) }
    var squeezeCount by remember { mutableStateOf (0)}

    val infoStringResId = when (appState) {
        LemonadeAppState.SelectLemon -> R.string.select_lemon
        LemonadeAppState.SqueezeLemon -> R.string.squeeze_lemon
        LemonadeAppState.DrinkLemonade -> R.string.drink_lemonade
        LemonadeAppState.NoLemonadeLeft -> R.string.start_again
    }
    val imageResId = when (appState) {
        LemonadeAppState.SelectLemon -> R.drawable.lemon_tree
        LemonadeAppState.SqueezeLemon -> R.drawable.lemon_squeeze
        LemonadeAppState.DrinkLemonade -> R.drawable.lemon_drink
        LemonadeAppState.NoLemonadeLeft -> R.drawable.lemon_restart
    }
    val contentDescriptionResId = when (appState) {
        LemonadeAppState.SelectLemon -> R.string.lemon_tree_content_description
        LemonadeAppState.SqueezeLemon -> R.string.lemon_content_description
        LemonadeAppState.DrinkLemonade -> R.string.glass_of_lemonade_content_description
        LemonadeAppState.NoLemonadeLeft -> R.string.empty_glass_content_description
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val image = painterResource(id = imageResId)
        Text(text = stringResource(id = infoStringResId), fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (appState == LemonadeAppState.SelectLemon) {
                    /*
                    * We are about to squeeze the lemon
                    * Set the number of times the lemon should be squeezed
                    * before transitioning to that state.
                    */
                    squeezeCount = (2..6).random()
                    appState = appState.nextState()
                } else if (appState == LemonadeAppState.SqueezeLemon) {
                    /*
                     * Transition to the next state only when the lemon was squeezed
                     * [squeezeCount] times
                     * */
                    squeezeCount--
                    if (squeezeCount == 0) {
                        appState = appState.nextState()
                    }
                } else {
                    appState = appState.nextState()
                }
            },
            elevation = null
        ) {
            Image(
                painter = image,
                contentDescription = stringResource(id = contentDescriptionResId),
                modifier = Modifier.border(
                    width = 1.dp,
                    color = Color(red = 105, green = 205, blue = 216),
                    shape = RoundedCornerShape(3),
                )
            )
        }
    }
}