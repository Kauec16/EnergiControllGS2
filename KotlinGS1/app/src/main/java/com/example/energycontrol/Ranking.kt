package com.example.energycontrol

import android.os.Bundle
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.energycontrol.databaseHandler.DatabaseHandler

class Ranking : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val dbHandler = DatabaseHandler(this)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ranking)

            val lista = dbHandler.generateRanking()

            if (lista.isNotEmpty()) {
                // Lista com Triple<Int, String, Double>
                val dados = lista

                // Referenciar o TableLayout no XML
                val tableLayout = findViewById<android.widget.TableLayout>(R.id.TableGroupsRanking)

                // Preencher a tabela com os dados
                for (linha in dados) {
                    val tableRow = TableRow(this)

                    // Acessando os valores de cada Triple corretamente
                    val id = linha.first.toString()  // `first` é a propriedade que contém o Int
                    val name = linha.second           // `second` é a propriedade que contém o String
                    val total = linha.third.toString() // `third` é a propriedade que contém o Double

                    // Adicionando os dados à linha
                    val textView1 = TextView(this)
                    textView1.text = id
                    textView1.setPadding(16, 16, 16, 16)
                    textView1.gravity = android.view.Gravity.CENTER
                    tableRow.addView(textView1)

                    val textView2 = TextView(this)
                    textView2.text = name
                    textView2.setPadding(16, 16, 16, 16)
                    textView2.gravity = android.view.Gravity.CENTER
                    tableRow.addView(textView2)

                    val textView3 = TextView(this)
                    textView3.text = total
                    textView3.setPadding(16, 16, 16, 16)
                    textView3.gravity = android.view.Gravity.CENTER
                    tableRow.addView(textView3)

                    tableLayout.addView(tableRow)
                }
            }
        }

}