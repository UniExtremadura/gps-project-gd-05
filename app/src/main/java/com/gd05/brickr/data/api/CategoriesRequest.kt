package com.gd05.brickr.data.api

data class CategoriesRequest (
    var page: Int? = null,
    var pageSize: Int? = null,
    var ordering: String? = null,
)

data class CategoryByIdRequest(
    var id: Int,
    var ordering: String? = null
)

