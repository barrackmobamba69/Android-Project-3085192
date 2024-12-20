//Name - Udayy Singh Pawar
//Student Number - 3085192
//Mobile Development [BSCH-MD/Dub/FT]
//Milestone 3 - Archive and document

package com.griffith.androidproject3085192

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Helper class for managing the SQLite database
class Database(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "fitness_db" // Database name
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "fitness_records"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_STEPS = "steps" // Column for steps
        private const val COLUMN_DISTANCE = "distance" // Column for distance
        private const val COLUMN_CALORIES = "calories" // Column for calories
    }

    // Called when the database is created for the first time
    override fun onCreate(db: SQLiteDatabase) {
        // SQL statement to create the fitness_records table
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_DATE TEXT PRIMARY KEY,
                $COLUMN_STEPS INTEGER,
                $COLUMN_DISTANCE REAL,
                $COLUMN_CALORIES REAL
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    // Called when the database version is upgraded
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Function to save a new fitness record in the database
    fun saveRecord(date: String, steps: Int, distance: Float, calories: Float) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DATE, date)
            put(COLUMN_STEPS, steps)
            put(COLUMN_DISTANCE, distance)
            put(COLUMN_CALORIES, calories)
        }
        db.replace(TABLE_NAME, null, values)
    }

    // Function to clear all data from the fitness_records table
    fun clearAllData() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
    }

    // Function to retrieve all fitness records from the database
    fun getAllRecords(): List<FitnessRecord> {
        val records = mutableListOf<FitnessRecord>() // List to store the records
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, "$COLUMN_DATE DESC")

        // Iterate through the cursor to extract each record
        with(cursor) {
            while (moveToNext()) {
                records.add(
                    FitnessRecord(
                        getString(getColumnIndexOrThrow(COLUMN_DATE)),
                        getInt(getColumnIndexOrThrow(COLUMN_STEPS)),
                        getFloat(getColumnIndexOrThrow(COLUMN_DISTANCE)),
                        getFloat(getColumnIndexOrThrow(COLUMN_CALORIES))
                    )
                )
            }
        }
        cursor.close()
        return records
    }
}

// Data class to represent a fitness record
data class FitnessRecord(
    val date: String,
    val steps: Int,
    val distance: Float,
    val calories: Float
)