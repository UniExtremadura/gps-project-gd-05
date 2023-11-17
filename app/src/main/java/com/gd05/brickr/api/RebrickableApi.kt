package com.gd05.brickr.api

import com.example.example.ColorApi
import com.example.example.ColorsResponse
import com.gd05.brickr.data.api.ApiSet
import com.gd05.brickr.data.api.BricksResponse
import com.gd05.brickr.data.api.SearchResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private val api: RebrickableAPI by lazy {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor())
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://rebrickable.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    retrofit.create(RebrickableAPI::class.java)
}

fun getRebrickableApi() = api

interface RebrickableAPI {
    @GET("/api/v3/lego/colors")
    fun getColors(
        @Query("key") key: String,
        @Query("page") page: Integer?,
        @Query("page_size") pageSize: Integer?,
        @Query("ordering") ordering: String?
    ): Call<ColorsResponse>

    @GET("/api/v3/lego/colors/{id}")
    fun getColorById(
        @Query("key") key: String,
        @Path("id") setNum: String
    ): Call<ColorApi>

    @GET("/api/v3/lego/sets/")
    fun searchSet(
        @Query("key") key: String,
        @Query("page") page: Integer?,
        @Query("page_size") pageSize: Integer?,
        @Query("theme_id") themeId: String?,
        @Query("min_year") minYear: Double?,
        @Query("max_year") maxYear: Double?,
        @Query("max_parts") maxParts: Double?,
        @Query("ordering") ordering: String?,
        @Query("search") search: String
    ): Call<SearchResponse>

    @GET("/api/v3/lego/sets/{setNum}")
    fun searchSetById(
        @Query("key") key: String,
        @Path("setNum") setNum: String
    ): Call<ApiSet>

    @GET("/api/v3/lego/parts/")
    fun searchBricks(
        @Query("key") key: String,
        @Query("page") page: Integer?,
        @Query("page_size") pageSize: Integer?,
        @Query("part_num") partNum: String?,
        @Query("part_nums") partNums: String?,
        @Query("part_cat_id") partCatId: String?,
        @Query("color_id") colorId: String?,
        @Query("bricklink_id") bricklinkId: String?,
        @Query("brickowl_id") brickowlId: String?,
        @Query("lego_id") legoId: String?,
        @Query("ldraw_id") ldrawId: String?,
        @Query("ordering") ordering: String?,
        @Query("search") search: String,
    ): Call<BricksResponse>

    @GET("/api/v3/lego/parts/{part_num}")
    fun searchBrickById(
        @Query("key") key: String,
        @Path("part_num") brickNum: String
    ): Call<BricksResponse>
}

class APIError(message: String, cause: Throwable?) : Throwable(message, cause)

interface APICallback {
    fun onCompleted(tvShows: List<Any?>)
    fun onError(cause: Throwable)
}