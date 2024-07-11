package br.com.dev.tps.precounitario.utils

import br.com.dev.tps.precounitario.model.Calculation
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import java.text.SimpleDateFormat
import java.util.Locale

object PUUtils {

    fun formatDate(timestamp: Timestamp): String {
        val sdf = SimpleDateFormat("MM-dd-yy hh:mm", Locale.getDefault())
        return sdf.format(timestamp.toDate())
    }

    fun fromDocument(doc: DocumentSnapshot): Calculation {
        val userId = doc.getString("userId") ?: ""
        val productName = doc.getString("productName") ?: ""
        val quantity = doc.getDouble("quantity") ?: 0.0
        val unitType = doc.getString("unitType") ?: ""
        val totalValue = doc.getDouble("totalValue") ?: 0.0
        val timestamp = doc.getTimestamp("timestamp") ?: Timestamp.now()
        val calcutationId = doc.getString("calcutationId") ?: ""

        return Calculation(
            userId = userId,
            productName = productName,
            quantity = quantity,
            unitType = unitType,
            totalValue = totalValue,
            timestamp = timestamp,
            calcutationId = calcutationId
        )
    }

}