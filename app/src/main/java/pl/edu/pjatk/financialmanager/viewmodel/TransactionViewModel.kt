package pl.edu.pjatk.financialmanager.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import pl.edu.pjatk.financialmanager.FinancialManagerApplication
import pl.edu.pjatk.financialmanager.persistance.model.Transaction
import pl.edu.pjatk.financialmanager.persistance.repository.TransactionRepository

class TransactionViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    val allTransactions: LiveData<List<Transaction>> = repository.allTransactions.asLiveData()

    fun addNewTransaction(transaction: Transaction): LiveData<Long> {
        val liveData = MutableLiveData<Long>()
        viewModelScope.launch {
            liveData.postValue(repository.insert(transaction))
        }
        return liveData
    }

    fun updateTransaction(transaction: Transaction): LiveData<Int> {
        val liveData = MutableLiveData<Int>()
        viewModelScope.launch {
            liveData.postValue(repository.update(transaction))
        }
        return liveData
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.delete(transaction)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return TransactionViewModel(
                    (application as FinancialManagerApplication).repository
                ) as T
            }
        }
    }
}
