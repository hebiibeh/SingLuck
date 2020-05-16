package com.example.hebiibeh.singluck.main.recsound

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.hebiibeh.singluck.R
import com.example.hebiibeh.singluck.common.Constants
import com.example.hebiibeh.singluck.common.SingLuckCommonUtil
import com.example.hebiibeh.singluck.model.RecSoundInfoData
import io.realm.Realm
import io.realm.kotlin.where
import java.io.File

// 録音終了時に表示する保存確認ダイアログ
class SaveConfirmDialogFragment : DialogFragment() {

    private lateinit var util: SingLuckCommonUtil
    private var realm = Realm.getDefaultInstance()
    private var targetSoundId: Long? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        util =
            SingLuckCommonUtil(this.requireContext())
        targetSoundId = arguments?.getLong("nextSoundId")
        val builder = AlertDialog.Builder(activity)
        val inputFileNameEdTxt = EditText(this.context)// TODO:ファイル名入力時の入力制限、入力チェックが必要
        initView(builder, inputFileNameEdTxt)
        initBtn(builder, inputFileNameEdTxt)

        return builder.create()
    }

    private fun initView(
        builder: AlertDialog.Builder,
        inputFileNameEdTxt: EditText
    ) {
        inputFileNameEdTxt.setText(util.generateDefaultFileNameNoExtension(targetSoundId))
        builder.setView(inputFileNameEdTxt)
        builder.setMessage(R.string.save_confirm)
    }

    private fun initBtn(
        builder: AlertDialog.Builder,
        inputFileNameEdTxt: EditText
    ) {
        // 保存ボタンを押下した場合、ダイアログに入力されたファイル名でDBと実ファイル名を更新
        builder.setPositiveButton(R.string.positive_save_btn) { _, _ ->
            renameRecFile(inputFileNameEdTxt.text.toString())
        }
        // キャンセルボタンを押下した場合、録音開始時に作成したDBと実ファイルを削除
        builder.setNegativeButton(R.string.negative_save_btn) { _, _ ->
            rollBackSavedRecSound()
        }
    }

    private fun renameRecFile(fileNameNoExtension: String) {

        // DB(RecSoundInfo)のファイル名更新
        realm.executeTransaction {
            val targetRecSound =
                realm.where<RecSoundInfoData>().equalTo("soundId", targetSoundId).findFirst()
            targetRecSound?.fileName = fileNameNoExtension + Constants.recFileExtension
            targetRecSound?.fileNameNoExtension = fileNameNoExtension
        }

        // 実ファイルのファイル名更新
        val recFile =
            File(util.generateDefaultRecFilePath(targetSoundId))
        if (recFile.exists()) {
            recFile.renameTo(File(util.generateRecFilePath(fileNameNoExtension)))
        }
    }

    private fun rollBackSavedRecSound() {

        // DB(RecSoundInfo)のデータ削除
        realm.executeTransaction {
            val targetRecSound =
                realm.where<RecSoundInfoData>().equalTo("soundId", targetSoundId).findAll()
            targetRecSound.deleteFromRealm(0)
        }

        // 実ファイルの削除
        val recFile =
            File(util.generateDefaultRecFilePath(targetSoundId))
        if (recFile.exists()) {
            recFile.delete()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}