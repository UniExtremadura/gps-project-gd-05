package com.gd05.brickr.data.api


import com.google.gson.annotations.SerializedName


data class ThemesResponse (
    @SerializedName("count"    ) var count    : Int?               = null,
    @SerializedName("next"     ) var next     : String?            = null,
    @SerializedName("previous" ) var previous : String?            = null,
    @SerializedName("results"  ) var results  : ArrayList<ApiTheme> = arrayListOf()
)
data class ApiTheme (
    @SerializedName("id"        ) var id       : Int?    = null,
    @SerializedName("parent_id" ) var parentId : String? = null,
    @SerializedName("name"      ) var name     : String? = null
)