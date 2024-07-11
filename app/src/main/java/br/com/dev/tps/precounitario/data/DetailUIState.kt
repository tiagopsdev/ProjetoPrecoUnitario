package br.com.dev.tps.precounitario.data

import br.com.dev.tps.precounitario.model.Calculation

data class DetailUIState(

    var productName: String = "",
    var quantity: String = "",
    var unitType:String = "",
    var totalValue:String = "",
    var unitValue:String = "",

    var productNameError: Boolean = false,
    var quantityError: Boolean = false,
    var unitTypeError: Boolean = false,
    var totalValueError: Boolean = false,

    val calculationAddedStatus: Boolean = false,
    val updateCalculationStatus: Boolean = false,
    val selectedCalculation: Calculation? = null,

    val loadingCalculation: Boolean = true


    )