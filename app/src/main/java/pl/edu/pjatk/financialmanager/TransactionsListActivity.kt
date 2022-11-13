package pl.edu.pjatk.financialmanager

import android.os.Bundle
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

//        binding.newTransactionFab.setOnClickListener {
//            addNewTransaction()
//        }
    }

    private fun transactionOnClick() {
        print("click")
    }

    private fun addNewTransaction() {
        TODO("Not yet implemented")
    }
}