package br.com.dev.tps.precounitario.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.dev.tps.precounitario.R
import br.com.dev.tps.precounitario.components.ButtonComponent
import br.com.dev.tps.precounitario.components.CheckBoxComponent
import br.com.dev.tps.precounitario.components.ClickableTextLoginComponent
import br.com.dev.tps.precounitario.components.DividerComponent
import br.com.dev.tps.precounitario.components.HeadingTextComponent
import br.com.dev.tps.precounitario.components.MyTextFieldComponent
import br.com.dev.tps.precounitario.components.NormalTextComponent
import br.com.dev.tps.precounitario.components.PasswordFieldComponent
import br.com.dev.tps.precounitario.data.SignUpUIEvent
import br.com.dev.tps.precounitario.data.SignUpViewModel
import br.com.dev.tps.precounitario.navigation.PrecoUnitarioAppRouter
import br.com.dev.tps.precounitario.navigation.Screen

@Composable
fun SigninUpScreen(signUpViewModel: SignUpViewModel = viewModel()) {

    Box(modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center){

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(28.dp)

        ){


            Column(modifier = Modifier.fillMaxSize()) {

                NormalTextComponent(value = stringResource(id = R.string.hello))
                HeadingTextComponent(value = stringResource(id = R.string.create_account))
                Spacer(modifier = Modifier.height(20.dp))
                MyTextFieldComponent(
                    labelValue = stringResource(id = R.string.first_name),
                    imageVector = Icons.Default.Person,
                    onTextSelected = {

                        signUpViewModel.onEvent(SignUpUIEvent.FirstNameChanged(it))

                    },
                    errorStatus = signUpViewModel.registrationUIState.value.firstNameError
                )

                MyTextFieldComponent(
                    labelValue = stringResource(id = R.string.last_name),
                    imageVector = Icons.Default.Person,
                    onTextSelected = {
                        signUpViewModel.onEvent(SignUpUIEvent.LastNameChanged(it))
                    },
                    errorStatus = signUpViewModel.registrationUIState.value.lastNameError
                )
                MyTextFieldComponent(
                    labelValue = stringResource(id = R.string.email),
                    imageVector = Icons.Default.Mail,
                    onTextSelected = {
                        signUpViewModel.onEvent(SignUpUIEvent.EmailChanged(it))


                    },
                    errorStatus = signUpViewModel.registrationUIState.value.emailError
                )
                PasswordFieldComponent(
                    labelValue = stringResource(id = R.string.password),
                    imageVector = Icons.Default.Lock,
                    onTextSelected = {
                        signUpViewModel.onEvent(SignUpUIEvent.PasswordChange(it))

                    },
                    errorStatus = signUpViewModel.registrationUIState.value.passwordError
                )
                CheckBoxComponent(

                    onTextSelected = {
                        PrecoUnitarioAppRouter.navigatieTo(Screen.TermsAndConditions)
                    },
                    onCheckedChange = {
                        signUpViewModel.onEvent(SignUpUIEvent.PrivacyPolicyCheckboxClicked(it))
                    }
                )

                Spacer(modifier = Modifier.height(80.dp))
                ButtonComponent(stringResource(id = R.string.register),
                    onClickedButton = {
                        signUpViewModel.onEvent(SignUpUIEvent.RegistrationButtonClicked)

                    },
                    isEnabled = signUpViewModel.allValidationPassed
                )
                Spacer(modifier = Modifier.height(20.dp))
                DividerComponent()
                ClickableTextLoginComponent(
                    onTextSelected = {
                        PrecoUnitarioAppRouter.navigatieTo(Screen.LoginScreen)
                    })

            }


        }
        if (signUpViewModel.validationInProgress.value){
            CircularProgressIndicator()
        }


    }

}

@Preview
@Composable
fun DefaultPreviewOfSignUpScreen(){

    SigninUpScreen()
}