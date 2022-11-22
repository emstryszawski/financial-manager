package pl.edu.pjatk.financialmanager

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class NewTransactionActivityContract : ActivityResultContract<Unit, String?>() {

    companion object {
        const val TRANSACTION = "TRANSACTION CONTRACT"
    }

    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(context, AddNewTransactionActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        val data = intent?.getStringExtra(TRANSACTION)
        return if (resultCode == Activity.RESULT_OK && !data.isNullOrEmpty()) data else null
    }
}