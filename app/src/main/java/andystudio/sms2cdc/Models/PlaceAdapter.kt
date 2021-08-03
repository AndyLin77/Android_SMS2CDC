package andystudio.sms2cdc.Models

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import andystudio.sms2cdc.R

class PlaceAdapter constructor(private val layout: Int, private val data: ArrayList<Place>)
    : BaseAdapter() {
    private class ViewHolder(v: View) {
        val placeNameTextView: TextView = v.findViewById(R.id.tv_PlaceNameTextView)
    }

    // 回傳項目比數
    override fun getCount() = data.size

    // 回傳某筆項目
    override fun getItem(position: Int) = data[position]

    // 回傳某筆項目ID
    override fun getItemId(position: Int) = 0L

    // 取得畫面
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if(convertView == null) {
            view = View.inflate(parent!!.context, layout, null)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            view = convertView
        }

        holder.placeNameTextView.text = data[position].PlaceName

        return view
    }
}