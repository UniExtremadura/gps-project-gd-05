package com.gd05.brickr.data.api

data class ThemesRequest(
    var page: Int? = null,
    var pageSize: Int? = null,
    var ordering: String? = null,
)
data class ThemeByIdRequest(
    var id: Int,
    var ordering: String? = null,
)