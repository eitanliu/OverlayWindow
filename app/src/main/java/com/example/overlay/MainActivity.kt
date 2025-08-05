package com.example.overlay

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
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
        setContentView(R.layout.activity_main)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.button.setOnClickListener {
            leftTopWindow()
            rightBottomWindow()
        }

        binding.button2.setOnClickListener {

        }
    }

    private fun leftTopWindow() {
        val ltWindow = OverlayWindow(View(this), windowManager)
        ltWindow.view.setBackgroundColor(AREA_VIEW_COLOR)
        ltWindow.updateLayoutParams {
            width = AREA_VIEW_SIZE
            height = AREA_VIEW_SIZE
            type = WindowManager.LayoutParams.TYPE_APPLICATION
            flags = flags or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                    // WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                layoutInDisplayCutoutMode = WindowManager.LayoutParams
                    .LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                layoutInDisplayCutoutMode = WindowManager.LayoutParams
                    .LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
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
}