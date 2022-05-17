package com.neoalvin.alvintube

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.neoalvin.alvintube.databinding.ActivityMainBinding


class MainActivity : FragmentActivity() {
    private var bundle: Bundle? = null

    private lateinit var activityMainBinding: ActivityMainBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bundle = savedInstanceState
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

//        if (hasPermissions(applicationContext)) {
//            supportFragmentManager.beginTransaction()
//                .setReorderingAllowed(true)
//                .add(R.id.cameraFragment, CameraFragment::class.java, savedInstanceState)
//                .commit()
//        } else {
//            requestPermissions(PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST_CODE)
//        }

        activityMainBinding.btnHockey.apply {
            setOnClickListener {
                startActivity(Intent(applicationContext, HockeyActivity::class.java))
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                supportFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.cameraFragment, CameraFragment::class.java, bundle)
                    .commit()
            } else {
                Toast.makeText(applicationContext, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 10

        private val PERMISSIONS_REQUIRED = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        private fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}