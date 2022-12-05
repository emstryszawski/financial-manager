package pl.edu.pjatk.financialmanager

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import pl.edu.pjatk.financialmanager.persistance.FinancialManagerDatabase
import pl.edu.pjatk.financialmanager.persistance.repository.TransactionRepository

class FinancialManagerApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { FinancialManagerDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { TransactionRepository(database.transactionDao()) }
}