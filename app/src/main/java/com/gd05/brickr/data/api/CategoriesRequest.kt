package com.gd05.brickr.data.api

import retrofit2.http.Path
import retrofit2.http.Query

data class CategoriesRequest (
    var page: Integer?,
    var pageSize: Integer?,
    var ordering: String?,
)

data class CategoryByIdRequest(
    var id: String,
    var ordering: String?
)