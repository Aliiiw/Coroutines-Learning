package com.example.mymvvpapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymvvpapp.data.model.Post
import com.example.mymvvpapp.data.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostsViewModel : ViewModel() {

    val postsList = MutableLiveData<List<Post>>()
    val postsListError = MutableLiveData<String?>()
    val loading = MutableLiveData<Boolean?>()


    fun getAllPostsRequest() {

        loading.value = true

        CoroutineScope(Dispatchers.IO).launch {

            val response = ApiClient.api.getAllPosts()

            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.let { allPosts ->
                        postsList.value = allPosts
                        postsListError.value = null
                        loading.value = false
                    }
                } else {
                    postsListError.value = response.message()
                    loading.value = false
                }
            }
        }
    }
}