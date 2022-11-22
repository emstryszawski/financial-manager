package pl.edu.pjatk.financialmanager.persistance

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM `transaction` ORDER BY date DESC")
    fun getAll(): Flow<List<Transaction>>

    @Query("SELECT * FROM `transaction` WHERE id IN (:ids)")
    fun loadAllByIds(ids: IntArray): Flow<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg transaction: Transaction)

    @Delete
    fun delete(transaction: Transaction)

    @Query("DELETE FROM `transaction`")
    fun deleteAll()
}