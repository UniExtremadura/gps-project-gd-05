package com.gd05.brickr.data.api

import com.google.gson.annotations.SerializedName
data class BricksResponse (
    @SerializedName("count"    ) var count    : Int?               = null,
    @SerializedName("next"     ) var next     : String?            = null,
    @SerializedName("previous" ) var previous : String?            = null,
    @SerializedName("results"  ) var results  : ArrayList<BrickApi> = arrayListOf()
)
data class BrickApi (
    @SerializedName("part_num"     ) var partNum     : String?      = null,
    @SerializedName("name"         ) var name        : String?      = null,
    @SerializedName("part_cat_id"  ) var partCatId   : Int?         = null,
    @SerializedName("part_url"     ) var partUrl     : String?      = null,
    @SerializedName("part_img_url" ) var partImgUrl  : String?      = null,
    @SerializedName("external_ids" ) var externalIds : ExternalIds? = ExternalIds(),
    @SerializedName("print_of"     ) var printOf     : String?      = null
)
data class ExternalIds (
    @SerializedName("BrickLink" ) var BrickLink : ArrayList<String> = arrayListOf(),
    @SerializedName("BrickOwl"  ) var BrickOwl  : ArrayList<String> = arrayListOf()
)

