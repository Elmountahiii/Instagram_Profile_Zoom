package com.redgunner.instagramzommy.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.redgunner.instagramzommy.R
import com.redgunner.instagramzommy.adapter.SavedStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.favorite_fragment.*

@AndroidEntryPoint

class FavoriteFragment : Fragment(R.layout.favorite_fragment) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpTabLayout()

    }

    private fun setUpTabLayout() {
        FavoriteViewPager.adapter = SavedStateAdapter(this)
        TabLayoutMediator(FavoriteTabLayout, FavoriteViewPager) { tab, position ->

            when (position) {
                0 -> {
                    tab.text = "Favorite"
                }
                1 -> {
                    tab.text = "History"
                }
            }

        }.attach()

    }


}