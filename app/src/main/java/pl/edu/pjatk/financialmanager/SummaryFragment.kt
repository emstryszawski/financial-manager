package pl.edu.pjatk.financialmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import pl.edu.pjatk.financialmanager.databinding.FragmentSummaryBinding
import pl.edu.pjatk.financialmanager.persistance.model.Transaction
import pl.edu.pjatk.financialmanager.util.CurrencyFormatter
import pl.edu.pjatk.financialmanager.viewmodel.TransactionViewModel
import java.math.BigDecimal
import java.util.function.Predicate

class SummaryFragment : Fragment() {
    private lateinit var binding: FragmentSummaryBinding
    private lateinit var transactionViewModel: TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSummaryBinding.inflate(layoutInflater, container, false)
        transactionViewModel =
            ViewModelProvider(requireActivity())[TransactionViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transactionViewModel.allTransactions.observe(
            viewLifecycleOwner,
            calculateTotalIncomesAndOutcomesInCurrentMonth()
        )
    }

    private fun calculateTotalIncomesAndOutcomesInCurrentMonth(): Observer<List<Transaction>> {
        return Observer { transactions ->
            binding.incomesAmountTextView.text =
                calculateTotalInCurrentMonth(transactions, Transaction::isIncome)
            binding.expensesAmountTextView.text =
                calculateTotalInCurrentMonth(transactions, Transaction::isExpense)
        }
    }

    private fun calculateTotalInCurrentMonth(
        transactions: List<Transaction>,
        predicate: Predicate<in Transaction>
    ): String {
        var total = BigDecimal.ZERO
        transactions.stream()
            .filter(predicate)
            .forEach { transaction -> total += transaction.amount }
        return CurrencyFormatter.toCurrencyFormat(total)
    }

}