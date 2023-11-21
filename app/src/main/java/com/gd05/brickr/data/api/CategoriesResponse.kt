package com.gd05.brickr.data.api

import com.google.gson.annotations.SerializedName

data class CategoriesResponse (
    @SerializedName("count"    ) var count    : Int?               = null,
    @SerializedName("next"     ) var next     : String?            = null,
    @SerializedName("previous" ) var previous : String?            = null,
    @SerializedName("results"  ) var results  : ArrayList<ApiCategorie> = arrayListOf()
)
data class ApiCategorie (
    @SerializedName("id"         ) var id        : Int?    = null,
    @SerializedName("name"       ) var name      : String? = null,
    @SerializedName("part_count" ) var partCount : Int?    = null
)