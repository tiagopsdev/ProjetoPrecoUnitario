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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.dev.tps.precounitario.R
import br.com.dev.tps.precounitario.components.ButtonComponent
import br.com.dev.tps.precounitario.components.ClickableTextLoginComponent
import br.com.dev.tps.precounitario.components.DividerComponent
import br.com.dev.tps.precounitario.components.HeadingTextComponent
import br.com.dev.tps.precounitario.components.MyTextFieldComponent
import br.com.dev.tps.precounitario.components.NormalTextComponent
import br.com.dev.tps.precounitario.components.PasswordFieldComponent
import br.com.dev.tps.precounitario.components.UnderlinedTextComponent
import br.com.dev.tps.precounitario.data.LoginUIEvent
import br.com.dev.tps.precounitario.data.LoginViewModel
import br.com.dev.tps.precounitario.navigation.PrecoUnitarioAppRouter
import br.com.dev.tps.precounitario.navigation.Screen


@Composable
fun LoginScreen(loginViewModel: LoginViewModel = LoginViewModel()){

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
                HeadingTextComponent(value = stringResource(R.string.do_your_login))
                Spacer(modifier = Modifier.height(40.dp))
                MyTextFieldComponent(
                    labelValue = stringResource(id = R.string.email),
                    imageVector = Icons.Default.Mail,
                    onTextSelected = {
                        loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
                    },
                    errorStatus = loginViewModel.loginUIState.value.emailError


                )
                PasswordFieldComponent(
                    labelValue = stringResource(id = R.string.password),
                    imageVector = Icons.Default.Lock,
                    onTextSelected = {
                                     loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it))
                    },
                    errorStatus = loginViewModel.loginUIState.value.passwordError

                )
                Spacer(modifier = Modifier.height(40.dp))
                UnderlinedTextComponent(stringResource(R.string.forgot_your_password))
                Spacer(modifier = Modifier.height(40.dp))
                ButtonComponent(value = "Login",
                    onClickedButton = {
                                      loginViewModel.onEvent(LoginUIEvent.LoginButtonClicked)
                    },
                    isEnabled = loginViewModel.allValidationPassed
                )
                Spacer(modifier = Modifier.height(20.dp))
                DividerComponent()
                ClickableTextLoginComponent(
                    tryToLogin = false,
                    onTextSelected = {
                        PrecoUnitarioAppRouter.navigatieTo(Screen.SignUpScreen)
                    })

            }


        }
        if (loginViewModel.loginInProgress.value){
            CircularProgressIndicator()
        }
    }


}

@Preview
@Composable
fun DefaultPreviewOfLoginScreen(){

    LoginScreen()
}