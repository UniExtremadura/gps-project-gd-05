package com.gd05.brickr.api

import com.gd05.brickr.util.BACKGROUND
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

class RebrickableApiTest {
    lateinit var api: RebrickableAPI
    val authKey: String = "4d61a67703f579104648c6c34bbc7709"

    @Before
    fun setUp() {
        api = getRebrickableApi()
    }

    /*@Test
    fun apiConnectionTest(){
        var response = api.getColors(authKey).execute()
        if (response.isSuccessful) {
            response.body()?.results?.forEach {println(it)}
        }
        BACKGROUND.submit {
        }
        BACKGROUND.awaitTermination(60, TimeUnit.SECONDS)
        assert(true)
    }*/
}