package kr.co.hs.cleanarchitecturesample.data.datasource

import kr.co.hs.cleanarchitecturesample.data.model.BookDetailsItemModel
import kr.co.hs.cleanarchitecturesample.data.model.BookNewResultModel
import kr.co.hs.cleanarchitecturesample.data.model.BookSearchResultModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BookStoreDataSource {
    @GET("search/{query}")
    suspend fun search(@Path("query") query: String): Response<BookSearchResultModel>

    @GET("search/{query}/{page}")
    suspend fun search(
        @Path("query") query: String,
        @Path("page") page: Int
    ): Response<BookSearchResultModel>

    @GET("new")
    suspend fun new(): Response<BookNewResultModel>

    @GET("books/{isbn13}")
    suspend fun details(@Path("isbn13") isbn13: String): Response<BookDetailsItemModel>
}