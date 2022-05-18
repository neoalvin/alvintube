package com.neoalvin.alvintube.program

import android.content.Context
import android.opengl.GLES20
import com.neoalvin.alvintube.R


open class TextureShaderProgram(context: Context) : ShaderProgram(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader) {
    val uMatrixLocation: Int
    val uTextureUnitLocation: Int
    val aPositionLocation: Int
    val aTextureCoordinatesLocation: Int

    init {
        uMatrixLocation = GLES20.glGetUniformLocation(programId, U_MATRIX)
        uTextureUnitLocation = GLES20.glGetUniformLocation(programId, U_TEXTURE_UNIT)
        aPositionLocation = GLES20.glGetAttribLocation(programId, A_POSITION)
        aTextureCoordinatesLocation = GLES20.glGetAttribLocation(programId, A_TEXTURE_COORDINATES)
    }

    fun setUniforms(matrix: FloatArray?, textureId: Int) {
        // 传入变化矩阵到shaderProgram
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
        // 激活纹理单元0
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        // 绑定纹理对象ID
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        // 告诉shaderProgram sampler2D纹理采集器 使用纹理单元0的纹理对象。
        GLES20.glUniform1i(uTextureUnitLocation, 0)
    }

    companion object {
        private const val U_MATRIX = "u_Matrix"
        private const val U_TEXTURE_UNIT = "u_TextureUnit"
        private const val A_POSITION = "a_Position"
        private const val A_TEXTURE_COORDINATES = "a_TextureCoordinates"
    }
}