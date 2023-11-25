package com.gd05.brickr.api

import com.gd05.brickr.data.api.BricksRequest
import com.gd05.brickr.data.api.CategoriesRequest
import com.gd05.brickr.data.api.CategoryByIdRequest
import com.gd05.brickr.data.api.ColorsRequest
import com.gd05.brickr.data.api.SearchRequest
import com.gd05.brickr.data.api.ThemeByIdRequest
import com.gd05.brickr.data.api.ThemesRequest
import org.junit.Before
import org.junit.Test

class RebrickableServiceTest {
    lateinit var service: RebrickableService

    @Before
    fun beforeTest() {
        service = RebrickableService
        // Esperamos 1 segundo y 1 milésima para no sobrecargar la API
        Thread.sleep(1100)
    }

    @Test
    fun test_colors() {
        var response = service.getColors(ColorsRequest()).execute()
        if (response.isSuccessful) {
            assert(response.body() != null)
        } else
            assert(false)
    }

    @Test
    fun test_getColorsById() {
        var response = service.getColorsById(0).execute()
        if (response.isSuccessful) {
            assert(response.body() != null)
        } else
            assert(false)
    }

    @Test
    fun test_searchBricks() {
        var response = service.searchBricks(BricksRequest(search = "batman")).execute()
        if (response.isSuccessful) {
            assert(response.body() != null)
        } else
            assert(false)
    }

    @Test
    fun test_searchBrickById() {
        var response = service.searchBrickById("3245cpr0002").execute()
        if (response.isSuccessful) {
            assert(response.body() != null)
        } else
            assert(false)
    }

    @Test
    fun test_searchSet() {
        var response = service.searchSet(SearchRequest(search = "batman")).execute()
        if (response.isSuccessful) {
            assert(response.body() != null)
        } else
            assert(false)
    }

    @Test
    fun test_searchSetById() {
        var response = service.searchSetById("001-1").execute()
        if (response.isSuccessful) {
            assert(response.body() != null)
        } else
            assert(false)
    }

    @Test
    fun test_getCategories() {
        var response = service.getCategories(CategoriesRequest()).execute()
        if (response.isSuccessful) {
            assert(response.body() != null)
        } else
            assert(false)
    }

    @Test
    fun test_getCategoryById() {
        var response = service.getCategoryById(CategoryByIdRequest(id = 1)).execute()
        if (response.isSuccessful) {
            assert(response.body() != null)
        } else
            assert(false)
    }

    @Test
    fun test_getThemes() {
        var response = service.getThemes(ThemesRequest()).execute()
        if (response.isSuccessful) {
            assert(response.body() != null)
        } else
            assert(false)
    }

    @Test
    fun test_getThemeById() {
        var response = service.getThemeById(ThemeByIdRequest(id = 1)).execute()
        if (response.isSuccessful) {
            assert(response.body() != null)
        } else
            assert(false)
    }
}