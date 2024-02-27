package com.shadspace.moviehub

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("discover/movie")
    suspend fun getMovies(
        @Query("api_key") apiKey: String,
        @Query("sort_by") sortBy: String = "popularity.desc"
    ): Response<MovieResponse>
}

data class MovieResponse(
    val results: List<Movie>
)
