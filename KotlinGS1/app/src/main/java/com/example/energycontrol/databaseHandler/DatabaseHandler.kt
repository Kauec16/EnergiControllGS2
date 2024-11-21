package com.example.energycontrol.databaseHandler

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.energycontrol.model.EnergySpentEntry
import com.example.energycontrol.model.Group

class DatabaseHandler(ctx: Context) : SQLiteOpenHelper(ctx, DB_NAME, null, DB_VERSION) {

    companion object {
        public val DB_VERSION = 1;
        public val DB_NAME = "EnergyControl"
        public val GROUP_TABLE = "TABLE_GROUP"
        public val ID = "id"
        public val NAME = "Name"
        public val FREQUENCY = "Frequency"

        public val ENTRY_TABLE = "ENTRYS"
        public val GROUPID = "GROUPID"
        public val ENERGYVALUE = "ENERGYVALUE"
        public val CREATETIME = "CREATETAME"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Definir a criação da tabela 'Groups'
        val CREATE_TABLE_Groups = """
        CREATE TABLE $GROUP_TABLE (
            $ID INTEGER PRIMARY KEY AUTOINCREMENT, 
            $NAME TEXT NOT NULL, 
            $FREQUENCY TEXT NOT NULL
        );
    """

        // Definir a criação da tabela 'Entrys'
        val CREATE_TABLE_ENTRYS = """
        CREATE TABLE $ENTRY_TABLE (
            $ID INTEGER PRIMARY KEY AUTOINCREMENT, 
            $GROUPID INTEGER, 
            $ENERGYVALUE REAL NOT NULL, 
            $CREATETIME TEXT NOT NULL
        );
    """

        // Executar as queries de criação das tabelas
        db?.execSQL(CREATE_TABLE_Groups)
        db?.execSQL(CREATE_TABLE_ENTRYS)


        val CREATETRIGGER = "CREATE TRIGGER deleteChild\n" +
                "BEFORE DELETE ON $GROUP_TABLE\n" +
                "FOR EACH ROW\n" +
                "BEGIN\n" +
                "    SELECT CASE\n" +
                "        WHEN EXISTS (SELECT 1 FROM $ENTRY_TABLE WHERE groupId = OLD.id) THEN\n" +
                "            RAISE (ABORT, 'Cannot delete group with children')\n" +
                "    END;\n" +
                "END;"
        db?.execSQL(CREATETRIGGER)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Exemplo de como atualizar a tabela (apagar e recriar)
        db?.execSQL("DROP TABLE IF EXISTS $ENTRY_TABLE")
        db?.execSQL("DROP TABLE IF EXISTS $GROUP_TABLE")
        onCreate(db)
    }


    fun addGroup(group: Group) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(NAME, group.name)
            put(FREQUENCY, group.frequency) // Corrigido para usar FREQUENCY
        }

        try {
            val result = db.insert(GROUP_TABLE, null, values)
            if (result == -1L) {
                throw Exception("Erro ao inserir o grupo no banco de dados.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
    }

    fun deleteGroupById(id: Int) {
        val db = writableDatabase
        try {
            db.delete(GROUP_TABLE, "$ID = ?", arrayOf(id.toString()))
        } finally {
            // Fecha a conexão com o banco
            db.close()
        }
    }

    fun listGroups(): List<Group> {
        val db = readableDatabase
        val groupList = mutableListOf<Group>()
        val query = "SELECT * FROM $GROUP_TABLE"

        val cursor = db.rawQuery(query, null)
        try {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(ID))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(NAME))
                    val frequency = cursor.getString(cursor.getColumnIndexOrThrow(FREQUENCY))

                    groupList.add(Group(id, name, frequency))
                } while (cursor.moveToNext())
            }
        } finally {
            cursor.close()
            db.close()
        }
        return groupList
    }


    fun addEnergyCostEntry(energySpentEntry: EnergySpentEntry) {
        val db = writableDatabase
        var values = ContentValues().apply {
            put(GROUPID, energySpentEntry.groupId)
            put(CREATETIME, energySpentEntry.date)
            put(ENERGYVALUE, energySpentEntry.value)
        }

        try {
            // Insere os valores na tabela
            val result = db.insert(ENTRY_TABLE, null, values)
            if (result == -1L) {
                // Log ou mensagem de erro caso a inserção falhe
                throw Exception("Erro ao inserir o grupo no banco de dados.")
            }
        } catch (e: Exception) {
            e.printStackTrace() // Imprime o erro para fins de debug
        } finally {
            db.close() // Fecha o banco para liberar os recursos
        }
    }

    fun deleteEnergyCostEntry(id: Int) {
        val db = writableDatabase
        try {
            db.delete(ENTRY_TABLE, "$ID = ?", arrayOf(id.toString()))
        } finally {
            db.close()
        }
    }

    fun listEnergyCostEntry(): List<EnergySpentEntry> {
        val db = readableDatabase
        val entryList = mutableListOf<EnergySpentEntry>()
        val query = """
          SELECT $GROUP_TABLE.id as groupId,
          $GROUP_TABLE.name as groupName,
          $ENTRY_TABLE.id as entryId,
          $ENTRY_TABLE.ENERGYVALUE as value,
          $GROUP_TABLE.Frequency as frequency,
          $ENTRY_TABLE.CREATETAME as data
          FROM $ENTRY_TABLE 
          INNER JOIN $GROUP_TABLE 
          ON $ENTRY_TABLE.$GROUPID = $GROUP_TABLE.$ID
        """

        val cursor = db.rawQuery(query, null)
        try {
            if (cursor.moveToFirst()) {
                do {
                    val groupId = cursor.getInt(cursor.getColumnIndexOrThrow("groupId"))
                    val groupName = cursor.getString(cursor.getColumnIndexOrThrow("groupName"))
                    val entryId = cursor.getInt(cursor.getColumnIndexOrThrow("entryId"))
                    val value = cursor.getDouble(cursor.getColumnIndexOrThrow("value"))
                    val frequency = cursor.getString(cursor.getColumnIndexOrThrow("frequency"))
                    val data = cursor.getString(cursor.getColumnIndexOrThrow("data"))
                    entryList.add(
                        EnergySpentEntry(
                            entryId,
                            groupId,
                            groupName,
                            data,
                            frequency,
                            value
                        )
                    )
                } while (cursor.moveToNext())
            }
        } finally {
            cursor.close()
            db.close()
        }
        return entryList
    }








    fun generateChart(group : String) : MutableList<Pair<String, Double>> {
        val matriz: MutableList<Pair<String, Double>> = mutableListOf()
        val db = readableDatabase
        val entryList = mutableListOf<EnergySpentEntry>()
        val query = """
          SELECT 
    CASE
        WHEN TABLE_GROUP.Frequency = 'Dia' THEN strftime('%d/%m/%Y', substr(CREATETAME, 7, 4) || '-' || substr(CREATETAME, 4, 2) || '-' || substr(CREATETAME, 1, 2))
        WHEN TABLE_GROUP.Frequency = 'Mes' THEN strftime('%m/%Y', substr(CREATETAME, 7, 4) || '-' || substr(CREATETAME, 4, 2) || '-01') -- Apenas mês e ano
        WHEN TABLE_GROUP.Frequency = 'Ano' THEN strftime('%Y', substr(CREATETAME, 7, 4) || '-01-01') -- Apenas o ano
    END AS agrupamento,
    SUM(ENERGYVALUE) AS total
FROM ENTRYS
INNER JOIN TABLE_GROUP 
          ON ENTRYS.GROUPID = TABLE_GROUP.id
WHERE TABLE_GROUP.Name = '$group'
GROUP BY agrupamento
order by 1,total
        """

        val cursor = db.rawQuery(query, null)
        try {
            if (cursor.moveToFirst()) {
                do {
                    matriz.add(Pair(cursor.getString(cursor.getColumnIndexOrThrow("agrupamento")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("total"))))
                } while (cursor.moveToNext())
            }
        } finally {
            cursor.close()
            db.close()
        }
        return matriz
    }



    fun generateRanking() : MutableList<Triple<Int, String, Double>> {
        val matriz: MutableList<Triple<Int, String, Double>> = mutableListOf()
        val db = readableDatabase
        val entryList = mutableListOf<EnergySpentEntry>()
        val query = """
            SELECT TABLE_GROUP.id as id,
    TABLE_GROUP.Name AS name,
    SUM(ENERGYVALUE) as total
    FROM ENTRYS
    INNER JOIN TABLE_GROUP 
    ON ENTRYS.GROUPID = TABLE_GROUP.id
    GROUP BY TABLE_GROUP.Name
    ORDER BY total DESC;
        """

        val cursor = db.rawQuery(query, null)
        try {
            if (cursor.moveToFirst()) {
                do {
                    matriz.add(
                        Triple(
                            cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                            cursor.getString(cursor.getColumnIndexOrThrow("name")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("total"))
                        )
                    )
                } while (cursor.moveToNext())
            }
        } finally {
            cursor.close()
            db.close()
        }
        return matriz
    }













}