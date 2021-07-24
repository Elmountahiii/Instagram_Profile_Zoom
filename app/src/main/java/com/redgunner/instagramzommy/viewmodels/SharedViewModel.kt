package com.redgunner.instagramzommy.viewmodels

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
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
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class SharedViewModel @ViewModelInject constructor(
    private val instagramRepository: InstagramRepository,
    @ApplicationContext application: Context
) : ViewModel() {


    var visibility = false

    val accountsList = MutableLiveData<Response<SearchResponse>>()

    private val accountResponse = MutableLiveData<Response<AccountResponse>>()

    private val accountEventChannel = Channel<Response<AccountResponse>>()
    val instagramAccountFlow = accountEventChannel.receiveAsFlow()


    private val shareImageEventChannel = Channel<String>()
    val shareItNotify = shareImageEventChannel.receiveAsFlow()

    private val context = application.applicationContext


    fun getAccount(userName: String) {
        viewModelScope.launch {


            async {

                try{
                    accountResponse.value = instagramRepository.getInstagramAccount(userName)
                    accountEventChannel.send(accountResponse.value!!)
                }catch (e:Exception){

                }

            }


        }
    }

    fun saveImage() {

        CoroutineScope(Dispatchers.IO).launch {
            if(accountResponse.value?.body()!=null){
                saveImage(
                    Glide
                        .with(context)
                        .asBitmap()
                        .load(accountResponse.value!!.body()!!.profile_pic_url_hd).submit().get()
                )
            }



        }

    }

    private fun saveImage(image: Bitmap) {
        var savedImagePath: String? = null
        val imageFileName = "JPEG_" + "${System.currentTimeMillis()}" + ".jpg"
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/Instagram profile pictures"
        )
        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }
        if (success) {
            val imageFile = File(storageDir, imageFileName)
            savedImagePath = imageFile.absolutePath

            try {
                val fOut: OutputStream = FileOutputStream(imageFile)
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            galleryAddPic(savedImagePath)

        }

    }


    private suspend fun saveImageAndShareIt(image: Bitmap) {
        var savedImagePath: String? = null
        val imageFileName = "JPEG_" + "${System.currentTimeMillis()}" + ".jpg"
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/Instagram profile pictures"
        )
        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }
        if (success) {
            val imageFile = File(storageDir, imageFileName)
            savedImagePath = imageFile.absolutePath
            shareImageEventChannel.send(savedImagePath)


            try {
                val fOut: OutputStream = FileOutputStream(imageFile)
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            galleryAddPic(savedImagePath)

        }

    }

    private fun galleryAddPic(imagePath: String?) {
        imagePath?.let { path ->
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val f = File(path)
            val contentUri: Uri = Uri.fromFile(f)
            mediaScanIntent.data = contentUri


        }
    }

    fun downloadAndShareIt() {


        CoroutineScope(Dispatchers.IO).launch {
            if(accountResponse.value?.body()!=null){

                saveImageAndShareIt(
                    Glide
                        .with(context)
                        .asBitmap()
                        .load(accountResponse.value?.body()?.profile_pic_url_hd).submit().get()
                )
            }

        }

    }

    fun search(userName: String) {

        viewModelScope.launch {

            try{
                accountsList.value = instagramRepository.search(userName)

            }catch (e :Exception){

            }
        }
    }


    fun addAccountToFavorite() {

        viewModelScope.launch {
            val account = accountResponse.value?.body()
            if (account != null) {
                instagramRepository.addAccount(
                    UserX(
                        full_name = account.full_name,
                        has_anonymous_profile_picture = false,
                        is_private = account.is_private,
                        is_verified = account.is_verified,
                        profile_pic_url = account.profile_pic_url,
                        username = account.username,
                        is_favorite = true


                    )
                )

            }
        }

    }


    fun addAccountToHistory(account: UserX) {

        viewModelScope.launch {
            instagramRepository.addAccount(account)
        }

    }


}