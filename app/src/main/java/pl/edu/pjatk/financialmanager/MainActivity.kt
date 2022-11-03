package pl.edu.pjatk.financialmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pl.edu.pjatk.financialmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}