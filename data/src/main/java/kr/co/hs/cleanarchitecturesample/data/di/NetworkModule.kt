package kr.co.hs.cleanarchitecturesample.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.co.hs.cleanarchitecturesample.data.datasource.BookStoreDataSource
import kr.co.hs.cleanarchitecturesample.data.repository.BookStoreRepositoryImpl
import kr.co.hs.cleanarchitecturesample.domain.repository.BookStoreRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Singleton
    @Provides
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(httpClient)
            .baseUrl("https://api.itbook.store/1.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideBookStoreDataSource(retrofit: Retrofit): BookStoreDataSource =
        retrofit.create(BookStoreDataSource::class.java)

    @Singleton
    @Provides
    @BookStoreRepositoryQualifier
    fun providerBookStoreRepository(bookStoreDataSource: BookStoreDataSource): BookStoreRepository =
        BookStoreRepositoryImpl(bookStoreDataSource)
}