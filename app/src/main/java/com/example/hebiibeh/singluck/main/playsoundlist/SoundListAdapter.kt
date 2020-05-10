package com.example.hebiibeh.singluck.main.playsoundlist

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hebiibeh.singluck.R
import com.example.hebiibeh.singluck.model.RecSoundInfoData
import io.realm.RealmResults

class SoundListAdapter(realmResults: RealmResults<RecSoundInfoData>) :
    RecyclerView.Adapter<SoundListViewHolder>() {

    private val rResults: RealmResults<RecSoundInfoData> = realmResults

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sound_list_result, parent, false)
        return SoundListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return rResults.size
    }

    override fun onBindViewHolder(holder: SoundListViewHolder, position: Int) {

        val recSoundData = rResults[position]
        holder.soundName?.text = recSoundData?.fileNameNoExtension

        // アイテムリストタップ時、タップした音声を再生する。
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, PlaySoundListService::class.java)
            intent.putExtra("selectedFileNameNoExtension", holder.soundName?.text.toString())
            intent.putExtra("selectedPosition", position)
            it.context.startService(intent)
        }
    }
}