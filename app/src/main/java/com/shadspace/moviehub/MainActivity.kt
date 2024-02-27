package com.shadspace.moviehub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter

    val movieApi: MovieApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApi::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set default endpoint
        fetchMovies()

        // Implement dropdown to change filters
        // You can use a Spinner or other UI components for this
        // On selection change, call fetchMovies with the selected filter
    }

    private fun fetchMovies(filter: String = "top_rated") {
        // Make network request using Retrofit
        lifecycleScope.launch {
            try {
                val response = movieApi.getMovies(
                    apiKey = "13befb0c6409e8c61c5e9ec4265a1d1c", // Provide your actual API key here
                    sortBy = filter
                )
                if (response.isSuccessful) {
                    val movies = response.body()?.results ?: emptyList()
                    movieAdapter = MovieAdapter(movies)
                    recyclerView.adapter = movieAdapter
                } else {
                    // Handle error
                    val errorBody = response.errorBody()?.string()
                    Log.e("MovieFetchError", "Error fetching movies: $errorBody")
                    Toast.makeText(this@MainActivity, "Error fetching movies: $errorBody", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // Handle exception
                Toast.makeText(this@MainActivity, "Exception: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
