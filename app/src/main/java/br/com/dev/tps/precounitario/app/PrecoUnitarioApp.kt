package br.com.dev.tps.precounitario.app


import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import br.com.dev.tps.precounitario.navigation.PrecoUnitarioAppRouter
import br.com.dev.tps.precounitario.navigation.Screen
import br.com.dev.tps.precounitario.screens.DetailScreen
import br.com.dev.tps.precounitario.screens.HomeScreen
import br.com.dev.tps.precounitario.screens.LoginScreen
import br.com.dev.tps.precounitario.screens.SigninUpScreen
import br.com.dev.tps.precounitario.screens.TermAndUseScreen

@Composable
fun PrecoUnitarioApp(){

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Crossfade(targetState = PrecoUnitarioAppRouter.currentScreen, label = "") { currentState ->

            when(currentState.value){

                is Screen.SignUpScreen -> {
                    SigninUpScreen()
                }
                is Screen.TermsAndConditions -> {
                    TermAndUseScreen()
                }
                is Screen.LoginScreen -> {
                    LoginScreen()
                }
                is Screen.HomeScreen -> {

                    HomeScreen(
                        homeViewModel = (currentState.value as Screen.HomeScreen).homeViewModel
                    )
                }

                is Screen.DetailScreen -> {
                    DetailScreen(
                        calculatorId = (currentState.value as Screen.DetailScreen).calcId)
                }


            }

        }
    }

}