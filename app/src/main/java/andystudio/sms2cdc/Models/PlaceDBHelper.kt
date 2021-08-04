package andystudio.sms2cdc.Models

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PlaceDBHelper (context: Context) :SQLiteOpenHelper(context, database, null, ver) {
    companion object {
        private const val database = "PlaceDatabase.db"
        private const val ver = 1
        private const val tableName = "PlaceDetail"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "CREATE TABLE IF NOT EXISTS $tableName (PlaceNo INTEGER PRIMARY KEY AUTOINCREMENT, PlaceName TEXT NOT NULL, PlaceStr TEXT NOT NULL, Latitude REAL, Longitude REAL)"
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //db?.execSQL("DROP TABLE IF EXISTS $tableName")
        //onCreate(db)
    }

    fun updatePlaceDetail(placeNo: Int, placeName: String) {
        val values = ContentValues()
        values.put("PlaceName", placeName)
        writableDatabase.update("$tableName", values, "PlaceNo = $placeNo", null)
    }

    fun addPlaceDetail(placeName: String, placeStr: String, latitude: Double, longitude: Double) {
        val values = ContentValues()
        values.put("PlaceName", placeName)
        values.put("PlaceStr", placeStr)
        values.put("Latitude", latitude)
        values.put("Longitude", longitude)
        writableDatabase.insert("$tableName", null, values)
    }

    fun deletePlaceDetail(placeNo: Int) {
        writableDatabase.delete("$tableName", "PlaceNo = $placeNo", null)
    }

    fun getPlaceDetailData(): ArrayList<Place> {
        val cursor = readableDatabase.query("$tableName",
                                                  arrayOf("PlaceNo", "PlaceName", "PlaceStr"),
                                          null,null,null,null, null)
        val placeList = ArrayList<Place>()

        try {
            if (cursor.moveToFirst()) {
                do {
                    val placeNo = cursor.getInt(cursor.getColumnIndex("PlaceNo"))
                    val placeName = cursor.getString(cursor.getColumnIndex("PlaceName"))
                    val placeStr = cursor.getString(cursor.getColumnIndex("PlaceStr"))
                    //val latitude = cursor.getDouble(cursor.getColumnIndex("Latitude"))
                    //val longitude = cursor.getDouble(cursor.getColumnIndex("Longitude"))

                    val placeItem = Place(placeNo, placeName, placeStr, 0.0, 0.0)
                    placeList.add(placeItem)
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {

        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
        }
        return  placeList
    }
}