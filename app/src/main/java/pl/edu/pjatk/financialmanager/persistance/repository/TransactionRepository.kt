package pl.edu.pjatk.financialmanager.persistance.repository

import android.database.Cursor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import pl.edu.pjatk.financialmanager.persistance.dao.TransactionDao
import pl.edu.pjatk.financialmanager.persistance.model.Transaction
import java.time.LocalDateTime


class TransactionRepository(private val transactionDao: TransactionDao) {
    val allTransactions: Flow<List<Transaction>> = transactionDao.getAll()

    fun getTransactionsInInterval(
        startDate: LocalDateTime,
        endTime: LocalDateTime
    ): List<Transaction> = transactionDao.getTransactionsInInterval(startDate, endTime)

    suspend fun insert(transaction: Transaction): Long = withContext(Dispatchers.IO) {
        return@withContext transactionDao.insert(transaction)
    }

    suspend fun update(transaction: Transaction): Int = withContext(Dispatchers.IO) {
        return@withContext transactionDao.update(transaction)
    }

    suspend fun delete(transaction: Transaction) {
        transactionDao.delete(transaction)
    }

    val allTransactionsCursor: Cursor = transactionDao.getAllTransactionsWithCursor()

    fun transactionCursor(id: Long): Cursor = transactionDao.getTransactionWithCursor(id)

    fun insertOnMainThread(transaction: Transaction): Long =
        transactionDao.insertOnMainThread(transaction)

    fun updateOnMainThread(transaction: Transaction): Int =
        transactionDao.updateOnMainThread(transaction)

    fun deleteOnMainThread(id: Long): Int =
        transactionDao.deleteOnMainThread(id)
}