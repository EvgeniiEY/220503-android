package ru.netology.nmedia.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Response
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import java.io.IOException
import java.lang.Exception

object AndroidUtils {
    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun onResponseFun(callback: PostRepository.PostCallback<Post>, call: Call, response: Response) {
        val gson = Gson()
        val typeToken = object : TypeToken<List<Post>>() {}
        val body =
            response.body?.string() ?: throw java.lang.RuntimeException("body is null")
        try {
            callback.onSuccess(gson.fromJson(body, typeToken.type))
        } catch (e: Exception) {
            callback.onError(e)
        }
    }

    fun onFailureFun(callback: PostRepository.PostCallback<Post>, call: Call, e: IOException) {
        callback.onError(e)
    }
}