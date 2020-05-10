package com.example.hebiibeh.singluck.main.searchsound

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.hebiibeh.singluck.model.RecSoundInfoData

//
class SearchFileNameSpinnerAdapter(
    context: Context,
    resource: Int,
    objects: Array<out RecSoundInfoData>
) : ArrayAdapter<RecSoundInfoData>(context, resource, objects) {

    // ドロップダウンリストから1項目が選択された後の表示内容を設定
    // （TextViewと同じ表示の時。下にぺろんと出ていない時。）
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val textView = super.getView(position, convertView, parent) as TextView
        textView.text = getItem(position)?.fileNameNoExtension
        return textView
    }

    // ドロップダウンリスト表示時の表示内容を設定
    // （タップして下にぺろんとリストが並んだ時）
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val textView = super.getDropDownView(position, convertView, parent) as TextView
        textView.text = getItem(position)?.fileNameNoExtension
        return textView
    }

}