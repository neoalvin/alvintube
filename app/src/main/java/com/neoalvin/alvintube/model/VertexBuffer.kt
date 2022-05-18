package com.neoalvin.alvintube.model

import android.opengl.GLES20
import com.neoalvin.alvintube.constants.Constants
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


class VertexBuffer(vertexData: FloatArray) {
    private val vertexBufferID: Int

    fun getVertexBufferID(): Int {
        return vertexBufferID
    }

    init {
        // 第一步，我们向OpenGL服务端申请创建缓冲区
        val buffers = IntArray(1)
        GLES20.glGenBuffers(buffers.size, buffers, 0)
        if (buffers[0] == 0) {
            val i = GLES20.glGetError()
            throw RuntimeException("Could not create a new vertex buffer object, glGetError : $i")
        }
        // 保存申请返回的缓冲区标示
        vertexBufferID = buffers[0]
        // 绑定缓冲区 为 数组缓存
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0])
        // 把java数据放转至到native
        val vertexArr: FloatBuffer = ByteBuffer.allocateDirect(vertexData.size * Constants.BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertexData)
        vertexArr.position(0)
        // 把native的数据绑定保存到缓存区，注意长度为字节单位。用途是为GL_STATIC_DRAW
        GLES20.glBufferData(
            GLES20.GL_ARRAY_BUFFER, vertexArr.capacity() * Constants.BYTES_PER_FLOAT,
            vertexArr, GLES20.GL_STATIC_DRAW
        )
        // 告诉OpenGL 解绑缓冲区的操作。
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
    }

    fun setVertexAttributePointer(
        attributeLocation: Int,
        componentCount: Int,
        stride: Int,
        dataOffset: Int
    ) {
        // 先绑定标示缓冲区，通知OpenGL要使用指定的缓冲区了。
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferID)
        // 调用接口，设置着色器程序顶点属性指针
        GLES20.glVertexAttribPointer(
            attributeLocation, componentCount, GLES20.GL_FLOAT,
            false, stride, dataOffset
        )
        // 使能着色器的属性
        GLES20.glEnableVertexAttribArray(attributeLocation)
        // 告诉OpenGL 解绑缓冲区的操作。
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
    }
}