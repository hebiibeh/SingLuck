package com.example.hebiibeh.singluck

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.Date

open class RecordSoundData : RealmObject() {
    @PrimaryKey
    var soundId: Long = 0
    var fileName: String = ""
    var displayName: String = ""
    var createDate: Date = Date()
    var updateDate: Date = Date()
}