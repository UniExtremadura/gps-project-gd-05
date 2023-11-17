package com.gd05.brickr.data.api

import android.icu.number.IntegerWidth

data class SearchRequest(
    // A page number within the paginated result set.
    var page: Integer? = null,
    // Number of results to return per page.
    var pageSize: Integer? = null,
    var themeId: String? = null,
    var minYear: Double? = null,
    var maxYear: Double? = null,
    var minParts: Double? = null,
    var maxParts: Double? = null,
    // Which field to use when ordering the results.
    var ordering: String? = null,
    // A search term.
    var search: String
)