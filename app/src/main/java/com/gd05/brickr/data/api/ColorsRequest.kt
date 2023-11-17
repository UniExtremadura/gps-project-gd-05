package com.gd05.brickr.data.api

data class ColorsRequest (
    /**
     * A page number within the paginated result set.
     */
    var page: Integer? = null,
    /**
     * Number of results to return per page.
     */
    var pageSize: Integer? = null,
    /**
     * Which field to use when ordering the results.
     */
    var ordering: String? = null
)