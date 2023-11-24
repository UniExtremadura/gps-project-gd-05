package com.gd05.brickr.data.api

import com.google.gson.annotations.SerializedName

data class SearchBrickByIdResponse(
    @SerializedName("part_num") var partNum: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("part_cat_id") var partCatId: Int? = null,
    @SerializedName("year_from") var yearFrom: Int? = null,
    @SerializedName("year_to") var yearTo: Int? = null,
    @SerializedName("part_url") var partUrl: String? = null,
    @SerializedName("part_img_url") var partImgUrl: String? = null,
    @SerializedName("prints") var prints: ArrayList<String> = arrayListOf(),
    @SerializedName("molds") var molds: ArrayList<String> = arrayListOf(),
    @SerializedName("alternates") var alternates: ArrayList<String> = arrayListOf(),
    @SerializedName("external_ids") var externalIds: SearchBrickByIdExternalIds? = SearchBrickByIdExternalIds(),
    @SerializedName("print_of") var printOf: String? = null
)
data class SearchBrickByIdExternalIds(
    @SerializedName("BrickLink") var BrickLink: ArrayList<String> = arrayListOf(),
    @SerializedName("BrickOwl") var BrickOwl: ArrayList<String> = arrayListOf(),
    @SerializedName("Brickset") var Brickset: ArrayList<String> = arrayListOf(),
    @SerializedName("LEGO") var LEGO: ArrayList<String> = arrayListOf()
)
