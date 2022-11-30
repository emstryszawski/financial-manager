package pl.edu.pjatk.financialmanager

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import pl.edu.pjatk.financialmanager.databinding.ActivityAddNewTransactionBinding
import pl.edu.pjatk.financialmanager.persistance.Transaction
import pl.edu.pjatk.financialmanager.util.DataConverter
import java.time.LocalDate
import java.util.*


class AddNewTransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNewTransactionBinding
    private lateinit var newTransactionViewModel: TransactionListViewModel

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewTransactionBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)

        newTransactionViewModel = ViewModelProvider(
            this, TransactionListViewModel.Factory
        )[TransactionListViewModel::class.java]

        binding.saveAndContinueButton.setOnClickListener { saveAndContinue() }

        binding.amountInputText.setRawInputType(Configuration.KEYBOARD_12KEY)
        binding.amountInputText.setText(DataConverter.getZero())
        binding.amountInputText.addTextChangedListener(object : TextWatcher {

            private var current = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = binding.amountInputText
                if (s.toString() != current) {
                    input.removeTextChangedListener(this);

                    var formatted = DataConverter.formatToAmount(s.toString())

                    if (!binding.switchToIncome.isChecked && DataConverter.isZero(formatted)) {
                        formatted = StringBuilder("-").append(formatted).toString()
                    }

                    current = formatted
                    input.setText(formatted);
                    input.setSelection(formatted.length - 3);

                    input.addTextChangedListener(this);
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        binding.switchToIncome.isChecked = true

        binding.switchToIncome.setOnCheckedChangeListener { _, isChecked ->
            val text = binding.amountInputText.text.toString()
            if (isChecked) {
                if (text.contains("-")) {
                    binding.amountInputText.editableText.replace(0, 1, "")
                }
            } else {
                if (!text.contains("-")) {
                    binding.amountInputText.text!!.insert(0, "-")
                }
            }
        }
    }

    private fun saveAndContinue() {
        val newTransaction = getTransactionFromInputs()
        val intent = Intent()
            .putExtra("title", newTransaction.title)
            .putExtra("amount", newTransaction.amount.toString())
            .putExtra("category", newTransaction.category)
            .putExtra("year", newTransaction.dateOfTransaction?.year)
            .putExtra("month", newTransaction.dateOfTransaction?.monthValue)
            .putExtra("day", newTransaction.dateOfTransaction?.dayOfMonth)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun getTransactionFromInputs(): Transaction {
        val title = binding.titleInputText.text.toString()
        val amount = DataConverter.formatStringToDecimal(binding.amountInputText.text.toString())
        val date = getDateFromInput()
        val category = binding.categorySpinner.selectedItem.toString()
        return Transaction(title, amount, category, date)
    }

    private fun getDateFromInput(): LocalDate {
        val datePicker = binding.datePicker
        return LocalDate.of(
            datePicker.year, datePicker.month, datePicker.dayOfMonth
        )
    }
}