package pl.edu.pjatk.financialmanager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.pjatk.financialmanager.adapter.TransactionsAdapter
import pl.edu.pjatk.financialmanager.databinding.ActivityTransactionsListBinding
import pl.edu.pjatk.financialmanager.persistance.model.Transaction
import pl.edu.pjatk.financialmanager.viewmodel.TransactionViewModel
import java.math.BigDecimal
import java.time.LocalDateTime

class TransactionsListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionsListBinding
    private lateinit var transactionViewModel: TransactionViewModel

    private lateinit var transactionsAdapter: TransactionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.transactionsRecyclerView.layoutManager = LinearLayoutManager(this)
        transactionsAdapter = TransactionsAdapter { transactionOnClick() }
        binding.transactionsRecyclerView.adapter = transactionsAdapter


        transactionViewModel = ViewModelProvider(
            this,
            TransactionViewModel.Factory
        )[TransactionViewModel::class.java]

        transactionViewModel.allTransactions.observe(this) {
            transactionsAdapter.submitList(it)
        }

        binding.newTransactionFab.setOnClickListener {
            addNewTransaction()
        }
    }

    // TODO
    private fun transactionOnClick() {
        print("click")
    }

    private var activityLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK && it.data != null) {
            val newTransaction = getTransactionFromResult(it)
//            val id =
            transactionViewModel.addNewTransaction(newTransaction)

            // TODO get id and then get position in list to scroll to newly inserted item
//            id.observe(this) { transactionId ->
//                Log.d("MainActivity", "transactionId in Activity: $transactionId")

//                binding.transactionsRecyclerView.smoothScrollToPosition(
//                    transactionsAdapter.getPositionOfTransactionById(transactionId.toInt())
//                )
        }
    }

    private fun getTransactionFromResult(result: ActivityResult): Transaction {
        val transaction = result.data!!
        val title = transaction.getStringExtra("title")
        val amount = transaction.getStringExtra("amount")
        val category = transaction.getStringExtra("category")
        val year = transaction.getIntExtra("year", LocalDateTime.now().year)
        val month = transaction.getIntExtra("month", LocalDateTime.now().monthValue)
        val day = transaction.getIntExtra("day", LocalDateTime.now().dayOfMonth)
        val dot = LocalDateTime.of(year, month, day, 0, 0, 0)
        return Transaction(title!!, BigDecimal(amount), category, dot)
    }

    private fun addNewTransaction() {
        activityLauncher.launch(Intent(this, AddNewTransactionActivity::class.java))
    }
}