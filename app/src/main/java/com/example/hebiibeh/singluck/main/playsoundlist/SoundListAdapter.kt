package com.example.hebiibeh.singluck.main.playsoundlist

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.recyclerview.widget.RecyclerView
import com.example.hebiibeh.singluck.R
import com.example.hebiibeh.singluck.model.RecSoundData
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults

class SoundListAdapter(realmResults: RealmResults<RecSoundData>) :
    RecyclerView.Adapter<SounListViewHolder>() {

    private val rResults: RealmResults<RecSoundData> = realmResults

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SounListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sound_list_result, parent, false)
        val viewHolder = SounListViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return rResults.size
    }

    override fun onBindViewHolder(holder: SounListViewHolder, position: Int) {
        val recSoundData = rResults[position]
        holder.soundName?.text = recSoundData?.fileNameNoExtension

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, PlaySoundListService::class.java)
            intent.putExtra("selectedFileNameNoExtension", holder.soundName?.text.toString())
            intent.putExtra("selectedPosition", position)
            it.context.startService(intent)
        }
    }
}