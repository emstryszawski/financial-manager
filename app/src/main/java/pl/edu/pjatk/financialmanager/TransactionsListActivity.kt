package pl.edu.pjatk.financialmanager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.pjatk.financialmanager.databinding.ActivityTransactionsListBinding
import pl.edu.pjatk.financialmanager.persistance.Transaction
import java.math.BigDecimal
import java.time.LocalDate

class TransactionsListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionsListBinding
    private lateinit var transactionListViewModel: TransactionListViewModel

    private lateinit var transactionsAdapter: TransactionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.transactionsRecyclerView.layoutManager = LinearLayoutManager(this)
        transactionsAdapter = TransactionsAdapter { transactionOnClick() }
        binding.transactionsRecyclerView.adapter = transactionsAdapter


        transactionListViewModel = ViewModelProvider(
            this,
            TransactionListViewModel.Factory
        )[TransactionListViewModel::class.java]

        transactionListViewModel.allTransactions.observe(this) {
            transactionsAdapter.submitList(it)
        }

        binding.newTransactionFab.setOnClickListener {
            addNewTransaction()
        }
    }

    private fun transactionOnClick() {
        print("click")
    }

    private fun addNewTransaction() {
        activityLauncher.launch(Intent(this, AddNewTransactionActivity::class.java))
    }

    private var activityLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK && it.data != null) {
            val newTransaction = getTransactionFromResult(it)
            val id = transactionListViewModel.addNewTransaction(newTransaction)

            id.observe(this) { transactionId ->
                Log.d("MainActivity", "transactionId in Activity: $transactionId")

//                binding.transactionsRecyclerView.smoothScrollToPosition(
//                    transactionsAdapter.getPositionOfTransactionById(transactionId.toInt())
//                )
            }
        }
    }

    private fun getTransactionFromResult(result: ActivityResult): Transaction {
        val transaction = result.data!!
        val title = transaction.getStringExtra("title")
        val amount = transaction.getStringExtra("amount")
        val category = transaction.getStringExtra("category")
        val year = transaction.getIntExtra("year", LocalDate.now().year)
        val month = transaction.getIntExtra("month", LocalDate.now().monthValue)
        val day = transaction.getIntExtra("day", LocalDate.now().dayOfMonth)
        val dot = LocalDate.of(year, month, day)
        return Transaction(title!!, BigDecimal(amount), category, dot)
    }
}