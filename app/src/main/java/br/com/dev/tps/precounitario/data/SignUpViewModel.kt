package br.com.dev.tps.precounitario.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import br.com.dev.tps.precounitario.navigation.PrecoUnitarioAppRouter
import br.com.dev.tps.precounitario.navigation.Screen
import br.com.dev.tps.precounitario.navigation.Screen.HomeScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener


class SignUpViewModel: ViewModel() {

    var allValidationPassed = false
    val registrationUIState = mutableStateOf(RegisterUIState())
    val TAG = SignUpViewModel::class.simpleName
    var validationInProgress = mutableStateOf(false)

    fun onEvent(event: SignUpUIEvent){


        when(event){

            is SignUpUIEvent.FirstNameChanged -> {

                registrationUIState.value = registrationUIState.value.copy(
                    firstName = event.firstName
                )
                printState()

            }
            is SignUpUIEvent.LastNameChanged -> {

                registrationUIState.value = registrationUIState.value.copy(
                    lastName = event.lastName
                )
                printState()

            }
            is SignUpUIEvent.EmailChanged -> {

                registrationUIState.value = registrationUIState.value.copy(
                    email = event.email
                )
                printState()

            }
            is SignUpUIEvent.PasswordChange -> {

                registrationUIState.value = registrationUIState.value.copy(
                    password = event.password
                )
                printState()

            }

            is SignUpUIEvent.PrivacyPolicyCheckboxClicked ->{

                registrationUIState.value = registrationUIState.value.copy(
                    privacyPolicyAccepted = event.status
                )

            }

            is SignUpUIEvent.RegistrationButtonClicked -> {

                signUp()

            }


        }
        validateDataWithRules()

    }

    private fun signUp() {
        Log.d(TAG, "Inside_signUp")
        printState()

        createUserInFireBase(
            email = registrationUIState.value.email,
            password = registrationUIState.value.password
        )


    }

    private fun validateDataWithRules() {



        val fNameResult = Validator.validateFirstName(
            fName = registrationUIState.value.firstName
        )

        val lNameResult = Validator.validateLastName(
            lName = registrationUIState.value.lastName
        )

        val emailResult = Validator.validateEmail(
            email = registrationUIState.value.email
        )

        val passwordResult = Validator.validatePassword(
            password = registrationUIState.value.password
        )
        val privacePolicyResult = Validator.validateprivacyPolicyAccepted(
            statusValue = registrationUIState.value.privacyPolicyAccepted
        )
        Log.d(TAG, "Inside_validateDataWithRules")
        Log.d(TAG, "fNameResult = $fNameResult")
        Log.d(TAG, "lNameResult = $lNameResult")
        Log.d(TAG, "emailResult = $emailResult")
        Log.d(TAG, "passwordResult = $passwordResult")
        Log.d(TAG, "privacePolicyResult = $privacePolicyResult")

        /* Errors */

        registrationUIState.value = registrationUIState.value.copy(

            firstNameError = !fNameResult.status,
            lastNameError = !lNameResult.status,
            emailError = !emailResult.status,
            passwordError = !passwordResult.status,
            privacyError = !privacePolicyResult.status,

        )

        allValidationPassed = fNameResult.status && lNameResult.status &&
                emailResult.status && passwordResult.status &&
                privacePolicyResult.status
    }

    fun createUserInFireBase(email: String, password: String){

        validationInProgress.value = true
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {

                Log.d(TAG, "Inside_addOnCompleteListener")
                Log.d(TAG, "isSuccessful = ${it.isSuccessful}")
                if (it.isSuccessful){

                    validationInProgress.value = false
                    PrecoUnitarioAppRouter.navigatieTo(HomeScreen())

                }


            }.addOnFailureListener {

                validationInProgress.value = false
                Log.d(TAG, "Inside_addOnFailureListener")
                Log.d(TAG, "Exception = ${it.message}")
                Log.d(TAG, "Exception = ${it.localizedMessage}")

            }



    }

    private fun printState(){
        Log.d(TAG, "Inside_PrintState")
        Log.d(TAG, registrationUIState.value.toString())
    }

    fun signout() {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()

        val authStateListener = AuthStateListener {
            if (it.currentUser == null) {

                Log.d(TAG, "Inside sigh out Success")
                PrecoUnitarioAppRouter.navigatieTo(Screen.LoginScreen)

            } else {

                Log.d(TAG, "Inside sigh out is note Complete")

            }


        }

        firebaseAuth.addAuthStateListener(authStateListener)
    }

}