package com.example.mymvvpapp


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.mymvvpapp.data.model.Post
import com.example.mymvvpapp.ui.theme.MyMVVPAppTheme
import com.example.mymvvpapp.viewmodel.PostsViewModel
import kotlinx.coroutines.*
import kotlin.concurrent.thread

class SecondActivity : ComponentActivity() {

    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyMVVPAppTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Text(text = "Second")
                }
            }
        }
    }

    @Composable
    private fun ObservePostsViewModel() {

        var postsList by remember { mutableStateOf(emptyList<Post>()) }

        Column() {
            PostView(postList = postsList)
        }

        LaunchedEffect(key1 = Unit) {

            val viewModel = ViewModelProvider(this@SecondActivity).get(PostsViewModel::class.java)
            viewModel.getAllPostsRequest()

            viewModel.postsList.observe(this@SecondActivity) { posts ->
                postsList = posts
            }

            viewModel.postsListError.observe(this@SecondActivity) { isError ->
                isError?.let {
                    Log.e("2323", isError)
                }
            }

            viewModel.loading.observe(this@SecondActivity) { isLoading ->
                Log.e("2323", isLoading.toString())
            }
        }
    }

    @Composable
    fun PostView(postList: List<Post>) {
        LazyColumn() {
            items(postList) { post ->
                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .background(Color.Blue)
                ) {
                    Text(text = post.title, color = Color.White)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = post.body, color = Color.DarkGray)
                }

            }
        }
    }
}
