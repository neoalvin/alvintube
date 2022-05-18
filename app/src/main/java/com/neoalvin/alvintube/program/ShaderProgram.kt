package com.neoalvin.alvintube.program

import android.content.Context
import android.opengl.GLES20
import com.neoalvin.alvintube.utils.ShaderHelper
import com.neoalvin.alvintube.utils.TextResourceReader


open class ShaderProgram {
    protected val programId: Int

    constructor(
        vertexShaderResourceStr: String,
        fragmentShaderResourceStr: String
    ) {
        programId = ShaderHelper.buildProgram(
            vertexShaderResourceStr,
            fragmentShaderResourceStr
        )
    }

    constructor(
        context: Context,
        vertexShaderResourceId: Int,
        fragmentShaderResourceId: Int
    ) {
        programId = ShaderHelper.buildProgram(
            TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId),
            TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId)
        )
    }

    fun userProgram() {
        GLES20.glUseProgram(programId)
    }

    fun deleteProgram() {
        GLES20.glDeleteProgram(programId)
    }
}