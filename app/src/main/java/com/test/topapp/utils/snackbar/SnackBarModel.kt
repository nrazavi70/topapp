package com.test.topapp.utils.snackbar

data class SnackBarModel(
    val message: String,
    val btnTitle: String? = null,
    val action: (() -> Unit)? = null
)