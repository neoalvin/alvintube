package com.neoalvin.alvintube.model

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


class VertexArray(vertexData: FloatArray) {
    init {
        floatBuffer = ByteBuffer.allocateDirect(vertexData.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertexData)
    }

    fun setVertexAttributePointer (
        attributeLocation: Int,
        componentCount: Int,
        stride: Int,
        dataOffset: Int
    ) {
        floatBuffer.position(dataOffset)
        GLES20.glVertexAttribPointer(
            attributeLocation, componentCount, GLES20.GL_FLOAT,
            false, stride, floatBuffer
        )
        GLES20.glEnableVertexAttribArray(attributeLocation)
        floatBuffer.position(0)
    }

    companion object {
        private const val BYTES_PER_FLOAT = 4

        private lateinit var floatBuffer: FloatBuffer
    }
}