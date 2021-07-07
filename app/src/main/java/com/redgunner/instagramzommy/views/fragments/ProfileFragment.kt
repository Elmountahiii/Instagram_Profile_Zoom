package com.redgunner.instagramzommy.views.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.redgunner.instagramzommy.R
import com.redgunner.instagramzommy.models.profile.AccountResponse
import com.redgunner.instagramzommy.utils.showPermissionRequestDialog
import com.redgunner.instagramzommy.viewmodels.SharedViewModel
import com.stfalcon.imageviewer.StfalconImageViewer
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.android.synthetic.main.profile_fragment.adView
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.profile_fragment) {

    private val viewModel: SharedViewModel by activityViewModels()
    private val saveArgs: ProfileFragmentArgs by navArgs()
    private val profileImageList = mutableListOf<String>()

    private lateinit var mInterstitialAd: com.google.android.gms.ads.InterstitialAd


    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onStart() {
        super.onStart()
        if (viewModel.hasInternetConnection.value == true) {

            viewModel.getAccount(saveArgs.userName)


        } else {
            Toast.makeText(this.context, "No internet connection", Toast.LENGTH_LONG).show()

        }

        setUpInterstitialAd()

        setUpMobileAds()


    }

    private fun setUpMobileAds() {
        MobileAds.initialize(requireActivity())
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setPermissionCallback()

    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launchWhenStarted {

            viewModel.instagramAccountFlow.collect { response ->

                if (response.isSuccessful) {
                    displayItems()
                    response.body()?.let { displayAccount(it) }

                }

            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.shareItNotify.collect { imagePath ->

                openChooser(imagePath)


            }
        }


        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.favorite -> {
                    viewModel.addAccountToFavorite()
                    true
                }
                R.id.save -> {
                    checkPermissionAndDownloadBitmap()

                    true
                }
                R.id.share -> {

                    viewModel.downloadAndShareIt()

                    true
                }
                else -> false
            }
        }

        topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        profile_image.setOnClickListener {

            StfalconImageViewer.Builder<String>(context,profileImageList){ imageView: ImageView, url: String ->
                Glide.with(this).load(url).into(imageView)

            }.show()
        }

    }

    private fun setUpInterstitialAd() {
        mInterstitialAd = com.google.android.gms.ads.InterstitialAd(this.context)

        mInterstitialAd.adUnitId = getString(R.string.InterstitialAdID)
        mInterstitialAd.loadAd(AdRequest.Builder().build())

    }



    private fun openChooser(imagePath: String?) {
        if (imagePath != null) {
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, Uri.parse(imagePath))
                type = "image/jpeg"
            }
            startActivity(Intent.createChooser(shareIntent, "Send To"))
        } else {
            Toast.makeText(this.context, "Error", Toast.LENGTH_LONG).show()
        }


    }

    private fun displayAccount(account: AccountResponse) {

        profileImageList.add(account.profile_pic_url)

        followersCount.text = account.edge_followed_by.count.toString()
        followingCount.text = account.edge_follow.count.toString()
        profileUserName.text = account.full_name
        if (account.is_verified) {
            profileIsCheck.visibility = View.VISIBLE
        }

        Glide.with(this).load(account.profile_pic_url).into(profile_image)

        Glide.with(this).load(account.profile_pic_url).into(profile_image_320)

        Glide.with(this).load(account.profile_pic_url_hd)
            .apply(bitmapTransform(BlurTransformation(25, 3))).into(profile_image_1080)

        profile_image_1080.setOnClickListener {

            if(mInterstitialAd.isLoaded){
                mInterstitialAd.show()
                Glide.with(this).load(account.profile_pic_url_hd).into(profile_image)
                Glide.with(this).load(account.profile_pic_url_hd).into(profile_image_1080)
                profileImageList.removeAt(0)
                profileImageList.add(account.profile_pic_url_hd)
            }else{
                Glide.with(this).load(account.profile_pic_url_hd).into(profile_image)
                Glide.with(this).load(account.profile_pic_url_hd).into(profile_image_1080)
                profileImageList.removeAt(0)
                profileImageList.add(account.profile_pic_url_hd)
            }



        }

        profile_image_320.setOnClickListener {
            Glide.with(this).load(account.profile_pic_url).into(profile_image)

        }


    }

    private fun checkPermissionAndDownloadBitmap() {
        when {
            ContextCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.saveImage()

            }

            shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {


                context?.showPermissionRequestDialog(
                    "Permission required",
                    "the application need permission to save image "
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    private fun setPermissionCallback() {
        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    viewModel.saveImage()

                }
            }
    }

    private fun displayItems() {
        textCounter1.visibility = View.VISIBLE
        textCounter2.visibility = View.VISIBLE
        textCounter3.visibility = View.VISIBLE
        textCounter4.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE

    }

}