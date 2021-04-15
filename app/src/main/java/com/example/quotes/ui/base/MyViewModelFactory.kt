package com.example.quotes.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quotes.data.repos.AuthRepo
import com.example.quotes.data.repos.BaseRepo
import com.example.quotes.data.repos.UserRepo
import com.example.quotes.ui.auth.AuthViewModel
import com.example.quotes.ui.home.HomeViewModel

class MyViewModelFactory(
    private val repo: BaseRepo
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return super.create(modelClass)
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repo as AuthRepo) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repo as UserRepo) as T
            else -> throw IllegalArgumentException("Your ViewModel isn't found")
        }
    }
}