package com.neoalvin.alvintube.program

import android.content.Context
import android.opengl.GLES20
import com.neoalvin.alvintube.R

open class BallShaderProgram(context: Context) : ShaderProgram(context, R.raw.panorama_vertex_shader, R.raw.panorama_fragment_shader) {
    val uMatrixLocation = GLES20.glGetUniformLocation(programId, U_MATRIX)
    val aColorLocation = GLES20.glGetAttribLocation(programId, A_COLOR)
    val aPositionLocation = GLES20.glGetAttribLocation(programId, A_POSITION)

    protected val A_TEXTURE_COORDINATES = "a_TextureCoordinates"
    var aTextureCoordinatesLocation = GLES20.glGetAttribLocation(programId, A_TEXTURE_COORDINATES)

    protected val U_TEXTURE_UNIT = "u_TextureUnit"
    var uTextureUnitLocation = GLES20.glGetUniformLocation(programId, U_TEXTURE_UNIT)

    open fun setUniforms(matrix: FloatArray?, textureId: Int) {
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
        // 激活纹理单元0
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        // 绑定纹理对象ID
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        // 告诉shaderProgram sampler2D纹理采集器 使用纹理单元0的纹理对象。
        GLES20.glUniform1i(uTextureUnitLocation, 0)
    }

    companion object {
        protected const val U_MATRIX = "u_Matrix"
        protected const val A_POSITION = "a_Position"
        protected const val A_COLOR = "a_Color"
    }
}