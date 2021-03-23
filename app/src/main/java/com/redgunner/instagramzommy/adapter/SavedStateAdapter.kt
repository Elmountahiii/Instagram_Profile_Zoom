package com.redgunner.instagramzommy.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.redgunner.instagramzommy.views.fragments.FavoritesFragment
import com.redgunner.instagramzommy.views.fragments.FavoriteFragment
import com.redgunner.instagramzommy.views.fragments.HistoryFragment

class SavedStateAdapter (fragmentActivity: FavoriteFragment) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> FavoritesFragment()
            else -> HistoryFragment()
        }


    }


}