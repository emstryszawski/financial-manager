package pl.edu.pjatk.financialmanager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pjatk.financialmanager.databinding.TransactionBinding
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TransactionsAdapter(
    private val onClick: (Transaction) -> Unit
) :
    RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>() {

    /* TODO(Remove hardcoded data for sql lite database) */
    private val transactionListMock = mockedData()
    private fun mockedData(): List<Transaction> {
        return listOf(
            Transaction(1, "Żabka", BigDecimal("21.19"), "Żywność", LocalDate.now()),
            Transaction(2, "Biedronka", BigDecimal("130.99"), "Żywność", LocalDate.now()),
            Transaction(3, "Myjnia bezdotykowa", BigDecimal("30.00"), "Samochód", LocalDate.now()),
            Transaction(4, "Shell Racing 100", BigDecimal("-300.12"), "Samochód", LocalDate.now()),
            Transaction(5, "ITN PJATK", BigDecimal("900.00"), "Uczelnia", LocalDate.now())
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        return TransactionViewHolder(
            TransactionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), onClick
        )
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionListMock[position]
        holder.bind(transaction)

        holder.takeIf { transaction.isExpense() }
            ?.apply { setAmountColorTo(R.color.negative_amount) }
    }

    override fun getItemCount(): Int {
        return transactionListMock.size
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

        fun bind(transaction: Transaction) {
            currentTransaction = transaction

            binding.placeName.text = transaction.title
            binding.amount.text = transaction.value.toPlainString()
            binding.date.text = transaction.date.format(DateTimeFormatter.ISO_DATE)
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
