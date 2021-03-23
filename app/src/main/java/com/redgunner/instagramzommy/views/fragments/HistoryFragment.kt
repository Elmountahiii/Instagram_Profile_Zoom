package com.redgunner.instagramzommy.views.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.redgunner.instagramzommy.R
import com.redgunner.instagramzommy.adapter.SearchInstagramUserListAdapter
import com.redgunner.instagramzommy.models.search.User
import com.redgunner.instagramzommy.viewmodels.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.history_fragment.*

@AndroidEntryPoint
class HistoryFragment : Fragment(R.layout.history_fragment) {


    private val viewModel: HistoryViewModel by viewModels()


    private val accountListAdapter = SearchInstagramUserListAdapter { account ->

        findNavController().navigate(FavoriteFragmentDirections.actionGlobalProfileFragment(account.username))


    }

    override fun onStart() {
        super.onStart()
        setUpRecyclerView()

        viewModel.historySearchList.observe(viewLifecycleOwner,{historyList ->

            val userList= mutableListOf<User>()
            historyList.forEach {
                userList.add(User(it))
            }

            accountListAdapter.submitList(userList.reversed())

        })


    }


    private fun setUpRecyclerView(){
        HistoryAccountsList.apply {
            this.adapter=accountListAdapter
        }
    }


}