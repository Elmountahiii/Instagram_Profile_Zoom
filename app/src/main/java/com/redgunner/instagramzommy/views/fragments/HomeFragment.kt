package com.redgunner.instagramzommy.views.fragments

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.redgunner.instagramzommy.R
import com.redgunner.instagramzommy.adapter.SearchInstagramUserListAdapter

import com.redgunner.instagramzommy.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.home_fragment.*

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home_fragment) {


    private val viewModel: SharedViewModel by activityViewModels()

    private val accountListAdapter = SearchInstagramUserListAdapter { account ->
        viewModel.addAccount(account)

        findNavController().navigate(HomeFragmentDirections.actionGlobalProfileFragment(account.username))

    }



    override fun onStart() {
        super.onStart()

        setUpRecyclerView()
        setUpObservers()



    }


    override fun onResume() {
        super.onResume()


        Input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {


                if (viewModel.hasInternetConnection.value==true){
                    if (s.toString().isEmpty()) {
                        MainImage.visibility = View.VISIBLE
                        MainText.visibility = View.VISIBLE
                        HomeAccountsList.visibility = View.INVISIBLE

                    } else {
                        MainImage.visibility = View.INVISIBLE
                        MainText.visibility = View.INVISIBLE
                        HomeAccountsList.visibility = View.VISIBLE
                        viewModel.search(s.toString())

                    }
                }else{
                    Toast.makeText(this@HomeFragment.context,"No internet connection",Toast.LENGTH_LONG).show()
                    MainImage.visibility = View.VISIBLE
                    MainText.visibility = View.VISIBLE
                    HomeAccountsList.visibility = View.INVISIBLE

                }





            }
        })


    }


    private fun setUpRecyclerView() {
        HomeAccountsList.apply {
            this.adapter = accountListAdapter
        }
    }

    private fun setUpObservers(){


        viewModel.accountsList.observe(viewLifecycleOwner, { response ->

            if (response.isSuccessful) {

                val instagramResult = response.body()
                accountListAdapter.submitList(instagramResult!!.users)


            } else {
                Toast.makeText(this.requireContext(), response.code().toString(), Toast.LENGTH_LONG)
                    .show()

            }

        })

    }



}