package com.sample.playground.di

import com.sample.playground.ui.main.MainViewModel
import com.sample.playground.utils.rx.AppSchedulerProvider
import com.sample.playground.utils.rx.SchedulerProvider
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { MainViewModel(get(), get()) }
    factory { AppSchedulerProvider() as SchedulerProvider }
}
