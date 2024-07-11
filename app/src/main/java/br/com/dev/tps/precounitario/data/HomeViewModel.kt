package br.com.dev.tps.precounitario.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.dev.tps.precounitario.repository.Resources
import br.com.dev.tps.precounitario.repository.StorageRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: StorageRepository = StorageRepository(),
) : ViewModel() {

    val TAG = HomeViewModel::class.simpleName

    var homeUiState = mutableStateOf(HomeUiState())

    val user: FirebaseUser?
        get() = repository.user()

    val hasUser: Boolean
        get() = repository.hasUser()

    val userId: String
        get() = repository.getUserId()

    fun loadCalculations(){
        Log.d(TAG, "hasUser: $hasUser")
        Log.d(TAG, "userId: $userId")
        Log.d("MyFirebase", "${TAG}Firebase.auth.currentUser?.uid: ${Firebase.auth.currentUser?.uid.toString()}")
        if (hasUser){
            if (userId.isNotBlank()){
                getUserNotes(userId)
            }
        }else{
            homeUiState.value = homeUiState.value.copy(calculationList = Resources.Error(
                throwable = Throwable(message = "User is not Login")
            ))
        }
    }

    private fun getUserNotes(userId:String) = viewModelScope.launch {
        repository.getUserCalculations(userId).collect {
            homeUiState.value = homeUiState.value.copy(calculationList = it)
        }
    }

    fun deleteNote(noteId:String) = repository.deleteCalculation(noteId){
        homeUiState.value = homeUiState.value.copy(calculationDeletedStatus = it)
    }

    fun signOut() = repository.signOut()

}