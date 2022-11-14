package pl.edu.pjatk.financialmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.pjatk.financialmanager.databinding.ActivityTransactionsListBinding

class TransactionsListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionsListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.transactionsRecyclerView.layoutManager = LinearLayoutManager(this)
        val transactionsAdapter = TransactionsAdapter { transactionOnClick() }
        binding.transactionsRecyclerView.adapter = transactionsAdapter

        binding.newTransactionFab.setOnClickListener {
            addNewTransaction()
        }
    }

    private fun transactionOnClick() {
        print("click")
    }

    private fun addNewTransaction() {
        openAddNewTransactionActivity.launch(Unit)
    }

    private val openAddNewTransactionActivity =
        registerForActivityResult(NewTransactionActivityContract()) { result ->
            if (result != null) Toast.makeText(this, "$result", Toast.LENGTH_SHORT)
                .show()
            else
                Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show()
        }
}