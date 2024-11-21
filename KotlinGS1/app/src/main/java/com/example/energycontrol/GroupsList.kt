package com.example.energycontrol

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.energycontrol.databaseHandler.DatabaseHandler
import com.example.energycontrol.model.Group

class GroupsList : AppCompatActivity() {

    val dbHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups_list)

        val lista = dbHandler.listGroups()

        if (lista.isNotEmpty()) {
            var dados =
                lista.map { arrayOf(it.id.toString(), it.name, it.frequency) }.toTypedArray()

            // Referenciar o TableLayout no XML
            var tableLayout = findViewById<android.widget.TableLayout>(R.id.TableGroups)

            // Preencher a tabela com os dados
            for (linha in dados) {
                var tableRow = TableRow(this)

                for (item in linha) {
                    val textView = TextView(this)
                    textView.text = item
                    textView.setPadding(16, 16, 16, 16)
                    textView.gravity = android.view.Gravity.CENTER
                    tableRow.addView(textView)
                }
                tableRow.addView(createDeleteButton(linha[0].toInt()))
                tableLayout.addView(tableRow)
            }
        }
    }

        fun addGroup(view: View) {
            var intent = Intent(this, CadastrosActivity::class.java)
            startActivity(intent)
        }

        fun backScreen(view: View) {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }




        fun deleteValue(id : Int) {
            dbHandler.deleteGroupById(id)
            recreate()
        }

    fun createDeleteButton(id : Int) : Button{
        val button = Button(this)
        button.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        button.setBackgroundColor(Color.WHITE)
        button.text = "Delete"
        button.setTextColor(Color.RED) // Define a cor do texto
        button.setOnClickListener {
            deleteValue(id)
        }
        return button
    }

    }