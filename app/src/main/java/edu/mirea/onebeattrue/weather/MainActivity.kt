package edu.mirea.onebeattrue.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import edu.mirea.onebeattrue.weather.databinding.ActivityMainBinding
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonLoad.setOnClickListener {

            binding.progress.isVisible = true
            binding.buttonLoad.isEnabled = false

            val deferredCity = lifecycleScope.async {
                    loadCity() // возвращает название города -> deferredCity: String = название города
                }

            val deferredTemperature = lifecycleScope.async {
                loadTemperature()
            }

            lifecycleScope.launch {
                val city = deferredCity.await() // делает то же самое что и join, но возвращает результат нужного типа
                val temperature = deferredTemperature.await()

                binding.tvLocation.text = city
                binding.tvTemperature.text = temperature.toString()

                Toast.makeText(
                    this@MainActivity,
                    "City: $city, temperature: $temperature",
                    Toast.LENGTH_SHORT
                ).show()

                binding.progress.isVisible = false
                binding.buttonLoad.isEnabled = true
            }
        }
    }

    private suspend fun loadCity(): String {
        delay(2000)
        return "Moscow"

    }

    private suspend fun loadTemperature(): Int {
        delay(5000)
        return 17
    }
}