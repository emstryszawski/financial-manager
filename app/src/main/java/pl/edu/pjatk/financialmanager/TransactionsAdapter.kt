package pl.edu.pjatk.financialmanager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pjatk.financialmanager.TransactionsAdapter.TransactionViewHolder
import pl.edu.pjatk.financialmanager.databinding.TransactionBinding
import pl.edu.pjatk.financialmanager.persistance.Transaction

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

    fun getPositionOfTransactionById(id: Int): Int {
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
            binding.amount.text = transaction.amount.toPlainString()
            binding.date.text = transaction.dateOfTransaction?.toString()
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
