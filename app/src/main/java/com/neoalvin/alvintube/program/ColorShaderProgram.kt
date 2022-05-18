package com.neoalvin.alvintube.program

import android.content.Context
import android.opengl.GLES20
import com.neoalvin.alvintube.R


class ColorShaderProgram(context: Context) : ShaderProgram(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader) {
    val uMatrixLocation: Int
    val aPositionLocation: Int
    val aColorLocation: Int

    init {
        uMatrixLocation = GLES20.glGetUniformLocation(programId, U_MATRIX)
        aColorLocation = GLES20.glGetAttribLocation(programId, A_COLOR)
        aPositionLocation = GLES20.glGetAttribLocation(programId, A_POSITION)
    }

    fun setUniforms(matrix: FloatArray?) {
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
    }

    companion object {
        protected const val U_MATRIX = "u_Matrix"
        protected const val A_POSITION = "a_Position"
        protected const val A_COLOR = "a_Color"
    }
}