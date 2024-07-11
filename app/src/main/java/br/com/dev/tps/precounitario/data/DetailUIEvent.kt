package br.com.dev.tps.precounitario.data

sealed class DetailUIEvent {

    data class ProductNameChanged(val productName:String) : DetailUIEvent()
    data class QuantityChanged(val quantity:String) : DetailUIEvent()
    data class UnitTypeChanged(val unitType:String) : DetailUIEvent()
    data class TotalValueChange(val totalValue:String) : DetailUIEvent()
    data class StartWithCalculatorID(val calculatorID: String): DetailUIEvent()
    data class UpdateCalculatorButtonCliked(val calculatorID: String): DetailUIEvent()



    object StartWithOutCalculatorID: DetailUIEvent()
    object AddCalculatorButtonCliked: DetailUIEvent()
    //object RegistrationButtonClicked: DetailUIEvent()

}