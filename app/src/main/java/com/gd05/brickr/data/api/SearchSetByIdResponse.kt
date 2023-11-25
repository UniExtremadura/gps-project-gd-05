package com.gd05.brickr.data.api

import com.google.gson.annotations.SerializedName

data class SearchSetByIdResponse(
    @SerializedName("set_num") var setNum: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("year") var year: Int? = null,
    @SerializedName("theme_id") var themeId: Int? = null,
    @SerializedName("num_parts") var numParts: Int? = null,
    @SerializedName("set_img_url") var setImgUrl: String? = null,
    @SerializedName("set_url") var setUrl: String? = null,
    @SerializedName("last_modified_dt") var lastModifiedDt: String? = null
)