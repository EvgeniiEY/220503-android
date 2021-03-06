package ru.netology.nmedia.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeUnit


class PostRepositoryImpl : PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }

//    override fun getAll(): List<Post> {
//        val request: Request = Request.Builder()
//            .url("${BASE_URL}/api/slow/posts")
//            .build()
//
//        return client.newCall(request)
//            .execute()
//            .let { it.body?.string() ?: throw RuntimeException("body is null") }
//            .let {
//                gson.fromJson(it, typeToken.type)
//            }
//    }

    override fun getAllAsync(callback: PostRepository.PostCallback<List<Post>>) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {

                override fun onResponse(call: Call, response: Response) {
                    Log.d("NETWORK REQUEST", response.code.toString())
                    if (response.isSuccessful) {
                    val body =
                        response.body?.string() ?: throw java.lang.RuntimeException("body is null")
                    try {
                        callback.onSuccess(gson.fromJson(body, typeToken.type))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                    } else {
                        callback.onError(Exception(response.message))
                    }
                }
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }

    override fun likeByIdAsync(id: Long, callback: PostRepository.PostCallback<Post>) {

        val request: Request = Request.Builder()
            .post("".toRequestBody())
            .url("$BASE_URL/api/slow/posts/$id/likes")
            .build()

        clientNewCall(id, callback, request)

    }


    override fun unlikeByIdAsync(id: Long, callback: PostRepository.PostCallback<Post>) {

        val request: Request = Request.Builder()
            .delete()
            .url("$BASE_URL/api/slow/posts/$id/likes")
            .build()

           clientNewCall(id, callback, request)

    }


    override fun saveAsync(post: Post, callback: PostRepository.PostCallback<Post>) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts")
            .build()



        client.newCall(request)
            .enqueue(object : Callback {

                override fun onResponse(call: Call, response: Response) {
                    Log.d("NETWORK REQUEST", response.code.toString())
                    if (response.isSuccessful) {
                        val body =
                            response.body?.string()
                                ?: throw java.lang.RuntimeException("body is null")
                        try {
                            callback.onSuccess(gson.fromJson(body, Post::class.java))
                        } catch (e: Exception) {
                            callback.onError(e)
                        }

                    } else {
                        callback.onError(Exception(response.message))
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })

    }

    override fun removeByIdAsync(id: Long, callback: PostRepository.PostCallback<Unit>) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {

                override fun onResponse(call: Call, response: Response) {
                    Log.d("NETWORK REQUEST", response.code.toString())
                    if (response.isSuccessful) {
                        val body =
                            response.body?.string()
                                ?: throw java.lang.RuntimeException("body is null")
                        try {
                            callback.onSuccess(Unit)
                        } catch (e: Exception) {
                            callback.onError(e)
                        }

                    } else {
                        callback.onError(Exception(response.message))
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })

    }

//TODO:?????????? ?????????????? ?????? ?????????????????? ???????? clientNewCall

    private fun clientNewCall(
        id: Long,
        callback: PostRepository.PostCallback<Post>,
        request: Request
    ) {
        client.newCall(request)
            .enqueue(object : Callback {

                override fun onResponse(call: Call, response: Response) {
                    Log.d("NETWORK REQUEST", response.code.toString())
                    if (response.isSuccessful) {
                        val body =
                            response.body?.string()
                                ?: throw java.lang.RuntimeException("body is null")
                        try {
                            callback.onSuccess(gson.fromJson(body, Post::class.java))
                        } catch (e: Exception) {
                            callback.onError(e)
                        }

                    } else {
                        callback.onError(Exception(response.message))
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }


            })
    }
}

