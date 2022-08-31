package com.dodi.cerita.abstraction

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import com.dodi.cerita.abstraction.Constant.DATE_FORMAT
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

fun convertUriFile(uri :Uri,context: Context):File{
    val contentResolver :ContentResolver = context.contentResolver
    val file = tempFile(context)
    val inputStream = contentResolver.openInputStream(uri) as InputStream
    val outputStream : OutputStream = FileOutputStream(file)
    val buf = ByteArray(1024)
    var len :Int
    while (inputStream.read(buf).also { len=it }>0)
        outputStream.write(buf,0,len)
    outputStream.close()
    inputStream.close()

    return file
}

fun rotateImage(bitmap :Bitmap,isBack :Boolean=false):Bitmap{
    val matrix = Matrix();
    return if (isBack){
        matrix.postRotate(90f)
        Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height,matrix,true)
    }else{
        matrix.postRotate(-90f)
        matrix.postScale(-1f,1f,bitmap.width/2f,bitmap.height/2f)
        Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height,matrix,true)
    }
}

fun tempFile(context: Context):File{
    val dir :File?=context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(currentTime,".jpg",dir)
}

val currentTime : String = SimpleDateFormat(DATE_FORMAT, Locale.US).format(System.currentTimeMillis() )