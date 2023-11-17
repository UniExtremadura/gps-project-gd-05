package com.gd05.brickr.api

import com.example.example.ColorApi
import com.example.example.ColorsResponse
import com.gd05.brickr.data.api.BricksRequest
import com.gd05.brickr.data.api.BricksResponse
import com.gd05.brickr.data.api.ColorsRequest
import com.gd05.brickr.data.api.SearchRequest
import com.gd05.brickr.data.api.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

object RebrickableService {

    val authKey: String = "4d61a67703f579104648c6c34bbc7709"
    var api = getRebrickableApi()

    fun searchSet(request: SearchRequest): Call<SearchResponse> {
        return api.searchSet(
            authKey,
            request.page,
            request.pageSize,
            request.themeId,
            request.minYear,
            request.maxYear,
            request.maxParts,
            request.ordering,
            request.search
        )
    }

    fun searchSetById(request: SearchRequest): Call<SearchResponse> {
        return api.searchSet(
            authKey,
            request.page,
            request.pageSize,
            request.themeId,
            request.minYear,
            request.maxYear,
            request.maxParts,
            request.ordering,
            request.search
        )
    }

    fun getColors(req: ColorsRequest): Call<ColorsResponse> {
        return api.getColors(authKey, req.page, req.pageSize, req.ordering)
    }
    fun getColorsById(setNum: String): Call<ColorApi> {
        return api.getColorById(authKey, setNum)
    }

    fun searchBricks(req: BricksRequest): Call<BricksResponse> {
        return api.searchBricks(
            authKey,
            req.page,
            req.pageSize,
            req.partNum,
            req.partNums,
            req.partCatId,
            req.colorId,
            req.bricklinkId,
            req.brickowlId,
            req.legoId,
            req.ldrawId,
            req.ordering,
            req.search
        )
    }

    fun searchBrickById(setNum: String): Call<BricksResponse> {
        return api.searchBrickById(authKey, setNum)
    }
}