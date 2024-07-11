package br.com.dev.tps.precounitario.repository

import br.com.dev.tps.precounitario.model.Calculation
import br.com.dev.tps.precounitario.utils.PUUtils
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

const val CALCULATION_COLLECTION_REF = "calculation"


class StorageRepository {


    fun user() = Firebase.auth.currentUser
    fun hasUser():Boolean =  Firebase.auth.currentUser != null
    fun getUserId():String = Firebase.auth.currentUser?.uid.orEmpty()

    val calculationId: CollectionReference =
        Firebase.firestore.collection(CALCULATION_COLLECTION_REF)


    fun getUserCalculations(
        userId: String,
    ): Flow<Resources<List<Calculation>>> = callbackFlow {
        var snapshotStateListener: ListenerRegistration? = null

        try {
            snapshotStateListener = calculationId
                //.orderBy("productName")
                .whereEqualTo("userId", userId)
                .addSnapshotListener { snapshot, e ->
                    val response = if (snapshot != null) {
                        val calculation = try {
                            snapshot.toObjects(Calculation::class.java)
                        } catch (e: Exception) {
                            //
                            snapshot.documents.mapNotNull { doc ->
                                try {
                                    PUUtils.fromDocument(doc)
                                } catch (e: Exception) {
                                    null
                                }
                            }
                            //
                        }
                        Resources.Success(data = calculation)
                    } else {
                        Resources.Error(throwable = e?.cause)
                    }
                    trySend(response)

                }


        } catch (e: Exception) {
            trySend(Resources.Error(e.cause))
            e.printStackTrace()
        }

        awaitClose {
            snapshotStateListener?.remove()
        }


    }
    fun getCalculation(
        calculationId:String,
        onError:(Throwable?) -> Unit,
        onSuccess: (Calculation?) -> Unit
    ){
        this.calculationId
            .document(calculationId)
            .get()
            .addOnSuccessListener {
                onSuccess.invoke(it?.toObject(Calculation::class.java))
            }
            .addOnFailureListener {result ->
                onError.invoke(result.cause)
            }


    }

    fun addCalculation(
        userId: String,
        productName: String,
        quantity: Double,
        unitType:String,
        totalValue:Double,
        timestamp: Timestamp,
        onComplete: (Boolean) -> Unit,
    ){
        val documentId = calculationId.document().id
        val calculo = Calculation(
            userId,
            productName,
            quantity,
            unitType,
            totalValue,
            timestamp,
            documentId

            )

        this.calculationId
            .document(documentId)
            .set(calculo)
            .addOnCompleteListener { result ->
                onComplete.invoke(result.isSuccessful)
            }


    }
    fun deleteCalculation(calculationID: String, onComplete: (Boolean) -> Unit){
        calculationId.document(calculationID)
            .delete()
            .addOnCompleteListener {
                onComplete.invoke(it.isSuccessful)
            }
    }

    fun updateCalculation(
        calculationId: String,
        productName: String,
        quantity: Double,
        unitType:String,
        totalValue:Double,
        onResult:(Boolean) -> Unit
    ){
        val updateData = hashMapOf<String,Any>(
            "productName" to productName,
            "quantity" to quantity,
            "unitType" to unitType,
            "totalValue" to totalValue,

            )

        this.calculationId.document(calculationId)
            .update(updateData)
            .addOnCompleteListener {
                onResult(it.isSuccessful)
            }



    }
    fun signOut() = Firebase.auth.signOut()





}
sealed class Resources<T>(
    val data: T? = null,
    val throwable: Throwable? = null

){
    class Loading<T>: Resources<T>()
    class Success<T>(data: T?): Resources<T>(data = data)
    class Error<T>(throwable: Throwable?) : Resources<T>(throwable = throwable)
}