package com.example.hebiibeh.singluck

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import io.realm.Realm
import io.realm.kotlin.where
import java.io.File

class RecSaveConfirmDialogFragment : DialogFragment() {

    private lateinit var realm: Realm
    private var targetSoundId = 0L

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        realm = Realm.getDefaultInstance()


        targetSoundId =
            (realm.where(RecSoundData::class.java).max("soundId") ?: 0L).toLong()

        val builder = AlertDialog.Builder(activity)
        val editFileNameTxt = EditText(this.context)
        editFileNameTxt.setText(Constants.recFileName + targetSoundId.toString() + Constants.soundFileExtension)
        builder.setView(editFileNameTxt)

        builder.setMessage(R.string.rec_save_confirm_txt)
        builder.setPositiveButton(R.string.dialog_btn_ok) { dialog, _ ->
            dialogButtonClickListener(
                editFileNameTxt.text.toString()
            )
        }
        builder.setNegativeButton(R.string.dialog_btn_cancel) { _, _ ->
            dialogButtonClickListener(
                null
            )
        }
        return builder.create()
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    private fun dialogButtonClickListener(fileName: String?) {

        // 録音データは保存済みのため、何もしない
        if (fileName != null) {
            updateRecFileName(fileName)
        }
        // 保存済みの録音データを削除する
        else {
            rollBackRecSound()
        }
    }

    private fun updateRecFileName(fileName: String) {


        realm.executeTransaction {
            val targetRecSound =
                realm.where<RecSoundData>().equalTo("soundId", targetSoundId).findFirst()

            targetRecSound?.fileName = fileName
        }
        val recFile =
            File(this.context?.filesDir.toString() + "/" + Constants.recFileName + targetSoundId + Constants.soundFileExtension)
        if (recFile.exists()) {
            recFile.renameTo(File(this.context?.filesDir.toString() + "/" + fileName))
        }
    }

    private fun rollBackRecSound() {

        realm.executeTransaction {
            val targetRecSound =
                realm.where<RecSoundData>().equalTo("soundId", targetSoundId).findAll()
            targetRecSound.deleteFromRealm(0)
        }
    }
}