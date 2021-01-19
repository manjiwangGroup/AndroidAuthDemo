package com.mjw.android.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class FragmentUseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_fragment_use_activity)

        supportFragmentManager.beginTransaction().replace(R.id.v_fl, FragmentUse())
            .commitNowAllowingStateLoss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        supportFragmentManager.fragments?.forEach {
            it.onActivityResult(requestCode,resultCode, data)
        }
    }
}