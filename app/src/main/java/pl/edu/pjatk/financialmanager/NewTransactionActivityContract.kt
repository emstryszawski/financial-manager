package pl.edu.pjatk.financialmanager

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import pl.edu.pjatk.financialmanager.persistance.Transaction

class NewTransactionActivityContract : ActivityResultContract<Unit, Long?>() {

    companion object {
        const val TRANSACTION = "TRANSACTION CONTRACT"
    }

    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(context, AddNewTransactionActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Long? {
        val data = intent?.getLongExtra(TRANSACTION, 0)
        return if (resultCode == Activity.RESULT_OK && data != 0L) data else null
    }
}