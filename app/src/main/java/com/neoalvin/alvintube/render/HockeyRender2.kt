package com.neoalvin.alvintube.render

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.neoalvin.alvintube.R
import com.neoalvin.alvintube.model.Mallet
import com.neoalvin.alvintube.model.Table
import com.neoalvin.alvintube.program.ColorShaderProgram
import com.neoalvin.alvintube.program.TextureShaderProgram
import com.neoalvin.alvintube.utils.MatrixHelper
import com.neoalvin.alvintube.utils.TextureHelper
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class HockeyRender2(context: Context) : GLSurfaceView.Renderer {
    private var context = context
    private var table = Table()
    private var mallet = Mallet()
    private lateinit var textureShaderProgram: TextureShaderProgram
    private lateinit var colorShaderProgram: ColorShaderProgram
    private var textureId: Int = 0

    private val uMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        textureShaderProgram = TextureShaderProgram(context)
        colorShaderProgram = ColorShaderProgram(context)

        textureId = TextureHelper.loadTexture(context, R.mipmap.test)
    }

    override fun onSurfaceChanged(gl10: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        MatrixHelper.perspectiveM(projectionMatrix, 45f, width.toFloat() / height.toFloat(), 1f, 100f)
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -2.5f) //这个距离自己喜欢多大就多大
        Matrix.rotateM(modelMatrix, 0, -30f, 1f, 0f, 0f) //这个角度也是
        Matrix.multiplyMM(uMatrix, 0, projectionMatrix, 0, modelMatrix, 0)
    }

    override fun onDrawFrame(gl10: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        textureShaderProgram.userProgram()
        textureShaderProgram.setUniforms(uMatrix, textureId)
        table.bindData(textureShaderProgram)
        table.draw()
        colorShaderProgram.userProgram()
        colorShaderProgram.setUniforms(uMatrix)
        mallet.bindData(colorShaderProgram)
        mallet.draw()
    }
}