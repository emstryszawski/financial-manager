package pl.edu.pjatk.financialmanager

import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.launch
import pl.edu.pjatk.financialmanager.persistance.Transaction
import pl.edu.pjatk.financialmanager.persistance.TransactionRepository

class TransactionListViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    val allTransactions: LiveData<List<Transaction>> = repository.allTransactions.asLiveData()

    // TODO
    fun insert(transaction: Transaction) = viewModelScope.launch {
        repository.insert(transaction)
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
