package com.example.overlay

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.overlay.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {

    private var fitWindow = true
    val binding by lazy {
        ActivityTestBinding.inflate(layoutInflater)
    }

    val windowInsetsController by lazy {
        WindowInsetsControllerCompat(window, window.decorView)
    }

    val rootWindowInsetsCompat get() = binding.root.rootWindowInsetsCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnStatusBar.setOnClickListener {
            val windowInsetsCompat = rootWindowInsetsCompat
            if (windowInsetsCompat != null) {
                if (windowInsetsCompat.isVisible(WindowInsetsCompat.Type.statusBars())) {
                    windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())
                } else {
                    windowInsetsController.show(WindowInsetsCompat.Type.statusBars())
                }
            } else {
                windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())
            }
        }
        binding.btnFitWindow.setOnClickListener {
            fitWindow = !fitWindow
            fitWindow()
        }
    }

    private fun fitWindow() {
        // WindowCompat.setDecorFitsSystemWindows(window, fitWindow)
        fitWindow16()
    }

    private fun fitWindow35() {
        // windowInsetsController.systemBarsBehavior =
        //     WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(fitWindow)
        }
    }

    private fun fitWindow30() {
        val decorView = window.decorView
        val maskFlag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        val sysUiVis = decorView.getSystemUiVisibility()
        decorView.setSystemUiVisibility(
            if (fitWindow) sysUiVis and maskFlag.inv() else sysUiVis or maskFlag
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(fitWindow)
        }
    }

    private fun fitWindow16() {
        val decorView = window.decorView
        val maskFlag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        val sysUiVis = decorView.getSystemUiVisibility()
        decorView.setSystemUiVisibility(
            if (fitWindow) sysUiVis and maskFlag.inv() else sysUiVis or maskFlag
        )
    }
}