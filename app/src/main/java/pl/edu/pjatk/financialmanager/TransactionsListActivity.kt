package pl.edu.pjatk.financialmanager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.pjatk.financialmanager.adapter.TransactionsAdapter
import pl.edu.pjatk.financialmanager.databinding.ActivityTransactionsListBinding
import pl.edu.pjatk.financialmanager.persistance.model.Transaction
import pl.edu.pjatk.financialmanager.viewmodel.TransactionViewModel

class TransactionsListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionsListBinding
    private lateinit var transactionViewModel: TransactionViewModel

    private lateinit var transactionsAdapter: TransactionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.transactionsRecyclerView.layoutManager = LinearLayoutManager(this)
        transactionsAdapter = TransactionsAdapter(
            onClick = this::transactionOnClick,
            onLongClick = this::transactionOnLongClick
        )
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

    private fun transactionOnLongClick(transaction: Transaction) {
        transactionViewModel.deleteTransaction(transaction)
        Toast.makeText(this, "Transaction deleted", Toast.LENGTH_SHORT).show()
    }

    // TODO
    private fun transactionOnClick(transaction: Transaction) {
        val intent = Intent(this, AddNewTransactionActivity::class.java)
        intent.putExtra("transaction", transaction)
//        intent.putExtra("id", transaction.id)
//        intent.putExtra("title", transaction.title)
//        intent.putExtra("amount", CurrencyFormatter.toCurrencyFormat(transaction.amount))
//        intent.putExtra("category", transaction.category)
//        intent.putExtra("categoryItemPosition", transaction.categoryItemPosition)
//        intent.putExtra("dateOfTransaction", transaction.dateOfTransaction)
        editTransactionLauncher.launch(intent)
    }

    private var editTransactionLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK && it.data != null) {
            val updatedTransaction =
                it.data?.getParcelableExtra("transaction", Transaction::class.java)

            updatedTransaction?.let { transaction ->
                transactionViewModel.updateTransaction(transaction)
                    .observe(this) { idOfUpdatedTransaction ->
                        Log.d(this::class.java.name, updatedTransaction.id.toString())
                    }
            }
        }
    }

    private var newTransactionLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK && it.data != null) {
            val newTransaction =
                it.data?.getParcelableExtra("newTransaction", Transaction::class.java)
            val id = newTransaction?.let { transaction ->
                transactionViewModel.addNewTransaction(transaction)
            }

            // TODO get id and then get position in list to scroll to newly inserted item
            id?.observe(this) { transactionId ->
                Log.d(this::class.java.name, "transactionId in Activity: $transactionId")
//                binding.transactionsRecyclerView.smoothScrollToPosition(
//                    transactionsAdapter.getPositionOfTransactionById(transactionId)
//                )
            }
        }
    }

    private fun addNewTransaction() {
        newTransactionLauncher.launch(Intent(this, AddNewTransactionActivity::class.java))
    }
}