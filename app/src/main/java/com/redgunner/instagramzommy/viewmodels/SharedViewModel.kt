package com.redgunner.instagramzommy.viewmodels

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.redgunner.instagramzommy.R
import com.redgunner.instagramzommy.models.profile.AccountResponse
import com.redgunner.instagramzommy.models.search.SearchResponse
import com.redgunner.instagramzommy.models.search.UserX
import com.redgunner.instagramzommy.repository.InstagramRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class SharedViewModel@ViewModelInject constructor(private val instagramRepository: InstagramRepository,
                                                  @ApplicationContext application: Context
) :ViewModel() {


    val isServerOn= MutableLiveData<Boolean>()
    val hasInternetConnection=MutableLiveData<Boolean>()

    val accountsList=MutableLiveData<Response<SearchResponse>>()

    val instagramAccount = MutableLiveData<Response<AccountResponse>>()

    val shareItNotify=MutableLiveData<String>()

    private val context = application.applicationContext




    fun loginWithTheServer(){
        viewModelScope.launch {
            isServerOn.value = instagramRepository.startServer().isSuccessful

        }
    }


    fun getAccount(userName: String) {
        viewModelScope.launch {
            instagramAccount.value = instagramRepository.getInstagramAccount(userName)
        }
    }

    fun addAccountToFavorite(account: UserX) {


        viewModelScope.launch {
            instagramRepository.addAccount(account)
        }

    }


    fun saveImage() {

        CoroutineScope(Dispatchers.IO).launch {
            if ( instagramAccount.value?.isSuccessful == true){
                saveImage( Glide
                    .with(context)
                    .asBitmap()
                    .load(instagramAccount.value?.body()?.profile_pic_url_hd).submit().get())
            }

        }

    }

    private  fun saveImage(image: Bitmap){
        var savedImagePath: String? = null
        val imageFileName = "JPEG_" + "${System.currentTimeMillis()}" + ".jpg"
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/${R.string.app_name}"
        )
        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }
        if (success) {
            val imageFile = File(storageDir, imageFileName)
            savedImagePath = imageFile.getAbsolutePath()

            try {
                val fOut: OutputStream = FileOutputStream(imageFile)
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath)
            //Toast.makeText(this, "IMAGE SAVED", Toast.LENGTH_LONG).show() // to make this working, need to manage coroutine, as this execution is something off the main thread
        }

    }

    private suspend fun saveImageAndShareIt(image: Bitmap){
        var savedImagePath: String? = null
        val imageFileName = "JPEG_" + "${System.currentTimeMillis()}" + ".jpg"
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/Insta Profile Zoom"
        )
        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }
        if (success) {
            val imageFile = File(storageDir, imageFileName)
            savedImagePath = imageFile.getAbsolutePath()

            try {
                val fOut: OutputStream = FileOutputStream(imageFile)
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath)
            //Toast.makeText(this, "IMAGE SAVED", Toast.LENGTH_LONG).show() // to make this working, need to manage coroutine, as this execution is something off the main thread
        }
        if (savedImagePath != null) {
            withContext(Dispatchers.Main){
                shareItNotify.value= savedImagePath!!

            }
        }

    }


    private  fun galleryAddPic(imagePath: String?) {
        imagePath?.let { path ->
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val f = File(path)
            val contentUri: Uri = Uri.fromFile(f)
            mediaScanIntent.data = contentUri


        }
    }

    fun downloadAndShareIt(){

        CoroutineScope(Dispatchers.IO).launch {
            if (instagramAccount.value?.isSuccessful == true){
                saveImageAndShareIt(
                    Glide
                        .with(context)
                        .asBitmap()
                        .load(instagramAccount.value?.body()?.profile_pic_url_hd).submit().get())
            }


        }

    }

    fun search(userName:String){

        viewModelScope.launch {
            accountsList.value=instagramRepository.search(userName)
        }
    }


    fun addAccount(account: UserX) {

        viewModelScope.launch {
            instagramRepository.addAccount(account)
        }

    }





}