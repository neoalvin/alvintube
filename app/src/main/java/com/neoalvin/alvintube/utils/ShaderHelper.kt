package com.neoalvin.alvintube.utils

import android.opengl.GLES20.*
import android.util.Log

class ShaderHelper {
    companion object {
        private const val TAG = "ShaderHelper"

        private fun compileVertexShader(shaderCode: String): Int {
            return compileShader(GL_VERTEX_SHADER, shaderCode)
        }

        private fun compileFragmentShader(shaderCode: String): Int {
            return compileShader(GL_FRAGMENT_SHADER, shaderCode)
        }

        private fun compileShader(type: Int, shaderCode: String): Int {
            // 创建着色器
            val shaderObjectId = glCreateShader(type)
            if (shaderObjectId == 0) {
                Log.w(TAG,"Warning! Could not create new shader, glGetError:"+glGetError());
                return 0
            }

            // 上传代码
            glShaderSource(shaderObjectId, shaderCode)

            // 编译代码
            glCompileShader(shaderObjectId)

//            // 获取编译状态
//            val compileStatus = intArrayOf()
//            glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus)
//
//            Log.i(TAG, "Result of compiling source:"+"\n"+shaderCode+"\n" + glGetShaderInfoLog(shaderObjectId))
//
//            // 编译失败，删除着色器
//            if(compileStatus[0] == 0) {
//                glDeleteShader(shaderObjectId)
//                Log.w(TAG,"Warning! Compilation of shader failed, glGetError:"+glGetError())
//                return 0
//            }

            // 编译成功，返回着色器ID
            return shaderObjectId
        }

        private fun linkProgram(vertexShaderId: Int, fragmentShaderId: Int): Int {
            // 创建GL程序
            val programObjectId = glCreateProgram()
            if (programObjectId == 0){
                Log.w(TAG," Warning! Could not create new program, glGetError:"+glGetError());
                return 0
            }

            // 附加顶点着色器
            glAttachShader(programObjectId, vertexShaderId)

            // 附加片段着色器
            glAttachShader(programObjectId, fragmentShaderId)

            // 链接着色器
            glLinkProgram(programObjectId)

//            // 获取链接状态
//            val linkStatus = intArrayOf()
//            glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0)
//
//            Log.i(TAG, "Result of linking program:" + glGetProgramInfoLog(programObjectId))
//
//            // 链接失败，清除程序
//            if(linkStatus[0] == 0){
//                glDeleteProgram(programObjectId)
//                Log.w(TAG," Warning! Linking of program failed, glGetError:"+glGetError())
//                return 0
//            }

            // 链接成功，返回程序ID
            return programObjectId
        }

        fun buildProgram(vertexShaderSource: String, fragmentShaderSource: String): Int {
            // 编译着色器
            var programObjectId: Int
            var vertexShader = compileVertexShader(vertexShaderSource)
            var fragmentShader = compileFragmentShader(fragmentShaderSource)

            // 链接着色器
            programObjectId = linkProgram(vertexShader, fragmentShader)

            // 验证程序
            glValidateProgram(programObjectId)

            return programObjectId
        }

        private fun validateProgram(programObjectId: Int): Boolean {
            glValidateProgram(programObjectId)

            val validateStatus = intArrayOf()
            glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0)

            Log.i(TAG, "Result of validating program:"+validateStatus[0]
                    +"\nLog:"+ glGetProgramInfoLog(programObjectId))

            return validateStatus[0] != GL_FALSE
        }
    }
}