package pl.edu.pjatk.financialmanager.persistance

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@Database(entities = [Transaction::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FinancialManagerDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: FinancialManagerDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): FinancialManagerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FinancialManagerDatabase::class.java,
                    "financial_manager_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(FinancialManagerDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    protected class FinancialManagerDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.transactionDao())
                }
            }
        }

        private fun populateDatabase(transactionDao: TransactionDao) {
            transactionDao.deleteAll()
            scope.launch {
                transactionDao.insertAll(
                    Transaction("Żabka", BigDecimal("21.19"), "Żywność", LocalDate.now(), 1),
                    Transaction("Biedronka", BigDecimal("130.99"), "Żywność", LocalDate.now(), 2),
                    Transaction("Myjnia", BigDecimal("30.00"), "Samochód", LocalDate.now(), 3),
                    Transaction("Paliwo", BigDecimal("-300.12"), "Samochód", LocalDate.now(), 4),
                    Transaction("ITN", BigDecimal("900.00"), "Uczelnia", LocalDate.now(), 5)
                )
            }
        }
    }

}