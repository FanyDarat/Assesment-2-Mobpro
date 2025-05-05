package com.rafael0112.asessment2.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafael0112.asessment2.database.MyDiaryDao
import com.rafael0112.asessment2.model.MyDiary
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainViewModel(dao: MyDiaryDao) : ViewModel() {

    val data: StateFlow<List<MyDiary>> = dao.getDiary().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    val dataRecycle: StateFlow<List<MyDiary>> = dao.getRecycleBin().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )
}