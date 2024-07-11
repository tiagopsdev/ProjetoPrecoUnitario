package br.com.dev.tps.precounitario.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.dev.tps.precounitario.R
import br.com.dev.tps.precounitario.components.HeadingTextComponent
import br.com.dev.tps.precounitario.components.MyTextFieldComponent
import br.com.dev.tps.precounitario.components.NormalTextComponent
import br.com.dev.tps.precounitario.data.DetailUIEvent
import br.com.dev.tps.precounitario.data.DetailViewModel
import br.com.dev.tps.precounitario.navigation.PrecoUnitarioAppRouter
import br.com.dev.tps.precounitario.navigation.Screen
import br.com.dev.tps.precounitario.navigation.SystemBackButtonHandler
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    detailViewModel: DetailViewModel = hiltViewModel(),
    calculatorId: String
) {
    val detailUiState by detailViewModel.detailUiState.collectAsState()

    val isFormsNotBlank: Boolean = detailUiState.productName.isNotBlank() &&
            detailUiState.quantity.isNotBlank() &&
            detailUiState.unitType.isNotBlank() &&
            detailUiState.totalValue.isNotBlank()

    val isCalculatorIdNotBlank = calculatorId.isNotBlank()
    val icon = if (isCalculatorIdNotBlank) Icons.Default.Refresh else Icons.Default.Check

    LaunchedEffect(key1 = calculatorId) {
        if (isCalculatorIdNotBlank) {
            Log.d("DetailScreen", "Starting with calculator ID: $calculatorId")
            detailViewModel.onEvent(DetailUIEvent.StartWithCalculatorID(calculatorId))


        } else {
            Log.d("DetailScreen", "Starting without calculator ID")
            detailViewModel.onEvent(DetailUIEvent.StartWithOutCalculatorID)
        }
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            //AnimatedVisibility(visible = isFormsNotBlank) {
            if(isFormsNotBlank) {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            val message = if (isCalculatorIdNotBlank) {
                                detailViewModel.updateCalculation(calculatorId)
                                "Calculation updated!"
                            } else {
                                detailViewModel.addCalculation()
                                "Calculation added!"
                            }
                            snackbarHostState.showSnackbar(message)
                            PrecoUnitarioAppRouter.navigatieTo(Screen.HomeScreen())
                        }
                    }
                ) {
                    Icon(imageVector = icon, contentDescription = null)
                }
            }
            //}
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (detailUiState.loadingCalculation && isCalculatorIdNotBlank) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    HeadingTextComponent(value = stringResource(R.string.unit_price))
                    Spacer(modifier = Modifier.height(20.dp))
                    NormalTextComponent(value = "R$: ${detailUiState.unitValue}")
                    Spacer(modifier = Modifier.height(20.dp))
                    MyTextFieldComponent(

                        initialText = detailUiState.productName,
                        labelValue = stringResource(R.string.product_name),
                        imageVector = Icons.Default.ShoppingBasket,
                        onTextSelected = {
                            detailViewModel.onEvent(DetailUIEvent.ProductNameChanged(it))
                        },
                        errorStatus = detailUiState.productNameError
                    )
                    MyTextFieldComponent(
                        initialText = detailUiState.quantity,
                        labelValue = stringResource(R.string.quantity),
                        imageVector = Icons.Default.HorizontalRule,
                        onTextSelected = {
                            detailViewModel.onEvent(DetailUIEvent.QuantityChanged(it))
                        },
                        errorStatus = detailUiState.quantityError
                    )
                    MyTextFieldComponent(
                        initialText = detailUiState.unitType,
                        labelValue = "Unidade",
                        imageVector = Icons.Default.Scale,
                        onTextSelected = {
                            detailViewModel.onEvent(DetailUIEvent.UnitTypeChanged(it))
                        },
                        errorStatus = detailUiState.unitTypeError
                    )
                    MyTextFieldComponent(
                        initialText = detailUiState.totalValue,
                        labelValue = "Valor Total",
                        imageVector = Icons.Default.AttachMoney,
                        onTextSelected = {
                            detailViewModel.onEvent(DetailUIEvent.TotalValueChange(it))
                        },
                        errorStatus = detailUiState.totalValueError
                    )

                }

            }
        }
    }

    SystemBackButtonHandler {
        PrecoUnitarioAppRouter.navigatieTo(Screen.HomeScreen())
    }
}

@Preview
@Composable
fun PrevDetailScreen() {
    DetailScreen(calculatorId = "")
}
