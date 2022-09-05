package com.dodi.cerita.abstraction

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.dodi.cerita.abstraction.Constant.DATE_FORMAT
import java.io.*
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
    val matrix = Matrix()
    return if (isBack){
        matrix.postRotate(90f)
        Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height,matrix,true)
    }else{
        matrix.postRotate(-90f)
        matrix.postScale(-1f,1f,bitmap.width/2f,bitmap.height/2f)
        Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height,matrix,true)
    }
}

fun showProgressBar(status : Boolean, progressBar: ProgressBar){
    progressBar.visibility = if(status) View.VISIBLE else View.GONE
}

fun showToast(context: Context,text : String){
    Toast.makeText(context,text, Toast.LENGTH_SHORT).show()
}

fun tempFile(context: Context):File{
    val dir :File?=context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(currentTime,".jpg",dir)
}

fun reduceImageSize(file : File):File{
    val bitmap = BitmapFactory.decodeFile(file.path)
    var quality = 100
    var length : Int
    do{
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG,quality,stream)
        val byteArray = stream.toByteArray()
        length = byteArray.size
        quality -=5
    }while (length> 1000000)
    bitmap.compress(Bitmap.CompressFormat.PNG,quality, FileOutputStream(file))
    return file
}

val currentTime : String = SimpleDateFormat(DATE_FORMAT, Locale.US).format(System.currentTimeMillis() )