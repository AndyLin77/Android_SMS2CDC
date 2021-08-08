package andystudio.sms2cdc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import andystudio.sms2cdc.Models.Place
import andystudio.sms2cdc.Models.PlaceAdapter
import andystudio.sms2cdc.Models.PlaceDBHelper


class MainActivity : AppCompatActivity() {

    private lateinit var placeAdapter: PlaceAdapter
    private var placeList = ArrayList<Place>()

    private lateinit var dbHelper: PlaceDBHelper

    //var placeListView: ListView? = null
    var placeListRecyclerView: RecyclerView? = null

    var addPlaceActivityCode: Int = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = PlaceDBHelper(this)
        placeListRecyclerView = findViewById(R.id.recyclerView)
        placeList = dbHelper.getPlaceDetailData()

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        placeListRecyclerView!!.layoutManager = linearLayoutManager

        placeAdapter = PlaceAdapter(placeList, this, this)
        placeListRecyclerView!!.adapter = placeAdapter



//        placeListView = findViewById(R.id.placeListView)
//        placeListView!!.setOnItemClickListener { parent, _, position, _ ->
//            val selectedItem = parent.getItemAtPosition(position) as Place
//            sendSMSUsingNativeSMSComposer(selectedItem.PlaceStr)
//        }

        //getPlaceList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == addPlaceActivityCode && resultCode == Activity.RESULT_OK) {
            placeList = dbHelper.getPlaceDetailData()
            placeAdapter = PlaceAdapter(placeList, this, this)
            placeListRecyclerView!!.adapter = placeAdapter
            placeAdapter.notifyDataSetChanged()
        }
//        if(requestCode == addPlaceActivityCode && resultCode == Activity.RESULT_OK) {
//            placeList = dbHelper.getPlaceDetailData()
//            placeAdapter = PlaceAdapter(R.layout.placelistlayout, placeList)
//            placeListView?.adapter = placeAdapter
//            placeListView!!.invalidateViews()
//        }
//        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.topmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.newItem -> {
                startActivityForResult(Intent(this, PlaceEditActivity::class.java), addPlaceActivityCode)
            }

            R.id.sysItem -> {
                startActivity(Intent(this, SystemActivity::class.java))
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when(keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                return false
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    fun getPlaceList() {

        //placeList.add(Place(1, "台鐵-松竹車站", "場所代碼：177289824657848 本次實聯簡訊限防疫目的使用。"))
        //placeList = dbHelper.getPlaceDetailData()
        //placeAdapter = PlaceAdapter(R.layout.placelistlayout, placeList)
        //placeListView?.adapter = placeAdapter
    }

//    fun sendSMSUsingNativeSMSComposer(smsStr: String) {
//        val uri = Uri.parse("smsto:1922")
//        val intent = Intent(Intent.ACTION_SENDTO, uri)
//        intent.putExtra("sms_body", smsStr)
//        startActivity(intent)
//    }
}

