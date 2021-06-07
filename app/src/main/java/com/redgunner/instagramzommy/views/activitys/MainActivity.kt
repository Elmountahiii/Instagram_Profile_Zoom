package com.redgunner.instagramzommy.views.activitys

import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.redgunner.instagramzommy.R
import com.redgunner.instagramzommy.utils.ConnectionLiveData
import com.redgunner.instagramzommy.utils.UserPreferences
import com.redgunner.instagramzommy.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var userPreferences: UserPreferences

    private val viewModel: SharedViewModel by viewModels()
    lateinit var connectionLiveData: ConnectionLiveData


    override fun onStart() {
        super.onStart()

        connectionLiveData = ConnectionLiveData(this)

        connectionLiveData.observe(this, { isNetworkAvailable ->

            if (!isNetworkAvailable) {

                Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_LONG)
                    .show()
                viewModel.hasInternetConnection.value = false

            } else {
                viewModel.hasInternetConnection.value = true


            }


        })


        userPreferences.themePreferences.asLiveData().observe(this, { isDarak ->
            if (isDarak == true) {

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            } else {

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            }

        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navigationController = Navigation.findNavController(
            this,
            R.id.NevHostFragment
        )
        setupBottomNavigationMenu(navigationController)
    }


    private fun setupBottomNavigationMenu(navController: NavController) {


        bottom_navigation?.let {
            NavigationUI.setupWithNavController(it, navController)
        }

    }


}