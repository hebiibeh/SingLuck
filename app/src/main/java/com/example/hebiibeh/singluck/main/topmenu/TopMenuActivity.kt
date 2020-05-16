package com.example.hebiibeh.singluck.main.topmenu

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hebiibeh.singluck.R
import com.example.hebiibeh.singluck.common.CheckPermission
import com.example.hebiibeh.singluck.common.Constants
import com.example.hebiibeh.singluck.main.MeasurePitchActivity
import com.example.hebiibeh.singluck.main.searchsound.SearchSoundActivity
import com.example.hebiibeh.singluck.main.recsound.RecSoundActivity
import kotlinx.android.synthetic.main.activity_top_menu.*

class TopMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_menu)

        checkPermissions(this)
        initBtn()
    }

    private fun initBtn() {
        goRecSoundBtn.setOnClickListener {
            val intent = Intent(this, RecSoundActivity::class.java)
            startActivity(intent)
        }
        goSearchSoundBtn.setOnClickListener {
            val intent = Intent(this, SearchSoundActivity::class.java)
            startActivity(intent)
        }
        goMeasurePitchBtn.setOnClickListener {
            val intent = Intent(this, MeasurePitchActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (permissions.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // パーミッションの要求が許可された場合、何もしない
        } else {
            // パーミッション未許可の場合、アプリを終了
            finish()
        }
    }

    private fun checkPermissions(topMenuActivity: TopMenuActivity) {
        val cp = CheckPermission(
            this,
            Constants.permissionList
        )
        cp.checkAndRequestPermission()
    }
}