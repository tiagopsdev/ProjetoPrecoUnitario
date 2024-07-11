package br.com.dev.tps.precounitario.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import br.com.dev.tps.precounitario.data.DetailViewModel
import br.com.dev.tps.precounitario.data.HomeViewModel


sealed class Screen {

    object SignUpScreen : Screen()
    object LoginScreen : Screen()
    object TermsAndConditions : Screen()
    data class HomeScreen(val homeViewModel: HomeViewModel = HomeViewModel()) : Screen()
    data class DetailScreen(val calcId: String = "", val detailViewModel: DetailViewModel = DetailViewModel()) : Screen()



}

object PrecoUnitarioAppRouter {

    var currentScreen: MutableState<Screen> = mutableStateOf(Screen.SignUpScreen)
    fun navigatieTo(destination: Screen){

        currentScreen.value = destination

    }
}