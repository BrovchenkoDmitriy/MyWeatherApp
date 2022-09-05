package com.example.testretrofitapp.di

import androidx.lifecycle.ViewModel
import com.example.testretrofitapp.presentation.OverViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(OverViewModel::class)
    fun bindOverViewModel(viewModel: OverViewModel):ViewModel
}