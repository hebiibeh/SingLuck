package com.example.hebiibeh.singluck.common

import android.content.Context
import java.io.File

// ファイル操作用クラス
class FileManageUtils(context: Context, _extension: String?) {

    private var fileDir = ""

    // アプリごとに既定の拡張子を設定
    private val defaultExtension = Constants.recFileExtension
    private val extension = _extension ?: defaultExtension

    init {
        fileDir = context.filesDir.toString()
    }

    // TODO:File作成
    fun create(fileName: String): Boolean {
        val fileWithExtension = toFileWithExtension(fileName)
        return false
    }

    fun rename(oldFileName: String, newFileName: String): Boolean {
        val oldFileWithExtension = toFileWithExtension(oldFileName)
        val newFileWithExtension = toFileWithExtension(newFileName)

        val targetFile = File(getFilePath(oldFileWithExtension))
        if (targetFile.exists()) {
            targetFile.renameTo(File(getFilePath(newFileWithExtension)))
            return true
        }
        return false
    }

    fun delete(fileName: String): Boolean {
        val fileWithExtension = toFileWithExtension(fileName)

        val targetFile = File(getFilePath(fileWithExtension))
        if (targetFile.exists()) {
            targetFile.delete()
            return true
        }
        return false
    }

    // TODO:ファイルの存在チェック
    fun isExists(fileName: String): Boolean {
        val fileWithExtension = toFileWithExtension(fileName)
        return false
    }

    private fun getFilePath(fileName: String): String {
        return "$fileDir/$fileName"
    }

    // 引数のファイル名の拡張子の有無に関わらず、拡張子込みのファイル名を返却する。
    private fun toFileWithExtension(fileName: String): String {
        if (fileName.endsWith(extension)) {
            // 拡張子が既に付与されているため何もしない
            return fileName
        }
        return fileName + extension
    }
}