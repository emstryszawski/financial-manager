package pl.edu.pjatk.financialmanager.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import pl.edu.pjatk.financialmanager.R
import pl.edu.pjatk.financialmanager.adapter.TransactionsAdapter.TransactionViewHolder
import pl.edu.pjatk.financialmanager.databinding.TransactionBinding
import pl.edu.pjatk.financialmanager.persistance.model.Transaction
import pl.edu.pjatk.financialmanager.util.CurrencyFormatter
import java.time.format.DateTimeFormatter

class TransactionsAdapter(
    private val onClick: (Transaction) -> Unit
) :
    ListAdapter<Transaction, TransactionViewHolder>(TransactionDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        return TransactionViewHolder.create(parent, onClick)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = getItem(position)
        holder.bind(transaction)
        holder.takeIf { transaction.isExpense() }
            ?.apply {
                setAmountColorTo(R.color.negative_amount)
            }
    }

    @Suppress("unused")
    fun getPositionOfTransactionById(id: Long): Int {
        val get = currentList.stream().filter { transaction ->
            transaction.id == id
        }.findFirst()
            .get()
        return currentList.indexOf(get)
    }

    class TransactionViewHolder(
        private val binding: TransactionBinding,
        val onClick: (Transaction) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private var currentTransaction: Transaction? = null

        init {
            binding.cardViewTransaction.setOnClickListener {
                currentTransaction?.let {
                    onClick(it)
                }
            }
        }

        companion object {
            fun create(parent: ViewGroup, onClick: (Transaction) -> Unit): TransactionViewHolder {
                val binding = TransactionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return TransactionViewHolder(binding, onClick)
            }
        }

        fun bind(transaction: Transaction) {
            currentTransaction = transaction

            binding.placeName.text = transaction.title
            binding.amount.text = CurrencyFormatter.toCurrencyFormat(transaction.amount)
            binding.date.text =
                String.format(transaction.dateOfTransaction.format(DateTimeFormatter.ISO_LOCAL_DATE))
            binding.category.text = transaction.category
        }

        fun setAmountColorTo(colorId: Int) {
            binding.amount.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    colorId
                )
            )
        }
    }
}

object TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {

    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.id == newItem.id
    }

}
