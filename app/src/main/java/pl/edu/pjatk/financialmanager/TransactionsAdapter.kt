package pl.edu.pjatk.financialmanager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TransactionsAdapter(private val context: Context, private val onClick: (Transaction) -> Unit) :
    RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        return TransactionViewHolder(
            LayoutInflater
                .from(context)
                .inflate(R.layout.transaction, parent, false),
            onClick
        )
    }

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

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionListMock[position]
        holder.bind(transaction)
        if (transaction.value < BigDecimal.ZERO) {
            holder.itemView.findViewById<TextView>(R.id.amount).setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.negative_amount
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return transactionListMock.size
    }

    class TransactionViewHolder(
        transactionView: View,
        val onClick: (Transaction) -> Unit
    ) :
        RecyclerView.ViewHolder(transactionView) {
        private var currentTransaction: Transaction? = null

        init {
            itemView.findViewById<CardView>(R.id.card_view_transaction).setOnClickListener {
                currentTransaction?.let {
                    onClick(it)
                }
            }
        }

        fun bind(transaction: Transaction) {
            currentTransaction = transaction

            itemView.findViewById<TextView>(R.id.placeName).text = transaction.title
            itemView.findViewById<TextView>(R.id.amount).text = transaction.value.toPlainString()
            itemView.findViewById<TextView>(R.id.date).text = transaction.date.format(DateTimeFormatter.ISO_DATE)
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
