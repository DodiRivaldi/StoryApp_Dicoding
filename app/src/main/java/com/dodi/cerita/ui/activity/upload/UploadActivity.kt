package com.dodi.cerita.ui.activity.upload

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewModelScope
import com.dodi.cerita.BuildConfig
import com.dodi.cerita.R
import com.dodi.cerita.abstraction.*
import com.dodi.cerita.databinding.ActivityUploadBinding
import com.dodi.cerita.ui.activity.home.HomeActivity
import com.dodi.cerita.ui.activity.home.HomeActivity.Companion.TOKEN
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class UploadActivity : AppCompatActivity() {
    private val viewModel : UploadViewModel by viewModels()
    private lateinit var photoPath : String
    private lateinit var fusedLocationc : FusedLocationProviderClient
    private var file : File? = null
    private var loc : Location? = null
    private var lat : RequestBody? = null
    private var long : RequestBody?= null
    private lateinit var binding: ActivityUploadBinding
    private lateinit var token : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarUpload)

        fusedLocationc = LocationServices.getFusedLocationProviderClient(this)

        getLatLong()
        //get token
        viewModel.viewModelScope.launch {
            viewModel.getToken().collect{
                token = it!!
            }
        }

        binding.apply {
            btnUpload.setOnClickListener {
                if (edtDesc.text.toString().isNullOrEmpty()){
                    showToast(this@UploadActivity,getString(R.string.desc_empty))
                }else{
                    upload(edtDesc.text.toString())
                }
            }

            tvSelectImage.setOnClickListener {
                val dialogBuilder = AlertDialog.Builder(this@UploadActivity)
                dialogBuilder.setMessage(getString(R.string.selectimagesource))
                    .setCancelable(true)
                    .setPositiveButton(getString(R.string.camera)) { dialog, id ->
                        dialog.cancel()
                        openCamera()
                    }
                    .setNegativeButton(getString(R.string.gallery)) { dialog, id ->
                        dialog.cancel()
                        openGallery()
                    }

                val alert = dialogBuilder.create()
                alert.setTitle(getString(R.string.info))
                alert.show()
            }
        }

    }

    private fun upload(desc : String){
        if (file != null){
            showProgressBar(true,binding.pbUpload)
            val fileReduce = reduceImageSize(file as File)
            val descBody = desc.toRequestBody("text/plain".toMediaType())
            val imageFile = fileReduce.asRequestBody("image/png".toMediaType())
            val imgMultiPart : MultipartBody.Part = MultipartBody.Part.createFormData("photo",fileReduce.name,imageFile)

            if (loc != null){
                lat = loc?.latitude.toString().toRequestBody("text/plain".toMediaType())
                long = loc?.longitude.toString().toRequestBody("text/plain".toMediaType())
            }

            viewModel.viewModelScope.launch {
                viewModel.upload(token,descBody,lat,long,imgMultiPart).collect{
                    it.onSuccess {
                        Intent(this@UploadActivity,HomeActivity::class.java).also {
                            it.apply {
                                putExtra(TOKEN,token)
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            }
                            startActivity(it)
                            finish()
                        }
                    }
                    it.onFailure {
                        showToast(this@UploadActivity,getString(R.string.error))
                        showProgressBar(false,binding.pbUpload)
                    }
                }
            }
        }else{
            showProgressBar(false,binding.pbUpload)
            showToast(this,getString(R.string.selectimagefirst))

        }
    }

    private fun getLatLong(){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationc.lastLocation.addOnSuccessListener {
                if (it != null){
                    this.loc = it
                }else{
                    showToast(this,getString(R.string.enablepermission))
                }
            }
        }else{
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()){isAllow : Boolean->
        if (isAllow){

        }else{
            showToast(this,getString(R.string.enablepermission))
        }
    }

    private val intentCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK){
            val newFile = File(photoPath)
            val result: Bitmap = rotateImage(BitmapFactory.decodeFile(newFile.path),true)
            val outputStream = FileOutputStream(newFile)
            result.compress(Bitmap.CompressFormat.PNG,100,outputStream)
            outputStream.flush()
            outputStream.close()
            file = newFile
            binding.imgUpload.setImageBitmap(result)
        }
    }

    private val intentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK){
            val item : Uri = it.data?.data as Uri
            val itemFile = convertUriFile(item, this@UploadActivity)
            file = itemFile
            binding.imgUpload.setImageURI(item)
        }
    }

    private fun openGallery(){
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val selected = Intent.createChooser(intent,getString(R.string.select_image))
        intentGallery.launch(selected)
    }

    private fun openCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        tempFile(application).also {
            val fileUri : Uri = FileProvider.getUriForFile(this,BuildConfig.APPLICATION_ID,it)
            photoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri)
            intentCamera.launch(intent)
        }
    }
}