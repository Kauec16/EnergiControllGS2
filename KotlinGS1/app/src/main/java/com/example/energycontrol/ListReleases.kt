package com.example.energycontrol

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.energycontrol.databaseHandler.DatabaseHandler

class ListReleases : AppCompatActivity() {

    val dbHandler = DatabaseHandler(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_releases)
        // Exemplo de vetor de dados

        val lista  = dbHandler.listEnergyCostEntry()

        if (lista.isNotEmpty()) {
            // Mapear os dados para uma estrutura adequada para a tabela
            val entryData = lista.map { arrayOf(it.groupName.toString(), it.value.toString(), it.frequency, it.date) }.toTypedArray()

            // Referenciar o TableLayout no XML
            val tableLayout = findViewById<android.widget.TableLayout>(R.id.TableReleases)

            // Preencher a tabela com os dados
            for (linha in entryData) {
                val tableRow = TableRow(this)

                // Adicionando os itens da linha na TableRow
                for (item in linha) {
                    val textView = TextView(this)
                    textView.text = item
                    textView.setPadding(16, 16, 16, 16) // Ajuste o padding conforme necessário
                    textView.gravity = android.view.Gravity.CENTER
                    tableRow.addView(textView)
                }

                // Adicionar a TableRow no TableLayout
                tableLayout.addView(tableRow)
            }
        }
    }

    fun backScreen(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun addRelease(view: View) {
        val intent = Intent(this, AddEnergyEntry::class.java)
        startActivity(intent)
    }


}