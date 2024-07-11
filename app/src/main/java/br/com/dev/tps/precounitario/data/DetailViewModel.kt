package br.com.dev.tps.precounitario.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.dev.tps.precounitario.model.Calculation
import br.com.dev.tps.precounitario.navigation.PrecoUnitarioAppRouter
import br.com.dev.tps.precounitario.navigation.Screen
import br.com.dev.tps.precounitario.repository.StorageRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: StorageRepository = StorageRepository()
) : ViewModel() {

    val TAG = DetailViewModel::class.simpleName

    private val _detailUiState = MutableStateFlow(DetailUIState())
    val detailUiState: StateFlow<DetailUIState> = _detailUiState

    private val hasUser: Boolean
        get() = repository.hasUser()

    private val user: FirebaseUser?
        get() = repository.user()

    var allValidationPassed = false

    fun onEvent(event: DetailUIEvent) {
        when (event) {
            is DetailUIEvent.ProductNameChanged -> {
                _detailUiState.value = _detailUiState.value.copy(
                    productName = event.productName
                )
                printState()
            }
            is DetailUIEvent.QuantityChanged -> {
                val unitValueCalculation = unitValue(event.quantity, _detailUiState.value.totalValue)
                _detailUiState.value = _detailUiState.value.copy(
                    quantity = event.quantity,
                    unitValue = unitValueCalculation
                )
                printState()
            }
            is DetailUIEvent.TotalValueChange -> {
                val unitValueCalculation = unitValue(_detailUiState.value.quantity, event.totalValue)
                _detailUiState.value = _detailUiState.value.copy(
                    totalValue = event.totalValue,
                    unitValue = unitValueCalculation
                )
                printState()
            }
            is DetailUIEvent.UnitTypeChanged -> {
                _detailUiState.value = _detailUiState.value.copy(
                    unitType = event.unitType
                )
                printState()
            }
            is DetailUIEvent.StartWithCalculatorID -> {
                getCalculation(event.calculatorID)
            }
            is DetailUIEvent.StartWithOutCalculatorID -> {
                resetState()
            }
            is DetailUIEvent.AddCalculatorButtonCliked -> {
                addCalculation()
            }
            is DetailUIEvent.UpdateCalculatorButtonCliked -> {
                updateCalculation(event.calculatorID)
            }

        }
        validateDataWithRules()
    }

    private fun unitValue(quantity: String?, totalValue: String?): String {
        if (quantity.isNullOrEmpty() || totalValue.isNullOrEmpty()) {
            return ""
        }
        if (!Validator.validateIsFloat(quantity).status) {
            return "Quantidade Inválida"
        }
        if (!Validator.validateIsFloat(totalValue).status) {
            return "Valor Total Inválido"
        }
        val result = (totalValue.toFloat()) / (quantity.toFloat())
        return String.format("%.2f", result)
    }

    private fun printState() {
        Log.d("DetailViewModel", _detailUiState.value.toString())
    }

    private fun validateDataWithRules() {
        val productNameResult = Validator.validateNotNullOrEmpty(
            text = _detailUiState.value.productName
        )
        val quantityResult = Validator.validateNotNullOrEmpty(
            text = _detailUiState.value.quantity
        )
        val unitTypeResult = Validator.validateNotNullOrEmpty(
            text = _detailUiState.value.unitType
        )
        val totalValueResult = Validator.validateIsFloat(
            text = _detailUiState.value.totalValue
        )

        _detailUiState.value = _detailUiState.value.copy(
            productNameError = !productNameResult.status,
            quantityError = !quantityResult.status,
            unitTypeError = !unitTypeResult.status,
            totalValueError = !totalValueResult.status
        )

        allValidationPassed = productNameResult.status && quantityResult.status &&
                unitTypeResult.status && totalValueResult.status
    }

    fun addCalculation() {
        if (hasUser) {
            viewModelScope.launch {
                repository.addCalculation(
                    userId = user!!.uid,
                    productName = _detailUiState.value.productName,
                    quantity = _detailUiState.value.quantity.toDouble(),
                    unitType = _detailUiState.value.unitType,
                    totalValue = _detailUiState.value.totalValue.toDouble(),
                    timestamp = Timestamp.now()
                ) {
                    if (it) {
                        PrecoUnitarioAppRouter.navigatieTo(Screen.HomeScreen())
                    }
                }
            }
        }
    }

    fun setEditFields(calculation: Calculation) {
        Log.d(TAG, "Setting edit fields: $calculation")
        _detailUiState.value = _detailUiState.value.copy(
            productName = calculation.productName,
            quantity = calculation.quantity.toString(),
            unitType = calculation.unitType,
            totalValue = calculation.totalValue.toString(),
            unitValue = unitValue(calculation.quantity.toString(), calculation.totalValue.toString())
        )

    }

    fun getCalculation(calculationId: String) {
        _detailUiState.value = _detailUiState.value.copy(loadingCalculation = true)
        repository.getCalculation(
            calculationId = calculationId,
            onError = {
                Log.d(TAG, "Error retrieving calculation: $calculationId")
                _detailUiState.value = _detailUiState.value.copy(loadingCalculation = false)
            },
        ) {
            Log.d(TAG, "Retrieved calculation: $it")
            validateDataWithRules()
            _detailUiState.value = _detailUiState.value.copy(
                selectedCalculation = it,
                loadingCalculation = false
            )
            _detailUiState.value.selectedCalculation?.let { calc -> setEditFields(calc) }
        }
    }

    fun updateCalculation(calculationId: String) {
        viewModelScope.launch {
            repository.updateCalculation(
                calculationId = calculationId,
                productName = _detailUiState.value.productName,
                quantity = _detailUiState.value.quantity.toDouble(),
                unitType = _detailUiState.value.unitType,
                totalValue = _detailUiState.value.totalValue.toDouble(),
            ) { status ->
                _detailUiState.value = _detailUiState.value.copy(updateCalculationStatus = status)
            }
        }
    }

    fun resetCalculationAddedStatus() {
        _detailUiState.value = _detailUiState.value.copy(
            calculationAddedStatus = false,
            updateCalculationStatus = false,
        )
    }

    fun resetState() {
        _detailUiState.value = DetailUIState(loadingCalculation = false)
    }
}
