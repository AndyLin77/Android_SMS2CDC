package andystudio.sms2cdc.Models

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import andystudio.sms2cdc.MainActivity
import andystudio.sms2cdc.PlaceEditActivity
import andystudio.sms2cdc.R

class PlaceAdapter(private val contacts:ArrayList<Place>, context: Context, activity: MainActivity): RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    var context = context
    var activity = activity
    var addPlaceActivityCode: Int = 1001

    // RecyclerView.ViewHolder Catch View
    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val placeName = v.findViewById<TextView>(R.id.tv_PlaceNameTextView)
        val editImage = v.findViewById<ImageView>(R.id.iv_Edit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.placelistlayout, parent, false)
        return ViewHolder(v)
    }

    // return data count
    override fun getItemCount() = contacts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.placeName.text = contacts[position].PlaceName
        holder.placeName.setOnClickListener {
            sendSMSUsingNativeSMSComposer(contacts[position].PlaceStr)
        }
        holder.editImage.setOnClickListener {
            val intent = Intent(context, PlaceEditActivity::class.java)
            intent.putExtra("placeNo", contacts[position].PlaceNo.toString())
            intent.putExtra("placeName", contacts[position].PlaceName)
            intent.putExtra("placeStr", contacts[position].PlaceStr)
            startActivityForResult(activity, intent, addPlaceActivityCode, null)
        }
    }


    fun sendSMSUsingNativeSMSComposer(smsStr: String) {
        val uri = Uri.parse("smsto:1922")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        intent.putExtra("sms_body", smsStr)
        startActivity(context, intent, null)
    }

}
//class PlaceAdapter constructor(private val layout: Int, private val data: ArrayList<Place>)
//    : BaseAdapter() {
//    private class ViewHolder(v: View) {
//        val placeNameTextView: TextView = v.findViewById(R.id.tv_PlaceNameTextView)
//    }
//
//    // 回傳項目比數
//    override fun getCount() = data.size
//
//    // 回傳某筆項目
//    override fun getItem(position: Int) = data[position]
//
//    // 回傳某筆項目ID
//    override fun getItemId(position: Int) = 0L
//
//    // 取得畫面
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        val view: View
//        val holder: ViewHolder
//
//        if(convertView == null) {
//            view = View.inflate(parent!!.context, layout, null)
//            holder = ViewHolder(view)
//            view.tag = holder
//        } else {
//            holder = convertView.tag as ViewHolder
//            view = convertView
//        }
//
//        holder.placeNameTextView.text = data[position].PlaceName
//
//        return view
//    }
//}