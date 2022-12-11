package pl.edu.pjatk.financialmanager.persistance

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pl.edu.pjatk.financialmanager.persistance.dao.TransactionDao
import pl.edu.pjatk.financialmanager.persistance.model.Transaction
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Database(entities = [Transaction::class], version = 5, exportSchema = false)
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
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .addCallback(FinancialManagerDatabaseCallback(scope)).build()
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

        // TODO this doesn't work
        private suspend fun populateDatabase(transactionDao: TransactionDao) {
            scope.launch {
                transactionDao.deleteAll()
                transactionDao.insertAll(
                    Transaction("Żabka", BigDecimal("21.19"), "Żywność", 0, LocalDateTime.now()),
                    Transaction(
                        "Biedronka",
                        BigDecimal("130.99"),
                        "Żywność",
                        0,
                        LocalDateTime.now()
                    ),
                    Transaction("Myjnia", BigDecimal("30.00"), "Samochód", 0, LocalDateTime.now()),
                    Transaction(
                        "Paliwo",
                        BigDecimal("-300.12"),
                        "Samochód",
                        0,
                        LocalDateTime.now()
                    ),
                    Transaction("ITN", BigDecimal("900.00"), "Uczelnia", 0, LocalDateTime.now())
                )
            }
        }
    }

}