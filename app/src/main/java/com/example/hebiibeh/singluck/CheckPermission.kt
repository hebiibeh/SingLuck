package com.example.hebiibeh.singluck

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class CheckPermission(_activity: Activity, _permissionList: List<String>) {

    // 呼び出し元のActivity
    private val activity: Activity = _activity

    // 要求したい許可の種類 (例：Manifest.permission.ACCESS_FINE_LOCATION)
    private val targetPermissionList = _permissionList

    // 重複しない数ならなんでもよいらしい。今回は一回しか使わないためとりあえず1を設定。
    private val requestCode = 1

    fun checkAndRequestPermission() {

        val requestPermissionList = mutableListOf<String>()

        for (i in targetPermissionList.indices) {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    targetPermissionList[i]
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // 許可済みの場合、何もしない
            } else {
                requestPermissionList.add(requestLocationPermission(targetPermissionList[i]))
            }
        }

        ActivityCompat.requestPermissions(
            activity,
            requestPermissionList.toTypedArray(),
            requestCode
        )
    }

    // TODO:許可を求め、拒否されたいた場合にさらにもう一度拒否（今後ダイアログを表示しない）するとアプリが起動できなくなる→再インストールしてください
    private fun requestLocationPermission(targetPermission: String): String {

        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                targetPermission
            )
        ) {
            // まだ許可を求めていない
            return targetPermission
        } else {
            // 許可を求め、拒否されていた場合
            return targetPermission
        }
    }
}