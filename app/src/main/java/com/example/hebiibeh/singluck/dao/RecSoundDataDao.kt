package com.example.hebiibeh.singluck.dao

import com.example.hebiibeh.singluck.model.RecSoundData
import io.realm.Realm
import io.realm.kotlin.where

class RecSoundDataDao {
    private val realm = Realm.getDefaultInstance()

    fun findAll(): MutableList<RecSoundData> {
        val results = realm.where<RecSoundData>().findAll()
        return results.toMutableList()
    }

    fun findBySoundId(soundId: Long): RecSoundData? {
        val result = realm.where<RecSoundData>().equalTo("soundId", soundId).findFirst()
        return result
    }

    fun updateBySoundId(newData: RecSoundData) {
        realm.executeTransaction {
            var oldData =
                realm.where<RecSoundData>().equalTo("soundId", newData.soundId).findFirst()
            // TODO:この方法で更新できるか？？？
            oldData = newData
        }
    }

    fun deleteBySoundId(soundId: Long) {
        realm.executeTransaction {
            var soundData =
                realm.where<RecSoundData>().equalTo("soundId", soundId).findFirst()
            soundData?.deleteFromRealm()
        }
    }
}