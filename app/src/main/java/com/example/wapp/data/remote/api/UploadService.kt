package com.example.wapp.data.remote.api

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadService {
    @Multipart
    @POST("upload.php")
    fun uploadFile(
        @Part file: MultipartBody.Part // Data file yang akan diupload
    ): Call<UploadResponse>
}

