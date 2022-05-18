package com.neoalvin.alvintube.model

import android.opengl.GLES20
import com.neoalvin.alvintube.constants.Constants
import com.neoalvin.alvintube.program.ColorShaderProgram


class Mallet {
    private val vertexArray = VertexArray(VERTEX_DATA)

    fun bindData(shaderProgram: ColorShaderProgram) {
        vertexArray.setVertexAttributePointer(
            shaderProgram.aPositionLocation,
            POSITION_COMPONENT_COUNT,
            STRIDE,
            0
        )
        vertexArray.setVertexAttributePointer(
            shaderProgram.aColorLocation,
            COLOR_COMPONENT_COUNT,
            STRIDE,
            POSITION_COMPONENT_COUNT
        )
    }

    fun draw() {
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 2)
    }

    companion object {
        private const val POSITION_COMPONENT_COUNT = 2
        private const val COLOR_COMPONENT_COUNT = 3
        private const val STRIDE: Int = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT
        private val VERTEX_DATA = floatArrayOf( // 两个木槌的质点位置
            0f, -0.4f, 0f, 0f, 0f,
            0f, 0.4f, 0f, 0f, 0f
        )
    }

}