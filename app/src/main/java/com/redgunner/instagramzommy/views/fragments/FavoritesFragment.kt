package com.redgunner.instagramzommy.views.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.redgunner.instagramzommy.R
import com.redgunner.instagramzommy.adapter.SearchInstagramUserListAdapter
import com.redgunner.instagramzommy.models.search.User
import com.redgunner.instagramzommy.viewmodels.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.favorites_fragment.*

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.favorites_fragment) {

  private  val viewModel:FavoritesViewModel by viewModels()

    private val accountListAdapter= SearchInstagramUserListAdapter { account ->

      findNavController().navigate(FavoriteFragmentDirections.actionGlobalProfileFragment(account.username))

    }


  override fun onStart() {
    super.onStart()
    setUpRecyclerView()

    viewModel.favoriteAccountList.observe(viewLifecycleOwner,{favoriteList ->

      val userList= mutableListOf<User>()
      favoriteList.forEach {
        userList.add(User(it))
      }

      accountListAdapter.submitList(userList.reversed())

    })


  }


  private fun setUpRecyclerView(){
    favoritesAccountsList.apply {
      this.adapter=accountListAdapter
    }
  }



}