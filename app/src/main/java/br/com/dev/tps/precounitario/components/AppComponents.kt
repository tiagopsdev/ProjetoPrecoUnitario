package br.com.dev.tps.precounitario.components


import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.dev.tps.precounitario.R
import br.com.dev.tps.precounitario.model.Calculation
import br.com.dev.tps.precounitario.ui.theme.BgColor
import br.com.dev.tps.precounitario.ui.theme.ColorPrimary
import br.com.dev.tps.precounitario.ui.theme.ColorSecondary
import br.com.dev.tps.precounitario.ui.theme.componentsShape
import br.com.dev.tps.precounitario.utils.PUUtils

@Composable
fun NormalTextComponent(value: String){

    Text(text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(

            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center

        ),
        color = colorResource(id = R.color.colorText)


    )

}
@Composable
fun Normal2TextComponent(value: String){

    Text(text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 46.dp),
        style = TextStyle(

            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center

        ),
        color = colorResource(id = R.color.colorText)


    )

}

@Composable
fun HeadingTextComponent(value: String){

    Text(text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(),
        style = TextStyle(

            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center

        ),
        color = colorResource(id = R.color.colorText)


    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextFieldComponent(initialText: String = "", labelValue: String, imageVector: ImageVector,
                         onTextSelected: (String) -> Unit,
                         errorStatus: Boolean = true){
    

    val textValue = remember {
        mutableStateOf(initialText)
    }
    Log.d("MyTextFieldComponent", "labelValue: $labelValue : textValue.value: ${textValue.value} initialText: $initialText")
    OutlinedTextField(

        modifier = Modifier
            .fillMaxWidth()
            .clip(componentsShape.small),
        label = { Text(text = labelValue)},
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = ColorPrimary,
            focusedLabelColor = ColorPrimary,
            cursorColor = ColorPrimary,
            containerColor = BgColor



        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        singleLine = true,
        maxLines = 1,
        value = textValue.value,
        onValueChange = {
                        textValue.value = it
            onTextSelected(it)
        },
        leadingIcon = {
            Icon(imageVector = imageVector, contentDescription = "")

        },
        isError = errorStatus


        )
    
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordFieldComponent(labelValue: String, imageVector: ImageVector,
                           onTextSelected: (String) -> Unit,
                           errorStatus: Boolean = true){

    val password = remember {
        mutableStateOf("")
    }

    val isVisible = remember {
        mutableStateOf(false)
    }

    val localFocusManager = LocalFocusManager.current

    OutlinedTextField(

        modifier = Modifier
            .fillMaxWidth()
            .clip(componentsShape.small),
        label = { Text(text = labelValue)},
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = ColorPrimary,
            focusedLabelColor = ColorPrimary,
            cursorColor = ColorPrimary,
            containerColor = BgColor



        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
        singleLine = true,
        maxLines = 1,
        keyboardActions = KeyboardActions {
                                          localFocusManager.clearFocus()

        },
        value = password.value,
        onValueChange = {
            password.value = it
            onTextSelected(it)
        },
        leadingIcon = {
                      Icon(imageVector = imageVector, contentDescription = "")
        },
        trailingIcon = {
            val iconImage = if (isVisible.value){
                Icons.Default.Visibility
            }else{
                Icons.Default.VisibilityOff
            }
            val description = if (isVisible.value){
                stringResource(id = R.string.hide_password)
            }else{
                stringResource(id = R.string.show_password)

            }
            IconButton(onClick = { isVisible.value = !isVisible.value }) {
                
                Icon(imageVector = iconImage, contentDescription = description)
                
            }



        },
        visualTransformation = if(isVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        isError = errorStatus


    )

}


@Composable
fun CheckBoxComponent(onTextSelected : (String) -> Unit, onCheckedChange : (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        val checkedState = remember {
            mutableStateOf(value = false)
        }
        Checkbox(checked = checkedState.value,
            onCheckedChange = {

                checkedState.value = !checkedState.value
                onCheckedChange.invoke(it)
            })
        ClickableTextComponent(onTextSelected = onTextSelected)


    }
}


@Composable
fun ClickableTextComponent(onTextSelected : (String) -> Unit) {
    val initialText = "By continuing you accept our "
    val privacyPolicyTerm = "Privacy Policy "
    val andText = "and "
    val termsAndConditionsText = "Terms of Use"

    val annotatedString = buildAnnotatedString {
        append(initialText)
        withStyle(style = SpanStyle(color = ColorPrimary)) {
            pushStringAnnotation(
                tag = privacyPolicyTerm,
                annotation = privacyPolicyTerm
            )
            append(privacyPolicyTerm)
        }
        append(andText)
        withStyle(style = SpanStyle(color = ColorPrimary)) {
            pushStringAnnotation(
                tag = termsAndConditionsText,
                annotation = termsAndConditionsText
            )
            append(termsAndConditionsText)
        }
    }

    ClickableText(
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(offset,offset)
                .firstOrNull()?.also { span ->
                // Handle click on Privacy Policy
                    Log.d("ClickableTextComponent", "${span}")

                    if((span.item == termsAndConditionsText) || (span.item == privacyPolicyTerm)){

                        onTextSelected(span.item)

                    }

            }

        }
    )
}

@Composable
fun ButtonComponent(value: String, onClickedButton: () -> Unit, isEnabled: Boolean) {
    Button(
        onClick = {
                  onClickedButton.invoke()
        },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        enabled = isEnabled
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .background(
                    brush = Brush.horizontalGradient(listOf(ColorSecondary, ColorPrimary)),
                    shape = RoundedCornerShape(50.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold) // Add your text or other content inside the Box
        }
    }
}

@Composable
fun DividerComponent(){

    Row (modifier = Modifier
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Divider(modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
            color = Color.Gray,
            thickness = 1.dp
        )

        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(R.string.or),
            fontSize = 14.sp,
            color = colorResource(id = R.color.colorText)
        )

        Divider(modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
            color = Color.Gray,
            thickness = 1.dp
        )


    }


}

@Composable
fun ClickableTextLoginComponent(tryToLogin: Boolean = true, onTextSelected : (String) -> Unit) {
    val initialText = if (tryToLogin) stringResource(R.string.do_you_have_a_acount) else stringResource(
        R.string.doesnt_have_a_Account
    )
    val LoginText = if (tryToLogin)  stringResource(id = R.string.login) else stringResource(id = R.string.register)


    val annotatedString = buildAnnotatedString {
        append(initialText)
        append(" ")
        withStyle(style = SpanStyle(color = ColorPrimary)) {
            pushStringAnnotation(
                tag = LoginText,
                annotation = LoginText
            )
            append(LoginText)
        }
    }

    ClickableText(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(

            fontSize = 21.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center

        ),

        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(offset,offset)
                .firstOrNull()?.also { span ->
                    // Handle click on Privacy Policy
                    Log.d("ClickableTextComponent", "${span}")

                    if((span.item == LoginText)){

                        onTextSelected(span.item)

                    }

                }

        }
    )
}

@Composable
fun UnderlinedTextComponent(value: String){

    Text(text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(

            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.Underline

        ),
        color = colorResource(id = R.color.colorText)


    )

}


@OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)
@Composable
fun CalculationItem(
    calculation: Calculation,
    onLongClick: () -> Unit,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .combinedClickable(
                onLongClick = { onLongClick.invoke() },
                onClick = { onClick.invoke() }
            )
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = ColorSecondary
        )
    ) {
        val description = "Quantidade: " + calculation.quantity +
                "\nValor Total: " + calculation.totalValue +
                "\nPreço Unitario: " + String.format("%.2f",(calculation.totalValue / calculation.quantity))
        Column {
            Text(
                text = calculation.productName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                modifier = Modifier.padding(4.dp)
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                ),
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(4.dp),
                maxLines = 4
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = PUUtils.formatDate(calculation.timestamp),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                ),
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.End),
                maxLines = 1
            )
        }
    }
}