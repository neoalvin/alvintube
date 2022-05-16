package com.neoalvin.alvintube

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.*
import android.media.MediaCodec
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Range
import android.util.Size
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.neoalvin.alvintube.databinding.FragmentCameraBinding
import com.neoalvin.alvintube.utils.getPreviewOutputSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.lang.RuntimeException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CameraFragment : Fragment() {
    private lateinit var fragmentCameraBinding: FragmentCameraBinding

    private val cameraManager: CameraManager by lazy {
        var context = requireContext().applicationContext
        context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    private lateinit var cameraDevice: CameraDevice

    private lateinit var captureSession: CameraCaptureSession

    private val cameraId: String by lazy {
        cameraManager.cameraIdList[0]
    }

    private val recorderSurface: Surface by lazy {
        val surface = MediaCodec.createPersistentInputSurface()

        createRecorder(surface).apply {
            prepare()
            release()
        }

        surface
    }

    private val recorder: MediaRecorder by lazy { createRecorder(recorderSurface) }

    private val cameraThread = HandlerThread("CameraThread").apply { start() }

    private val cameraHandler = Handler(cameraThread.looper)

    private val characteristics: CameraCharacteristics by lazy {
        cameraManager.getCameraCharacteristics(cameraId)
    }

    private val previewRequest: CaptureRequest by lazy {
        // Capture request holds references to target surfaces
        captureSession.device.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW).apply {
            // Add the preview surface target
            addTarget(fragmentCameraBinding.surfaceView.holder.surface)
        }.build()
    }

    private val recordRequest: CaptureRequest by lazy {
        // Capture request holds references to target surfaces
        captureSession.device.createCaptureRequest(CameraDevice.TEMPLATE_RECORD).apply {
            addTarget(fragmentCameraBinding.surfaceView.holder.surface)
            addTarget(recorderSurface)
            // Sets user requested FPS for all targets
            set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, Range(30, 30))
        }.build()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentCameraBinding = FragmentCameraBinding.inflate(inflater, container, false)
        return fragmentCameraBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragmentCameraBinding.surfaceView.holder.addCallback(
            object: SurfaceHolder.Callback {
                override fun surfaceCreated(holder: SurfaceHolder) {
                    var previewSize = getPreviewOutputSize(fragmentCameraBinding.surfaceView.display,
                    characteristics, SurfaceHolder::class.java)
                    fragmentCameraBinding.surfaceView.holder.setFixedSize(previewSize.width, previewSize.height)
                    fragmentCameraBinding.surfaceView.post {
                        initCamera()
                    }
                }

                override fun surfaceChanged(
                    holder: SurfaceHolder,
                    format: Int,
                    width: Int,
                    height: Int
                ) = Unit

                override fun surfaceDestroyed(holder: SurfaceHolder) = Unit

            })
    }

    private fun initCamera() = lifecycleScope.launch(Dispatchers.Main) {
        cameraDevice = openCamera()
        captureSession = createCaptureSession()
        captureSession.setRepeatingRequest(previewRequest, null, cameraHandler)
    }

    private fun createRecorder(surface: Surface) = MediaRecorder().apply {
        setAudioSource(MediaRecorder.AudioSource.MIC)
        setVideoSource(MediaRecorder.VideoSource.SURFACE)
        setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        setOutputFile(requireContext().cacheDir.absolutePath + getFileName())
        setVideoEncodingBitRate(RECORDER_VIDEO_BITRATE)
        setVideoFrameRate(30)
        setVideoSize(1080, 1920)
        setVideoEncoder(MediaRecorder.VideoEncoder.H264)
        setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        setInputSurface(surface)
    }

    @SuppressLint("MissingPermission")
    private suspend fun openCamera(): CameraDevice =
        suspendCancellableCoroutine { cont ->
            cameraManager.openCamera(cameraId, object: CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    cont.resume(camera)
                }

                override fun onDisconnected(camera: CameraDevice) {
                    Log.w(TAG, "Camera disconnected.")
                }

                override fun onError(camera: CameraDevice, error: Int) {
                    val msg = when(error) {
                        ERROR_CAMERA_DEVICE -> "Fatal (device)"
                        ERROR_CAMERA_DISABLED -> "Device policy"
                        ERROR_CAMERA_IN_USE -> "Camera in use"
                        ERROR_CAMERA_SERVICE -> "Fatal (service)"
                        ERROR_MAX_CAMERAS_IN_USE -> "Maximum cameras in use"
                        else -> "Unknown"
                    }

                    val exception = RuntimeException("Camera error: $error $msg")
                    Log.e(TAG, exception.message, exception)
                    if (cont.isActive) cont.resumeWithException(exception)
                }

            }, cameraHandler)
        }

    private suspend fun createCaptureSession(): CameraCaptureSession =
        suspendCoroutine { cont ->
            val targets = listOf(fragmentCameraBinding.surfaceView.holder.surface,
                recorderSurface) as MutableList<Surface>
            cameraDevice.createCaptureSession(targets, object:
                CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    cont.resume(session)
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {
                    val exception = RuntimeException("Camera ${cameraDevice.id} session configuration failed")
                    Log.e(TAG, exception.message, exception)
                    cont.resumeWithException(exception)
                }

            }, cameraHandler)
        }

    companion object {
        private const val TAG: String = "CameraFragment"

        private const val RECORDER_VIDEO_BITRATE: Int = 10_000_000

        private fun getFileName(): String {
            var date = Date()
            var simpleDateFormat = SimpleDateFormat("yyyy-mm-dd hh:MM:ss")
            return simpleDateFormat.format(date) + ".mp4"
        }
    }
}