package pl.edu.pjatk.financialmanager

import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.*
import pl.edu.pjatk.financialmanager.persistance.Transaction
import pl.edu.pjatk.financialmanager.persistance.TransactionRepository

class TransactionListViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    val allTransactions: LiveData<List<Transaction>> = repository.allTransactions.asLiveData()

//    fun addNewTransaction(transaction: Transaction): Long? {
//        val liveData = MutableLiveData<Long>()
//        viewModelScope.launch(Dispatchers.IO) {
//            val id = repository.insert(transaction)
//            Log.d("viewModel", "id0: $id")
//            liveData.postValue(id)
//            Log.d("viewModel", "id1: " + liveData.value)
//        }
//        Log.d("viewModel", "id2: " + liveData.value)
//        return liveData.value
//    }

    fun addNewTransaction(transaction: Transaction): LiveData<Long> {
        val liveData = MutableLiveData<Long>()
        viewModelScope.launch {
            liveData.value = repository.insert(transaction)
        }
        return liveData;
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return TransactionListViewModel(
                    (application as FinancialManagerApplication).repository
                ) as T
            }
        }
    }
}
