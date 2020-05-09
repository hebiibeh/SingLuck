package com.example.hebiibeh.singluck.main.playsoundlist

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.sound_list_result.view.*

class SounListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var soundName: TextView? = null

    init {
        soundName = itemView.soundNameTxt
    }
}