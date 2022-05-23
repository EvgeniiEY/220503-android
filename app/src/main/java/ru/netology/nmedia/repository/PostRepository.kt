package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {

    fun likeByIdAsync(callback: PostCallback<List<Post>>)
    fun unlikeByIdAsync(callback: PostCallback<List<Post>>)
    fun save(post: Post)
    fun removeById(id: Long)

    fun getAllAsync(callback: PostCallback<List<Post>>)

    interface PostCallback<T> {
        fun onSuccess(value:T) {}
        fun onError(e:Exception) {}
    }
}
