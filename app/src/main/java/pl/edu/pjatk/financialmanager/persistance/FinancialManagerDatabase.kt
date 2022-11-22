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
import java.util.*

@Database(entities = [Transaction::class], version = 1, exportSchema = true)
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
            val calendar = Calendar.getInstance()
            transactionDao.insertAll(
                Transaction(1, "Żabka", BigDecimal("21.19"), "Żywność", calendar.time),
                Transaction(2, "Biedronka", BigDecimal("130.99"), "Żywność", calendar.time),
                Transaction(
                    3,
                    "Myjnia bezdotykowa",
                    BigDecimal("30.00"),
                    "Samochód",
                    calendar.time
                ),
                Transaction(
                    4,
                    "Shell Racing 100",
                    BigDecimal("-300.12"),
                    "Samochód",
                    calendar.time
                ),
                Transaction(5, "ITN PJATK", BigDecimal("900.00"), "Uczelnia", calendar.time)
            )
        }
    }

}