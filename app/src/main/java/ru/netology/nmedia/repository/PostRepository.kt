package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post



interface PostRepository {


    fun likeByIdAsync( id: Long, callback: PostCallback <Post>)
    fun unlikeByIdAsync( id: Long, callback: PostCallback<Post>)
    fun saveAsync(post: Post, callback: PostCallback<Post>)
    fun removeByIdAsync(id: Long, callback: PostCallback<Unit>)

    fun getAllAsync(callback: PostCallback<List<Post>>)

    interface PostCallback<T> {
        fun onSuccess(value:T) {}
        fun onError(e:Exception) {}
    }
}
