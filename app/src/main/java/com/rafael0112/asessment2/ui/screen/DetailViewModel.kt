package com.rafael0112.asessment2.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafael0112.asessment2.R
import com.rafael0112.asessment2.database.MyDiaryDao
import com.rafael0112.asessment2.model.MyDiary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailViewModel(private val dao: MyDiaryDao): ViewModel() {

    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val dayFormatter = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("in", R.string.bahasa.toString()))

    fun insert(isi: String, mood: String) {
        val catatan = MyDiary(
            tanggal = formatter.format(Date()),
            hari = dayFormatter.format(Date()),
            catatan = isi,
            mood = mood
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(catatan)
        }
    }

    suspend fun getDiary(id: Long): MyDiary? {
        return dao.getDiaryById(id)
    }

    fun update(id: Long, isi: String, mood: String) {
        val catatan = MyDiary(
            id = id,
            tanggal = formatter.format(Date()),
            hari = dayFormatter.format(Date()),
            mood = mood,
            catatan = isi
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(catatan)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }
}