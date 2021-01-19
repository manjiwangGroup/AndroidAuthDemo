package com.mjw.android.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.mjw.android.auth.lib.MjwAuthApi

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    companion object {
        private const val REQUEST_CODE_MJW_AUTH = 1000
    }

    private val mjwAuthApi by lazy { MjwAuthApi(this) }

    private lateinit var mAuthResultTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sc = ScrollView(this)
        val ll = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        sc.addView(ll)

        setContentView(sc)

        MjwAuthApi.init("rCJI0yAo")

        ll.addView(Button(this).apply {
            text = "判断是否安装 APP"
            setOnClickListener {
                val isInstalled = mjwAuthApi.isMJAppInstalled()
                Toast.makeText(
                    this@MainActivity,
                    "${if (isInstalled == true) "安装了" else "未安装"}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        ll.addView(Button(this).apply {
            text = "到系统浏览器下载APP"
            setOnClickListener {
                mjwAuthApi.downloadAppWithSystemBrowser()
            }
        })

        ll.addView(Button(this).apply {
            text = "登录授权"
            setOnClickListener {
                mjwAuthApi.sendAuthRequest(
                    "manji_userInfo",
                    REQUEST_CODE_MJW_AUTH,
                    "authDemo_${System.currentTimeMillis()}"
                )
            }
        })
        mAuthResultTextView = TextView(this)
        ll.addView(mAuthResultTextView)

        ll.addView(Button(this).apply {
            text = "在 Fragment 中使用"
            setOnClickListener {
                startActivity(Intent(this@MainActivity, FragmentUseActivity::class.java))
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_MJW_AUTH) {
            if (resultCode == RESULT_OK) {

                //                    "appSecret": "8cdf939e5d557fc374d2e3310e6ee641a2bbe774"
                if (data != null) {
                    val result = mjwAuthApi?.parseAuthResult(data)
                    Log.i(TAG, "onActivityResult: $result")
                    mAuthResultTextView.text = "$result"
                    if (result == null) {
                        Toast.makeText(this, "授权信息为空", Toast.LENGTH_LONG).show()
                        return
                    }
                    if (result.errcode == "1001") {
                        Toast.makeText(this, result.errstr, Toast.LENGTH_LONG).show()
                    }
                    if (result.errcode == "1") {
                        Toast.makeText(this, "授权成功 code = ${result.code}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}