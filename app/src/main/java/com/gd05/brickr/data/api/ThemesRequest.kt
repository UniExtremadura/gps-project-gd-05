package com.gd05.brickr.data.api

data class ThemesRequest(
    var page: Integer?,
    var pageSize: Integer?,
    var ordering: String?,
)
data class ThemeByIdRequest(
    var id: Integer,
    var ordering: String?,
)