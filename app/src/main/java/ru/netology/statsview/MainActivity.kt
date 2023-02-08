package ru.netology.statsview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import ru.netology.statsview.ui.StatsView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val starter = findViewById<Button>(R.id.starter)
        starter.setOnClickListener {
            val view = findViewById<StatsView>(R.id.stats_view)
            view.post { //Проверку на текст добавлять не стал
                view.fillingTypeField = findViewById<EditText>(R.id.filling_type).text.toString()
                view.data = listOf(
                    findViewById<EditText>(R.id.param_1).text.ifEmpty { 0F }.toString().toFloat(),
                    findViewById<EditText>(R.id.param_2).text.ifEmpty { 0F }.toString().toFloat(),
                    findViewById<EditText>(R.id.param_3).text.ifEmpty { 0F }.toString().toFloat(),
                    findViewById<EditText>(R.id.param_4).text.ifEmpty { 0F }.toString().toFloat(),
                    findViewById<EditText>(R.id.param_5).text.ifEmpty { 0F }.toString().toFloat(),
                )
            }
        }
    }
}