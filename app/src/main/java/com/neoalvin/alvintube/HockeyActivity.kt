package com.neoalvin.alvintube

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import com.neoalvin.alvintube.render.CubeRenderer
import com.neoalvin.alvintube.render.HockeyRender
import com.neoalvin.alvintube.render.HockeyRender2
import com.neoalvin.alvintube.render.PanoramaRenderer

class HockeyActivity : Activity() {
    private lateinit var glSurfaceView: GLSurfaceView

    private var supportEls = false

    private var renderSet = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        glSurfaceView = GLSurfaceView(applicationContext)

        var activityManager = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        supportEls = (activityManager.deviceConfigurationInfo.reqGlEsVersion >= 0x20000)
            || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                && (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")))

        if (supportEls) {
            glSurfaceView.setEGLContextClientVersion(2)
            glSurfaceView.setRenderer(PanoramaRenderer(applicationContext))
            renderSet = true
            setContentView(glSurfaceView)
        } else {
            Toast.makeText(applicationContext, "Device does not support OpenGL.ES 3.0", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        if (renderSet) {
            glSurfaceView.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (renderSet) {
            glSurfaceView.onPause()
        }
    }

    companion object {
    }
}