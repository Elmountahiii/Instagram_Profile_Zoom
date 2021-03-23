package com.redgunner.instagramzommy.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.redgunner.instagramzommy.repository.InstagramRepository


class FavoritesViewModel @ViewModelInject constructor(private val instagramRepository: InstagramRepository) :
    ViewModel() {


        val favoriteAccountList=instagramRepository.favoriteAccountList.asLiveData()

}