package com.redgunner.instagramzommy.views.fragments

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.redgunner.instagramzommy.R
import com.redgunner.instagramzommy.adapter.SearchInstagramUserListAdapter
import com.redgunner.instagramzommy.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.home_fragment.*

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home_fragment) {


    private val viewModel: SharedViewModel by activityViewModels()
    private lateinit var mInterstitialAd: com.google.android.gms.ads.InterstitialAd


    private val accountListAdapter = SearchInstagramUserListAdapter { account ->
        viewModel.addAccountToHistory(account)

        findNavController().navigate(HomeFragmentDirections.actionGlobalProfileFragment(account.username))

    }


    override fun onStart() {
        super.onStart()
        setUpAds()
        setUpInterstitialAd()
        setUpRecyclerView()
        setUpObservers()


    }

    override fun onResume() {
        super.onResume()

        mInterstitialAd.adListener= object : AdListener() {

            override fun onAdClosed() {
                super.onAdClosed()
                requireActivity().finish()
            }
        }


        Input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


            }

            override fun afterTextChanged(s: Editable?) {


                if (s.toString().isEmpty()) {
                    MainImage.visibility = View.VISIBLE
                    MainText.visibility = View.VISIBLE
                    HomeAccountsList.visibility = View.INVISIBLE
                    shimmer_view_container.visibility = View.INVISIBLE
                    viewModel.visibility = false


                } else {
                    MainImage.visibility = View.INVISIBLE
                    MainText.visibility = View.INVISIBLE
                    shimmer_view_container.visibility = View.VISIBLE
                    HomeAccountsList.visibility = View.INVISIBLE
                    shimmer_view_container.startShimmer()
                    viewModel.search(s.toString())
                    viewModel.visibility = true


                }


            }
        })


        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {



            if (mInterstitialAd.isLoaded){
                mInterstitialAd.show()
            }else{
                requireActivity().finish()
            }

        }


    }

    override fun onStop() {
        super.onStop()
        viewModel.visibility = false

    }

    private fun setUpRecyclerView() {
        HomeAccountsList.apply {
            this.adapter = accountListAdapter
        }
    }

    private fun setUpObservers() {

        viewModel.accountsList.observe(viewLifecycleOwner, { response ->


            if (viewModel.visibility) {
                shimmer_view_container.stopShimmer()
                shimmer_view_container.visibility = View.INVISIBLE
                HomeAccountsList.visibility = View.VISIBLE

            } else {
                HomeAccountsList.visibility = View.INVISIBLE

            }


            if (response.isSuccessful) {
                val instagramResult = response.body()

                if (instagramResult!!.users.isNotEmpty()) {

                    accountListAdapter.submitList(instagramResult.users)

                }

            } else {
                Toast.makeText(
                    this.requireContext(),
                    "Server Error ${response.code().toString()}",
                    Toast.LENGTH_LONG
                )
                    .show()

            }

        })

    }


    private fun setUpAds() {
        MobileAds.initialize(requireActivity())
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    private fun setUpInterstitialAd() {
        mInterstitialAd = com.google.android.gms.ads.InterstitialAd(this.context)

        mInterstitialAd.adUnitId = getString(R.string.exitInterstitialAdID)
        mInterstitialAd.loadAd(AdRequest.Builder().build())

    }





}