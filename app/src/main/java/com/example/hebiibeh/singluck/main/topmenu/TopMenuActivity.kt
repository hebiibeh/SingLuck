package com.example.hebiibeh.singluck.main.topmenu

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hebiibeh.singluck.R
import com.example.hebiibeh.singluck.common.CheckPermission
import com.example.hebiibeh.singluck.main.searchsound.SearchSoundActivity
import com.example.hebiibeh.singluck.main.recsound.RecSoundActivity
import kotlinx.android.synthetic.main.activity_top_menu.*

class TopMenuActivity : AppCompatActivity() {

    private val permissionList: List<String> =
        listOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_menu)

        val cp = CheckPermission(
            this,
            permissionList
        )
        cp.checkAndRequestPermission()
        initBtn()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissions.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // 許可された場合、何もしない
        } else {
            finish()
        }
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
    }
}