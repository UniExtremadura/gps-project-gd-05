package com.gd05.brickr.api

import com.example.example.ColorApi
import com.example.example.ColorsResponse
import com.gd05.brickr.data.api.ApiCategorie
import com.gd05.brickr.data.api.ApiSet
import com.gd05.brickr.data.api.ApiTheme
import com.gd05.brickr.data.api.BricksResponse
import com.gd05.brickr.data.api.CategoriesResponse
import com.gd05.brickr.data.api.SearchBrickByIdResponse
import com.gd05.brickr.data.api.SearchResponse
import com.gd05.brickr.data.api.SearchSetByIdResponse
import com.gd05.brickr.data.api.ThemesResponse
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
        @Query("page") page: Int?,
        @Query("page_size") pageSize: Int?,
        @Query("ordering") ordering: String?
    ): Call<ColorsResponse>

    @GET("/api/v3/lego/colors/{id}")
    fun getColorById(
        @Path("id") setNum: Int,
        @Query("key") key: String
    ): Call<ColorApi>

    @GET("/api/v3/lego/sets/")
    fun searchSet(
        @Query("key") key: String,
        @Query("page") page: Int?,
        @Query("page_size") pageSize: Int?,
        @Query("theme_id") themeId: String?,
        @Query("min_year") minYear: Double?,
        @Query("max_year") maxYear: Double?,
        @Query("max_parts") maxParts: Double?,
        @Query("ordering") ordering: String?,
        @Query("search") search: String
    ): Call<SearchResponse>

    @GET("/api/v3/lego/sets/{setNum}/")
    fun searchSetById(
        @Path("setNum") setNum: String,
        @Query("key") key: String
    ): Call<SearchSetByIdResponse>

    @GET("/api/v3/lego/parts/")
    fun searchBricks(
        @Query("key") key: String,
        @Query("page") page: Int?,
        @Query("page_size") pageSize: Int?,
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

    @GET("/api/v3/lego/parts/{part_num}/")
    fun searchBrickById(
        @Path("part_num") brickNum: String,
        @Query("key") key: String
    ): Call<SearchBrickByIdResponse>

    @GET("/api/v3/lego/part_categories/")
    fun getCategories(
        @Query("key") key: String,
        @Query("page") page: Int?,
        @Query("page_size") pageSize: Int?,
        @Query("ordering") ordering: String?,
    ): Call<CategoriesResponse>

    @GET("/api/v3/lego/part_categories/{id}/")
    fun getCategoryById(
        @Path("id") id: Int,
        @Query("key") key: String,
        @Query("ordering") ordering: String?
    ): Call<ApiCategorie>

    @GET("/api/v3/lego/themes/")
    fun getThemes(
        @Query("key") key: String,
        @Query("page") page: Int?,
        @Query("page_size") pageSize: Int?,
        @Query("ordering") ordering: String?,
    ): Call<ThemesResponse>

    @GET("/api/v3/lego/themes/{id}/")
    fun getThemeById(
        @Path("id") id: Int,
        @Query("key") key: String,
        @Query("ordering") ordering: String?,
    ): Call<ApiTheme>
}