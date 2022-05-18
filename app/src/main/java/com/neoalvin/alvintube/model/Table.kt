package com.neoalvin.alvintube.model

import android.opengl.GLES20
import com.neoalvin.alvintube.constants.Constants
import com.neoalvin.alvintube.program.TextureShaderProgram


class Table {
    private val vertexArray = VertexArray(VERTEX_DATA)

    fun bindData(shaderProgram: TextureShaderProgram) {
        vertexArray.setVertexAttributePointer(
            shaderProgram.aPositionLocation,
            POSITION_COMPONENT_COUNT,
            STRIDE,
            0
        )
        vertexArray.setVertexAttributePointer(
            shaderProgram.aTextureCoordinatesLocation,
            TEXTURE_COORDINATES_COMPONENT_COUNT,
            STRIDE,
            POSITION_COMPONENT_COUNT
        )
    }

    fun draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6)
    }

    companion object {
        private const val POSITION_COMPONENT_COUNT = 2
        private const val TEXTURE_COORDINATES_COMPONENT_COUNT = 2
        private val STRIDE: Int = ((POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT)
                * Constants.BYTES_PER_FLOAT)
        private val VERTEX_DATA = floatArrayOf(
            //x,    y,      s,      t
            0f, 0f, 0.5f, 0.5f,
            -0.5f, -0.8f, 0f, 0.9f,
            0.5f, -0.8f, 1f, 0.9f,
            0.5f, 0.8f, 1f, 0.1f,
            -0.5f, 0.8f, 0f, 0.1f,
            -0.5f, -0.8f, 0f, 0.9f
        )
    }

}