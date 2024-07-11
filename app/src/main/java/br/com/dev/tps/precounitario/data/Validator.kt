package br.com.dev.tps.precounitario.data

object Validator {

    fun validateFirstName(fName: String): ValidationResult {
        return ValidationResult(
            (!fName.isNullOrEmpty() && fName.length>=6)

        )
    }
    fun validateLastName(lName: String): ValidationResult {
        return ValidationResult(
            (!lName.isNullOrEmpty() && lName.length>=4)
        )
    }
    fun validateEmail(email: String): ValidationResult {
        return ValidationResult(
            (!email.isNullOrEmpty())
        )
    }
    fun validatePassword(password: String): ValidationResult {
        return ValidationResult(
            (!password.isNullOrEmpty())
        )
    }

    fun validateprivacyPolicyAccepted(statusValue: Boolean): ValidationResult{

        return ValidationResult(statusValue)

    }

    fun validateNotNullOrEmpty(text: String?): ValidationResult {
        return ValidationResult(
            (!text.isNullOrEmpty())
        )
    }
    fun validateIsFloat(text: String?): ValidationResult {

        return ValidationResult(
            text?.toFloatOrNull() != null && text.toFloatOrNull()!! > 0
        )
    }


}

data class ValidationResult(
    val status: Boolean = false
)