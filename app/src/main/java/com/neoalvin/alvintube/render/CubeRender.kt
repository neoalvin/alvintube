package com.neoalvin.alvintube.render

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.neoalvin.alvintube.model.Cube
import com.neoalvin.alvintube.program.CubeShaderProgram
import com.neoalvin.alvintube.utils.MatrixHelper
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class CubeRenderer(context: Context) : GLSurfaceView.Renderer {
    private val context = context

    private val modelViewProjectionMatrix = FloatArray(16)
    private val viewProjectionMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    var cube: Cube? = null
    var cubeShaderProgram: CubeShaderProgram? = null

    override fun onSurfaceCreated(gl10: GL10?, eglConfig: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        cube = Cube()
        cubeShaderProgram = CubeShaderProgram(context)
    }


    override fun onSurfaceChanged(gl10: GL10, width: Int, height: Int) {
        GLES20.glViewport(0,0,width,height);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        MatrixHelper.perspectiveM(projectionMatrix, 45f, width.toFloat() / height.toFloat(), 1f, 100f);
        Matrix.setLookAtM(viewMatrix, 0,
            4f, 4f, 4f,
            0f, 0f, 0f,
            0f, 1f, 0f);
        Matrix.multiplyMM(viewProjectionMatrix,0,  projectionMatrix,0, viewMatrix,0);
    }

    override fun onDrawFrame(gl10: GL10) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)

        Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, Cube.modelMatrix, 0)
        cubeShaderProgram!!.userProgram()
        cubeShaderProgram!!.setUniforms(modelViewProjectionMatrix)
        cube!!.bindData(cubeShaderProgram!!)
        cube!!.draw()
    }

    init {
        Matrix.setIdentityM(projectionMatrix, 0)
        Matrix.setIdentityM(viewMatrix, 0)
        Matrix.setIdentityM(viewProjectionMatrix, 0)
        Matrix.setIdentityM(modelViewProjectionMatrix, 0)
    }
}