package pl.edu.pjatk.financialmanager.persistance.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pl.edu.pjatk.financialmanager.persistance.model.Transaction

@Dao
interface TransactionDao {
    @Query("SELECT * FROM `transaction` ORDER BY dateOfTransaction DESC")
    fun getAll(): Flow<List<Transaction>>

    @Query("SELECT * FROM `transaction` WHERE id IN (:ids)")
    fun loadAllByIds(ids: IntArray): Flow<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(transaction: Transaction): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(vararg transaction: Transaction): List<Long>

    @Delete
    fun delete(transaction: Transaction)

    @Query("DELETE FROM `transaction`")
    fun deleteAll()
}