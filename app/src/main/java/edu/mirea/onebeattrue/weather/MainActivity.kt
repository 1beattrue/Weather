package edu.mirea.onebeattrue.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import edu.mirea.onebeattrue.weather.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonLoad.setOnClickListener {
            loadData()
        }
    }

    private fun loadData() {
        Log.d("MainActivity", "Load started $this") // переверни телефон и наслаждайся
        binding.progress.isVisible = true
        binding.buttonLoad.isEnabled = false
        loadCity { city: String ->
            binding.tvLocation.text = city
            loadTemperature(city) { temperature: Int ->
                binding.tvTemperature.text = temperature.toString()
                binding.progress.isVisible = false
                binding.buttonLoad.isEnabled = true
                Log.d("MainActivity", "Load finished $this")
                // загрузка завершается в activity, которая была создана изначально
                // происходит утечка памяти,
                // потому что сборщик мусора не может уничтожить ни одну из activity, которую держат наши потоки
                // у потоков нет жизненного цикла -> они умрут только тогда, когда закончат свою работу
            }
        }
    }

    private fun loadCity(callback: (String) -> Unit) {
        thread {
            Thread.sleep(5000)
            runOnUiThread { // удивительно, но что написано, то этот метод и делает (на самом деле под капотом Handler)
                callback.invoke("Moscow")
            }
        }
    }

    private fun loadTemperature(city: String, callback: (Int) -> Unit) {
        thread {
            runOnUiThread {
                Toast.makeText(
                    this, getString(R.string.loading_temperature_toast, city), Toast.LENGTH_SHORT
                ).show()
            }

            Thread.sleep(5000)

            runOnUiThread { callback.invoke(17) }
        }
    }
}