package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.*
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.IOException
import kotlin.Exception
import kotlin.concurrent.thread

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    authorAvatar = "",
    likedByMe = false,
    likes = 0,
    published = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    // упрощённый вариант
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {

        loadPosts()
    }

    fun loadPosts() {

        _data.postValue(FeedModel(loading = true))
        repository.getAllAsync(object : PostRepository.PostCallback<List<Post>> {
            override fun onSuccess(value: List<Post>) {
                _data.postValue(FeedModel(posts = value, empty = value.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun likeById(id: Long) {

//        _data.postValue(FeedModel(loading = true))
        repository.likeByIdAsync(id, object : PostRepository.PostCallback<Post> {
            override fun onSuccess(value: Post) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .map {
                            if (it.id != id) it
                            else value
                        })
                )
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }

        })
    }


    fun unlikeById(id: Long) {
//        _data.postValue(FeedModel(loading = true))
        repository.unlikeByIdAsync(id, object : PostRepository.PostCallback<Post> {
            override fun onSuccess(value: Post) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .map {
                            if (it.id != id) it
                            else value
                        })
                )
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }

        })
    }

    fun removeById(id: Long) {
        val old = _data.value?.posts.orEmpty()
        _data.postValue(
            _data.value?.copy(posts = _data.value?.posts.orEmpty()
                .filter { it.id != id }
            )
        )
        repository.removeByIdAsync(id, object : PostRepository.PostCallback<Unit> {
            override fun onSuccess(value: Unit) {
            }

            override fun onError(e: Exception) {
                _data.postValue(_data.value?.copy(posts = old))
            }

        })
    }





    fun save() {
        edited.value?.let {
            repository.saveAsync(it, object : PostRepository.PostCallback<Post> {
                override fun onSuccess(value: Post) {
                    _postCreated.postValue(Unit)
                }

                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = true))
                }
            })
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }



}
