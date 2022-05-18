package com.neoalvin.alvintube.render

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.neoalvin.alvintube.R
import com.neoalvin.alvintube.utils.ShaderHelper
import com.neoalvin.alvintube.utils.TextResourceReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class HockeyRender(context: Context) : GLSurfaceView.Renderer {
    private var vertexShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_vertex_shader)

    private var fragmentShaderSource =
        TextResourceReader.readTextFileFromResource(context, R.raw.simple_fragment_shader)

    // private var uColorLocation: Int? = null

    private var aColorLocation: Int? = null

    private var aPositionLocation: Int? = null

    init {
        vertexData = ByteBuffer
            .allocateDirect(tableVerticesWithTriangles.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer();
        vertexData.put(tableVerticesWithTriangles.toFloatArray())
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        var programId = ShaderHelper.buildProgram(vertexShaderSource, fragmentShaderSource)
        GLES20.glUseProgram(programId)

        // uColorLocation = GLES20.glGetUniformLocation(programId, U_COLOR)
        aPositionLocation = GLES20.glGetAttribLocation(programId, A_POSITION)
        aColorLocation = GLES20.glGetAttribLocation(programId, A_COLOR)

        vertexData.position(0)
        GLES20.glVertexAttribPointer(
            aPositionLocation!!, POSITION_COMPONENT_COUNT,
            GLES20.GL_FLOAT, false, STRIDE, vertexData
        )
        GLES20.glEnableVertexAttribArray(aPositionLocation!!)

        vertexData.position(2);
        GLES20.glVertexAttribPointer(aColorLocation!!, COLOR_COMPONENT_COUNT,
            GLES20.GL_FLOAT, false, STRIDE, vertexData
        )
        GLES20.glEnableVertexAttribArray(aColorLocation!!)

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        // 桌面
        // GLES20.glUniform4f(uColorLocation!!, 1.0f,1.0f,1.0f,1.0f)
        // GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);

        // 分割线
        // GLES20.glUniform4f(uColorLocation!!, 1.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2)

        // 木槌点
        // GLES20.glUniform4f(uColorLocation!!, 0.0f,0.0f,1.0f, 1.0f)
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1)
        // GLES20.glUniform4f(uColorLocation!!, 0.0f,1.0f,0.0f, 1.0f)
        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1)


    }

    companion object {
        // private const val U_COLOR = "u_Color"
        private const val A_COLOR = "a_Color"
        private const val A_POSITION = "a_Position"

        private const val POSITION_COMPONENT_COUNT = 2

        private const val BYTES_PER_FLOAT = 4

        private const val COLOR_COMPONENT_COUNT = 3

        private const val STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT

        private var tableVerticesWithTriangles = arrayOf(
//            // 第一个三角形
//            -0.5f, -0.5f,
//            0.5f, 0.5f,
//            -0.5f, 0.5f,
//            // 第二个三角形
//            -0.5f, -0.5f,
//            0.5f, -0.5f,
//            0.5f, 0.5f,
            // 三角扇
            0.0f, 0.0f, 1f,  1f,  1f,
            -0.5f, -0.5f, 0.7f,0.7f,0.7f,
            0.5f, -0.5f, 0.7f,0.7f,0.7f,
            0.5f, 0.5f, 0.7f,0.7f,0.7f,
            -0.5f, 0.5f, 0.7f,0.7f,0.7f,
            -0.5f, -0.5f, 0.7f,0.7f,0.7f,
            // 中间的分界线
            -0.5f, 0.0f, 1f,  0f,  0f,
            0.5f, 0.0f, 1f,  0f,  0f,
            // 两个摇杆的质点位置
            0.0f, -0.25f, 0f,  0f,  1f,
            0.0f, 0.25f, 1f,  0f,  0f
        )

        private lateinit var vertexData: FloatBuffer
    }
}