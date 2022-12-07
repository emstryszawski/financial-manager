package pl.edu.pjatk.financialmanager.persistance.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import pl.edu.pjatk.financialmanager.persistance.dao.TransactionDao
import pl.edu.pjatk.financialmanager.persistance.model.Transaction


class TransactionRepository(private val transactionDao: TransactionDao) {
    val allTransactions: Flow<List<Transaction>> = transactionDao.getAll()

    suspend fun insert(transaction: Transaction): Long = withContext(Dispatchers.IO) {
        return@withContext transactionDao.insert(transaction)
    }

    suspend fun update(transaction: Transaction): Int = withContext(Dispatchers.IO) {
        return@withContext transactionDao.update(transaction)
    }
}