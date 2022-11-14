package pl.edu.pjatk.financialmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import pl.edu.pjatk.financialmanager.databinding.ActivityAddNewTransactionBinding

class AddNewTransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNewTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewTransactionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.datePickerButton.setOnClickListener {
            Toast
                .makeText(this, "datePickerButton pressed", Toast.LENGTH_LONG)
                .show()
        }

        binding.saveAndContinueButton.setOnClickListener { saveAndContinue() }
    }

    private fun saveAndContinue() {
        val intent = Intent()
            .putExtra(NewTransactionActivityContract.TRANSACTION, binding.editTextTextPersonName.text.toString().trim())
        setResult(RESULT_OK, intent)
        finish()
    }
}