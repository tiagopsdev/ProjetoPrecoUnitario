package br.com.dev.tps.precounitario.data

sealed class SignUpUIEvent {

    data class FirstNameChanged(val firstName:String) : SignUpUIEvent()
    data class LastNameChanged(val lastName:String) : SignUpUIEvent()
    data class EmailChanged(val email:String) : SignUpUIEvent()
    data class PasswordChange(val password:String) : SignUpUIEvent()

    data class PrivacyPolicyCheckboxClicked(val status:Boolean) : SignUpUIEvent()

    object RegistrationButtonClicked: SignUpUIEvent()




}