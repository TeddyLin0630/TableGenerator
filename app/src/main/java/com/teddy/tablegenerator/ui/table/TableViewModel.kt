package com.teddy.tablegenerator.ui.table

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class TableViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val _selectedEvent = MutableLiveData<Int>()
    val selectedEvent: LiveData<Int> = _selectedEvent

    var numberOfCells = 0
    var totalCells = Pair(0, 0)

    /*
    * Random a select position every 10 seconds
    * */
    fun startTick() {
        val disposable = Observable.interval(0, INTERVAL_OF_RANDOM_FUNCTION, TimeUnit.SECONDS)
            .map { Random.nextInt(0, numberOfCells - 1 /*remove 0 case*/) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _selectedEvent.postValue(it)
            }, {
                Log.d(TableViewModel::class.java.simpleName, "error : ${it.message}")
            })
        compositeDisposable.add(disposable)
    }

    /*
    * Stop the timer when viewModel clear or App is in background.
    * */
    fun stopTick() {
        compositeDisposable.clear()
    }

    override fun onCleared() {
        stopTick()
        super.onCleared()
    }

    companion object {
        const val INTERVAL_OF_RANDOM_FUNCTION = 10L // seconds
    }
}