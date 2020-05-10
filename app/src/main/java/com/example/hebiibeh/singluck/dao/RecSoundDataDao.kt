package com.example.hebiibeh.singluck.dao

import com.example.hebiibeh.singluck.model.RecSoundInfoData
import io.realm.Realm
import io.realm.kotlin.where

class RecSoundDataDao {
    private val realm = Realm.getDefaultInstance()

    fun findAll(): MutableList<RecSoundInfoData> {
        val results = realm.where<RecSoundInfoData>().findAll()
        return results.toMutableList()
    }

    fun findBySoundId(soundId: Long): RecSoundInfoData? {
        val result = realm.where<RecSoundInfoData>().equalTo("soundId", soundId).findFirst()
        return result
    }

    fun updateBySoundId(newInfoData: RecSoundInfoData) {
        realm.executeTransaction {
            var oldData =
                realm.where<RecSoundInfoData>().equalTo("soundId", newInfoData.soundId).findFirst()
            // TODO:この方法で更新できるか？？？
            oldData = newInfoData
        }
    }

    fun deleteBySoundId(soundId: Long) {
        realm.executeTransaction {
            var soundData =
                realm.where<RecSoundInfoData>().equalTo("soundId", soundId).findFirst()
            soundData?.deleteFromRealm()
        }
    }
}