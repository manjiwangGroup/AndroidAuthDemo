package com.mjw.android.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mjw.android.auth.lib.MjwAuthApi

class FragmentUse : Fragment() {

    private val mjwAuthHelper by lazy { MjwAuthApi(this) }


    private lateinit var mAuthResultTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.app_fragment_use, container, false)
        val ll = view.findViewById<LinearLayout>(R.id.v_ll)
        ll.addView(Button(requireContext()).apply {
            text = "判断是否安装 APP"
            setOnClickListener {
                val isInstalled = mjwAuthHelper?.isMJAppInstalled()
                Toast.makeText(
                    requireActivity(),
                    "${if (isInstalled == true) "安装了" else "未安装"}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        ll.addView(Button(requireContext()).apply {
            text = "到系统浏览器下载APP"
            setOnClickListener {
                mjwAuthHelper?.downloadAppWithSystemBrowser()
            }
        })

        ll.addView(Button(requireContext()).apply {
            text = "登录授权"
            setOnClickListener {
                mjwAuthHelper?.sendAuthRequest(
                    "manji_userInfo",
                    1000,
                    "authDemo_${System.currentTimeMillis()}"
                )
            }
        })
        mAuthResultTextView = TextView(requireContext())
        ll.addView(mAuthResultTextView)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = mjwAuthHelper?.parseAuthResult(data)
        mAuthResultTextView.text = "$result"
    }
}