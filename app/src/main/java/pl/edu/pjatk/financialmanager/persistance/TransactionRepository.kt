package pl.edu.pjatk.financialmanager.persistance

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


class TransactionRepository(private val transactionDao: TransactionDao) {
    val allTransactions: Flow<List<Transaction>> = transactionDao.getAll()

    suspend fun insert(transaction: Transaction) = withContext(Dispatchers.IO) {
        transactionDao.insert(transaction)
    }
}