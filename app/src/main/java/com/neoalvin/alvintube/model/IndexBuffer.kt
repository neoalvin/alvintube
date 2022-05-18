package com.neoalvin.alvintube.model

import android.opengl.GLES20
import com.neoalvin.alvintube.constants.Constants
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.IntBuffer
import java.nio.ShortBuffer


class IndexBuffer {
    val indexBufferId: Int

    constructor(indexData: ShortArray) {
        //allocate a buffer
        val buffers = IntArray(1)
        GLES20.glGenBuffers(buffers.size, buffers, 0)
        if (buffers[0] == 0) {
            throw RuntimeException("Could not create a new vertex buffer object")
        }
        indexBufferId = buffers[0]
        //bind to the buffer
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, buffers[0])
        //Transfer data to native memory.
        val indexArry: ShortBuffer = ByteBuffer.allocateDirect(indexData.size * Constants.BYTES_PER_SHORT)
            .order(ByteOrder.nativeOrder())
            .asShortBuffer()
            .put(indexData)
        indexArry.position(0)
        GLES20.glBufferData(
            GLES20.GL_ELEMENT_ARRAY_BUFFER, indexArry.capacity() * Constants.BYTES_PER_SHORT,
            indexArry, GLES20.GL_STATIC_DRAW
        )
        //IMPORTANT! unbind the buffer when done with it
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0)
    }

    constructor(indexData: IntArray) {
        val buffers = IntArray(1)
        GLES20.glGenBuffers(buffers.size, buffers, 0)
        if (buffers[0] == 0) {
            throw RuntimeException("Could not create a new vertex buffer object")
        }
        indexBufferId = buffers[0]
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, buffers[0])
        val indexArry: IntBuffer = ByteBuffer.allocateDirect(indexData.size * Constants.BYTES_PER_INT)
            .order(ByteOrder.nativeOrder())
            .asIntBuffer()
            .put(indexData)
        indexArry.position(0)
        GLES20.glBufferData(
            GLES20.GL_ELEMENT_ARRAY_BUFFER, indexArry.capacity() * Constants.BYTES_PER_INT,
            indexArry, GLES20.GL_STATIC_DRAW
        )
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0)
    }
}