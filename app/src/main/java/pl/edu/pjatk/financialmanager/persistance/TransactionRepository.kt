package pl.edu.pjatk.financialmanager.persistance

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow


class TransactionRepository(private val transactionDao: TransactionDao) {
    val allTransactions: Flow<List<Transaction>> = transactionDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(transaction: Transaction) {
        transactionDao.insertAll(transaction)
    }
}