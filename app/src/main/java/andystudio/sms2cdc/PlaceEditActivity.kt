package andystudio.sms2cdc

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import andystudio.sms2cdc.Models.PlaceDBHelper
import com.google.zxing.integration.android.IntentIntegrator

class PlaceEditActivity : AppCompatActivity() {

    private lateinit var dbHelper: PlaceDBHelper

    var getPlaceStrButton: Button? = null
    var sendSMSButton: Button? = null
    var savePlaceStrButton: Button? = null
    var cancelEditButton: Button? = null
    var deletePlaceButton: Button? = null

    var placeNameEditText: EditText? = null
    var placeStrEditText: EditText? = null

    val customizedRequestCode = 0x0000ffff

    var placeNo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_edit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        // Get Database Instance
        dbHelper = PlaceDBHelper(this)

        placeNameEditText = findViewById(R.id.et_PlaceName)
        placeStrEditText = findViewById(R.id.et_PlaceStr)

        getPlaceStrButton = findViewById(R.id.btn_ScanQRCode)
        getPlaceStrButton!!.setOnClickListener {
            var scanner = IntentIntegrator(this)
            scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            scanner.setPrompt(getString(R.string.tv_ScanMsg))
            scanner.setCameraId(0)
            scanner.setBeepEnabled(false)
            scanner.initiateScan()
        }

        sendSMSButton = findViewById(R.id.btn_SendSMS)
        sendSMSButton!!.setBackgroundColor(Color.rgb(125, 255, 110))
        sendSMSButton!!.setOnClickListener {
            var smsMsg = placeStrEditText!!.text
            if (smsMsg.isNullOrEmpty()) {
                Toast.makeText(this, "請先掃描或輸入場所代碼", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            sendSMSUsingNativeSMSComposer(smsMsg.toString())
        }

        // Save Button
        savePlaceStrButton = findViewById(R.id.btn_SavePlace)
        savePlaceStrButton!!.setOnClickListener {
            savePlaceData()
        }

        // Delete Button
        deletePlaceButton = findViewById(R.id.btn_DeletePlace)
        deletePlaceButton!!.setBackgroundColor(Color.rgb(255, 55, 55))
        deletePlaceButton!!.setOnClickListener {
            deletePlace()
        }

        // Cancel Button
        cancelEditButton = findViewById(R.id.btn_CancelEdit)
        cancelEditButton!!.setOnClickListener {
            cancelSavePlace()
        }

        intent?.extras?.let {
            placeNo = it.getString("placeNo").toString()
            placeNameEditText!!.setText(it.getString("placeName"))
            placeStrEditText!!.setText("只得修改場所名稱")
            placeStrEditText!!.isEnabled = false
        }
    }

    fun sendSMSUsingNativeSMSComposer(smsStr: String) {
        val uri = Uri.parse("smsto:1922")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        intent.putExtra("sms_body", smsStr)
        ContextCompat.startActivity(this, intent, null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != customizedRequestCode && requestCode != IntentIntegrator.REQUEST_CODE) {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data)
            return
        }

        val result = IntentIntegrator.parseActivityResult(resultCode, data)
        if (result.contents == null) {
            Toast.makeText(this, getString(R.string.scan_err_null), Toast.LENGTH_LONG).show()
        } else {
            Log.d("ScanPatientActivity", "Scanned")
            setPlaceMessage(result.contents)
            // Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun setPlaceMessage(scannerStr: String) {

        var smsMsg = scannerStr.substring(11, scannerStr.length)
        placeStrEditText!!.setText(smsMsg)

        AlertDialog.Builder(this)
            .setTitle("傳送訊息?")
            .setMessage("是否需要發送場所代碼簡訊")
            .setPositiveButton("傳送") { _, _ ->
                sendSMSUsingNativeSMSComposer(smsMsg)
            }
            .setNeutralButton("取消", null)
            .show()

        // smsto:1922:場所代碼：177289824657848 本次實聯簡訊限防疫目的使用。 // 松竹車站
        // smsto:1922:場所代碼：188393939883899 本次實聯簡訊限防疫目的使用。 // 大慶車站
        // SMSTO:1922:場所代碼：2770 2513 6512 291 本次簡訊實聯限防疫目的使用。 // 全家台中汝川店
    }

    fun savePlaceData() {
        var placeName = placeNameEditText!!.text
        var smsMsg = placeStrEditText!!.text

        if (placeName.isNullOrEmpty() || smsMsg.isNullOrEmpty()) {
            Toast.makeText(this, "請輸入場所名稱及場所代碼", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            if (placeNo.isNullOrEmpty()) {
                dbHelper.addPlaceDetail(placeName.toString(), smsMsg.toString(), 0.0, 0.0)
            } else {
                dbHelper.updatePlaceDetail(placeNo.toInt(), placeName.toString())
            }

            placeNameEditText!!.text = null
            placeStrEditText!!.text = null
            setResult(Activity.RESULT_OK)
            finish()

        } catch (ex: Exception) {
            Toast.makeText(this, "新增失敗", Toast.LENGTH_SHORT).show()
        }
    }

    fun deletePlace() {
        if (!placeNo.isNullOrEmpty()) {
            dbHelper.deletePlaceDetail(placeNo.toInt())
            placeNameEditText!!.text = null
            placeStrEditText!!.text = null
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            Toast.makeText(this, "請選擇欲刪除的地點", Toast.LENGTH_SHORT).show()
        }

    }

    fun cancelSavePlace() {
        placeNameEditText!!.text = null
        placeStrEditText!!.text = null
        finish()
    }
}