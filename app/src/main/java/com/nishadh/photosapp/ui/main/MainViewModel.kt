package com.nishadh.photosapp.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val photos = MutableLiveData<Array<PhotoUio>>()
    var selectedPhoto = MutableLiveData<PhotoUio>()

}