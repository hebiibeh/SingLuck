package com.example.hebiibeh.singluck.main.recsound

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.hebiibeh.singluck.R
import com.example.hebiibeh.singluck.common.Constants
import com.example.hebiibeh.singluck.common.SingLuckCommonUtil
import com.example.hebiibeh.singluck.model.RecSoundData
import io.realm.Realm
import io.realm.kotlin.where
import java.io.File

class SaveSoundConfirmDialogFragment : DialogFragment() {

    private lateinit var utils: SingLuckCommonUtil
    private lateinit var realm: Realm
    private var targetSoundId = 0L

    fun setTargetSoundId(_targetSoundId: Long) {
        targetSoundId = _targetSoundId
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        realm = Realm.getDefaultInstance()
        utils =
            SingLuckCommonUtil(this.requireContext())

        val builder = AlertDialog.Builder(activity)
        // TODO:ファイル名入力時の入力制限、入力チェックが必要
        val inputFileNameEdTxt = EditText(this.context)
        inputFileNameEdTxt.setText(utils.generateDefaultRecFileNameNoExtension(targetSoundId))
        builder.setView(inputFileNameEdTxt)
        builder.setMessage(R.string.save_confirm)

        builder.setPositiveButton(R.string.positive_save_btn) { _, _ ->
            renameRecFile(inputFileNameEdTxt.text.toString())
        }

        builder.setNegativeButton(R.string.negative_save_btn) { _, _ ->
            rollBackSavedRecSound()
        }
        return builder.create()
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    private fun renameRecFile(fileNameNoExtension: String) {

        val fileName = fileNameNoExtension + Constants.recFileExtension
        // DB(RecSoundData)のファイル名更新
        realm.executeTransaction {
            val targetRecSound =
                realm.where<RecSoundData>().equalTo("soundId", targetSoundId).findFirst()
            targetRecSound?.fileName = fileName
            targetRecSound?.fileNameNoExtension = fileNameNoExtension
        }

        // 実ファイルのファイル名更新
        val recFile =
            File(utils.generateDefaultRecFilePath(targetSoundId))
        if (recFile.exists()) {
            recFile.renameTo(File(utils.generateRecFilePath(fileNameNoExtension)))
        }
    }

    private fun rollBackSavedRecSound() {
        // DB(RecSoundData)のデータ削除
        realm.executeTransaction {
            val targetRecSound =
                realm.where<RecSoundData>().equalTo("soundId", targetSoundId).findAll()
            targetRecSound.deleteFromRealm(0)
        }

        // 実ファイルの削除
        val recFile =
            File(utils.generateDefaultRecFilePath(targetSoundId))
        if (recFile.exists()) {
            recFile.delete()
        }
    }
}