package com.gd05.brickr.data.api

data class BricksRequest(
    // A page number within the paginated result set.
    var page: Int? = null,
    // Number of results to return per page.
    var pageSize: Int? = null,
    var partNum: String? = null,
    var partNums: String? = null,
    var partCatId: String? = null,
    var colorId: String? = null,
    var bricklinkId: String? = null,
    var brickowlId: String? = null,
    var legoId: String? = null,
    var ldrawId: String? = null,
    // Which field to use when ordering the results.
    var ordering: String? = null,
    // A search term.
    var search: String
)