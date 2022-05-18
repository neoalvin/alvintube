package com.neoalvin.alvintube.program

import android.content.Context
import android.opengl.GLES20
import com.neoalvin.alvintube.R


open class CubeShaderProgram(context: Context) : ShaderProgram(context, R.raw.cube_vertex_shader, R.raw.cube_fragment_shader) {
    val uMatrixLocation = GLES20.glGetUniformLocation(programId, U_MATRIX)
    val aColorLocation = GLES20.glGetAttribLocation(programId, A_COLOR)
    val aPositionLocation = GLES20.glGetAttribLocation(programId, A_POSITION)

    fun setUniforms(matrix: FloatArray?) {
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
    }

    companion object {
        protected const val U_MATRIX = "u_Matrix"
        protected const val A_POSITION = "a_Position"
        protected const val A_COLOR = "a_Color"
    }
}