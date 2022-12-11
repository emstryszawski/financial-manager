package pl.edu.pjatk.financialmanager.persistance.dao

import android.database.Cursor
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pl.edu.pjatk.financialmanager.persistance.model.Transaction

@Dao
interface TransactionDao {
    @Query("SELECT * FROM `transaction` ORDER BY dateOfTransaction DESC")
    fun getAll(): Flow<List<Transaction>>

    @Query("SELECT * FROM `transaction` WHERE id IN (:ids)")
    fun loadAllByIds(ids: IntArray): Flow<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(transaction: Transaction): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg transaction: Transaction): List<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(transaction: Transaction): Int

    @Delete
    suspend fun delete(transaction: Transaction)

    @Query("DELETE FROM `transaction`")
    suspend fun deleteAll()

    // provider dao functions
    @Query("SELECT * FROM `transaction` ORDER BY dateOfTransaction DESC")
    fun getAllTransactionsWithCursor(): Cursor

    @Query("SELECT * FROM `transaction` WHERE id = :id")
    fun getTransactionWithCursor(id: Long): Cursor

    @Insert
    fun insertOnMainThread(transaction: Transaction): Long

    @Update
    fun updateOnMainThread(transaction: Transaction): Int

    @Query("DELETE FROM `transaction` WHERE id = :id")
    fun deleteOnMainThread(id: Long): Int
}