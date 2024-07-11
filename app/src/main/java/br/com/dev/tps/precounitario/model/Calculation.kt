package br.com.dev.tps.precounitario.model

import com.google.firebase.Timestamp

data class Calculation(

    val userId: String = "",
    var productName: String = "",
    var quantity: Double = 0.0,
    var unitType:String = "",
    var totalValue:Double = 0.0,
    var timestamp: Timestamp = Timestamp.now(),
    val calcutationId: String = "",



    )
