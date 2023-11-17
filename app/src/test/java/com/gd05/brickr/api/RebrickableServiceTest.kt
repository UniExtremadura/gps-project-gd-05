package com.gd05.brickr.api

import org.junit.Before
import org.junit.Test

class RebrickableServiceTest {
    lateinit var service: RebrickableService
    @Before
    fun setUp() {
        service = RebrickableService
    }

    @Test
    fun test_colors(){
        var response = service.getColors().execute()
        if (response.isSuccessful) {
            response.body()?.results?.forEach {println(it)}
            assert(true)
        }else
            assert(false)
    }
}