package com.lzx.nickphoto.utils.network

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by lzx on 2017/7/4.
 */
interface PhotoServer {

    @GET("photos?client_id=6c18f0d4f3c1fcd37b2388ec2c543f272777584f8ed62a4bcd0fba0fe904c6f8")
    fun getAllPhoto(@Query("page") page: Int, @Query("per_page") per_page: Int): Observable<ResponseBody>
}