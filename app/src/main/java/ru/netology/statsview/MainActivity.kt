package ru.netology.statsview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.statsview.ui.StatsView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val view = findViewById<StatsView>(R.id.stats_view)
        view.postDelayed({
            view.data = listOf(
                0.2F,
                0.2F,
                0.2F,
                0.2F,
                //0.2F,
            )
        }, 50)

    }
}