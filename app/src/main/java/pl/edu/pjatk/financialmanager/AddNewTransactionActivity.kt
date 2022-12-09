package pl.edu.pjatk.financialmanager

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import pl.edu.pjatk.financialmanager.databinding.ActivityAddNewTransactionBinding
import pl.edu.pjatk.financialmanager.persistance.model.Transaction
import pl.edu.pjatk.financialmanager.util.CurrencyFormatter
import pl.edu.pjatk.financialmanager.viewmodel.TransactionViewModel
import java.time.LocalDateTime


class AddNewTransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNewTransactionBinding
    private lateinit var newTransactionViewModel: TransactionViewModel
    private var transaction: Transaction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        newTransactionViewModel = ViewModelProvider(
            this, TransactionViewModel.Factory
        )[TransactionViewModel::class.java]

        transaction = intent.getParcelableExtra("transaction", Transaction::class.java)

        binding.amountInputText.setRawInputType(Configuration.KEYBOARD_12KEY)
        binding.amountInputText.setText(CurrencyFormatter.getZero())
        binding.amountInputText.addTextChangedListener(object : TextWatcher {

            private var current = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = binding.amountInputText
                if (s.toString() != current) {
                    input.removeTextChangedListener(this)

                    var formatted = CurrencyFormatter.formatToAmount(s.toString())

                    if (!binding.switchToIncome.isChecked && CurrencyFormatter.isZero(formatted)) {
                        formatted = StringBuilder("-").append(formatted).toString()
                    }

                    current = formatted
                    input.setText(formatted)
                    input.setSelection(formatted.length - 3)

                    input.addTextChangedListener(this)
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        binding.switchToIncome.isChecked = true
        binding.amountInputText.setRawInputType(Configuration.KEYBOARD_12KEY)

        transaction?.let { it ->
            binding.titleInputText.setText(it.title)
            binding.amountInputText.setText(CurrencyFormatter.toCurrencyFormat(it.amount))
            binding.categorySpinner.setSelection(it.categoryItemPosition, true)
            it.dateOfTransaction.let { t ->
                binding.datePicker.updateDate(t.year, t.monthValue - 1, t.dayOfMonth)
            }
            binding.switchToIncome.isChecked = it.isIncome()
        }

        binding.saveAndContinueButton.setOnClickListener { saveAndContinue() }

        binding.amountInputText.addTextChangedListener(object : TextWatcher {

            private var current = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = binding.amountInputText
                if (s.toString() != current) {
                    input.removeTextChangedListener(this)

                    var formatted = CurrencyFormatter.formatToAmount(s.toString())

                    if (!binding.switchToIncome.isChecked && CurrencyFormatter.isZero(formatted)) {
                        formatted = StringBuilder("-").append(formatted).toString()
                    }

                    current = formatted
                    input.setText(formatted)
                    input.setSelection(formatted.length - 3)

                    input.addTextChangedListener(this)
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        })

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

        binding.shareButton.setOnClickListener {
            shareTransaction()
        }
    }

    private fun shareTransaction() {
        var title = binding.titleInputText.text.toString()
        title = if (title.isEmpty()) "" else "$title,"
        val amount = binding.amountInputText.text.toString()
        val share = Intent.createChooser(Intent().apply {
            type = "text/plain"
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, transaction.toString())

            putExtra(Intent.EXTRA_TITLE, "$title $amount")
        }, title)
        startActivity(share)
    }

    private fun saveAndContinue() {
        val newTransaction = getTransactionFromInputs()
        val intent = Intent()
        if (this.intent.hasExtra("transaction")) {
            transaction?.let {
                intent.putExtra("transaction", updateTransaction(newTransaction))
                setResult(RESULT_OK, intent)
            }
        } else {
            intent.putExtra("newTransaction", newTransaction)
            setResult(RESULT_OK, intent)
        }
        finish()
    }

    private fun getTransactionFromInputs(): Transaction {
        val title = binding.titleInputText.text.toString()
        val amount =
            CurrencyFormatter.formatStringToDecimal(binding.amountInputText.text.toString())
        val category = binding.categorySpinner.selectedItem.toString()
        val categoryItemPosition = binding.categorySpinner.selectedItemPosition
        val date = getDateFromInput()
        return Transaction(title, amount, category, categoryItemPosition, date)
    }

    private fun updateTransaction(newTransaction: Transaction): Transaction? {
        return transaction?.apply {
            this.title = newTransaction.title
            this.amount = newTransaction.amount
            this.category = newTransaction.category
            this.categoryItemPosition = newTransaction.categoryItemPosition
            this.dateOfTransaction = newTransaction.dateOfTransaction
        }
    }

    private fun getDateFromInput(): LocalDateTime {
        val datePicker = binding.datePicker
        return LocalDateTime.of(
            datePicker.year, datePicker.month + 1, datePicker.dayOfMonth, 0, 0, 0
        )
    }
}