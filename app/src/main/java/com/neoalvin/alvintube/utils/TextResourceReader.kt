package com.neoalvin.alvintube.utils

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

class TextResourceReader {
    companion object {
        /**
         * 从原生文件读入glsl
         * @param context
         * @param resourceId
         * @return
         */
        fun readTextFileFromResource(context: Context, resourceId: Int): String {
            var body = StringBuilder()

            try {
                var inputStream = context.resources.openRawResource(resourceId);
                var inputStreamReader = InputStreamReader(inputStream)
                var bufferedReader = BufferedReader(inputStreamReader)

                while (true) {
                    var nextLine: String? = bufferedReader.readLine() ?: break

                    body.append(nextLine)
                    body.append('\n')
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            return body.toString()
        }
    }
}