package com.example.mymvvpapp


import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.lifecycle.lifecycleScope
import com.example.mymvvpapp.data.model.Post
import com.example.mymvvpapp.ui.theme.MyMVVPAppTheme
import com.example.mymvvpapp.viewmodel.PostsViewModel
import kotlinx.coroutines.*
import kotlin.concurrent.thread

class MainActivity : ComponentActivity() {

    var counter: Int = 0


    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyMVVPAppTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    runBlocking {
                        //ui works
                        GlobalScope.launch(context = Dispatchers.Main) {
                            Log.e("2323", "Main in : Thread -> ${Thread.currentThread().name}")

                        }

                        launch(context = Dispatchers.Unconfined) {
                            Log.e(
                                "2323",
                                "Unconfined1 in : Thread -> ${Thread.currentThread().name}"
                            )
                            delay(1000)
                            Log.e(
                                "2323",
                                "Unconfined2 in : Thread -> ${Thread.currentThread().name}"
                            )
                        }


                        launch(context = Dispatchers.IO) {
                            Log.e("2323", "IO in : Thread -> ${Thread.currentThread().name}")
                        }

                        //cpu heavy works
                        launch(context = Dispatchers.Default) {
                            Log.e("2323", "Default in : Thread -> ${Thread.currentThread().name}")
                        }
                    }
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

            val viewModel = ViewModelProvider(this@MainActivity).get(PostsViewModel::class.java)
            viewModel.getAllPostsRequest()

            viewModel.postsList.observe(this@MainActivity) { posts ->
                postsList = posts
            }

            viewModel.postsListError.observe(this@MainActivity) { isError ->
                isError?.let {
                    Log.e("2323", isError)
                }
            }

            viewModel.loading.observe(this@MainActivity) { isLoading ->
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
