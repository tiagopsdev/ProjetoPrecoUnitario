package br.com.dev.tps.precounitario.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import br.com.dev.tps.precounitario.navigation.PrecoUnitarioAppRouter
import br.com.dev.tps.precounitario.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginViewModel() : ViewModel()  {

    var allValidationPassed = false
    val loginUIState = mutableStateOf(LoginUIState())
    val TAG = LoginViewModel::class.simpleName
    var loginInProgress = mutableStateOf(false)

    fun onEvent(event: LoginUIEvent){

        when(event){

            is LoginUIEvent.EmailChanged -> {

                loginUIState.value = loginUIState.value.copy(
                    email = event.email
                )

            }
            is LoginUIEvent.PasswordChanged -> {

                loginUIState.value = loginUIState.value.copy(
                    password = event.password
                )
            }
            is LoginUIEvent.LoginButtonClicked -> {
                login()
            }


        }
        validateDataWithRules()

    }

    private fun login() {

        val email = loginUIState.value.email
        val password = loginUIState.value.password
        loginInProgress.value = true
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.d(TAG, "Inside_LoginSuccess")
                Log.d(TAG, "${it.isSuccessful}")
                Log.d("MyFirebase", "${TAG}Firebase.auth.currentUser?.uid: ${Firebase.auth.currentUser?.uid.toString()}")
                loginInProgress.value = false
                PrecoUnitarioAppRouter.navigatieTo(Screen.HomeScreen())

            }
            .addOnFailureListener {
                Log.d(TAG, "Inside_LoginFailure")
                Log.d(TAG, "${it.message}")
                Log.d(TAG, "${it.localizedMessage}")
                loginInProgress.value = false
            }

    }

    private fun validateDataWithRules() {

        val emailResult = Validator.validateEmail(
            email = loginUIState.value.email
        )

        val passwordResult = Validator.validatePassword(
            password = loginUIState.value.password
        )

        Log.d(TAG, "Inside_validateDataWithRules")
        Log.d(TAG, "emailResult = $emailResult")
        Log.d(TAG, "passwordResult = $passwordResult")


        /* Errors */

        loginUIState.value = loginUIState.value.copy(

            emailError = !emailResult.status,
            passwordError = !passwordResult.status,

            )

        allValidationPassed =
            emailResult.status && passwordResult.status
    }


}