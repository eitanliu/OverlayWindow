package com.example.overlay

import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.eitanliu.overlay.OverlayWindow
import com.example.overlay.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {

        private const val AREA_VIEW_COLOR = Color.RED
        private const val AREA_VIEW_SIZE = 100
    }

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContentView(R.layout.activity_main)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.button.setOnClickListener {
            fixWindow()
            leftTopWindow()
            rightBottomWindow()
        }

        binding.button2.setOnClickListener {
            startActivity<TestActivity>()
        }
    }

    private fun fixWindow() {
        val window = window
        val decorView = window.decorView
        val layoutParams = decorView.layoutParams
        (layoutParams as? WindowManager.LayoutParams)?.apply {
            // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            //     layoutInDisplayCutoutMode = WindowManager.LayoutParams
            //         .LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER
            // }
        }
        window.windowManager.updateViewLayout(decorView, decorView.layoutParams)
    }

    private fun leftTopWindow() {
        val ltWindow = OverlayWindow(View(this), windowManager)
        val decorView = ltWindow.view
        decorView.rootWindowInsets

        val decorFitsSystemWindows = true
        // val stableFlag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        // val stableFlag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
        //         View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
        //         View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        // val sysUiVis = decorView.getSystemUiVisibility()
        // decorView.setSystemUiVisibility(
        //     if (decorFitsSystemWindows)
        //         sysUiVis and stableFlag.inv()
        //     else sysUiVis or stableFlag
        // )
        ltWindow.view.setBackgroundColor(AREA_VIEW_COLOR)
        ltWindow.updateLayoutParams {
            width = AREA_VIEW_SIZE
            height = AREA_VIEW_SIZE
            type = WindowManager.LayoutParams.TYPE_APPLICATION
            flags = flags or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                    // WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS

            if (decorFitsSystemWindows) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    layoutInDisplayCutoutMode = WindowManager.LayoutParams
                        .LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    layoutInDisplayCutoutMode = WindowManager.LayoutParams
                        .LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    layoutInDisplayCutoutMode = WindowManager.LayoutParams
                        .LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                }
            }
        }
        ltWindow.attachView()
    }


    private fun rightBottomWindow() {
        val rbWindow = OverlayWindow(View(this), windowManager)
        rbWindow.view.setBackgroundColor(Color.BLUE)
        rbWindow.updateLayoutParams {
            width = AREA_VIEW_SIZE
            height = AREA_VIEW_SIZE
            gravity = Gravity.RIGHT or Gravity.BOTTOM
            type = WindowManager.LayoutParams.TYPE_APPLICATION
            flags = flags or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                    // WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        }
        rbWindow.attachView()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }
}