package com.example.quotes.utils

import android.view.View
import androidx.fragment.app.Fragment
import com.example.api.network.Resource
import com.example.quotes.ui.auth.SignInFragment
import com.google.android.material.snackbar.Snackbar

private fun View.showSnackBar(
    message: String,
    action: (() -> Unit)? = null
) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let {
        snackbar.setAction("Retry") {
            it()
        }
    }
    snackbar.show()
}

fun Fragment.handleApiError(
    failure: Resource.Failure,
    retryFunction: (() -> Unit)? = null
) {
    when {
        failure.isNetworkError -> {
            requireView().showSnackBar("Please check your network connection", retryFunction)
        }
        failure.errorCode == 401 -> {
            // Error code 401 means UnAuthorized request
            if (this is SignInFragment) {
                // If it comes from signInFragment -> Means user has entered wrong email or password
                requireView().showSnackBar("You have entered wrong email or password")
            } else {
                // If it comes from other than SignInFragment then access/auth token has been
                // expired so we need to logout the user & user will have to sigIn again
                // TODO Logout the user
            }
        }
        else -> {
            // For any other error we can simply display error body
            val error: String = failure.errorBody?.string()
                .toString()  // toString() lgane se null string nhi aaegi
            requireView().showSnackBar(error)
        }
    }
}