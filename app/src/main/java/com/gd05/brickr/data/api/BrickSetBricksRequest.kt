package com.gd05.brickr.data.api

data class BrickSetBricksRequest (
    // A page number within the paginated result set.
    var page: Int? = null,
    // Number of results to return per page.
    var pageSize: Int? = null,
    var setNum: String? = null,

)