package andystudio.sms2cdc

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SystemActivity : AppCompatActivity() {

    var versionTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_system)

        versionTextView = findViewById(R.id.tv_Version)
        val pInfo = this.packageManager.getPackageInfo(this.packageName, 0)
        versionTextView!!.text = "Ver. ${pInfo.versionName}"
    }
}