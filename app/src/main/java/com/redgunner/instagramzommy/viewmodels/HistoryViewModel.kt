package com.redgunner.instagramzommy.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.redgunner.instagramzommy.repository.InstagramRepository


class HistoryViewModel @ViewModelInject constructor(private val instagramRepository: InstagramRepository): ViewModel() {


    val historySearchList=instagramRepository.historySearchList.asLiveData()


}