package kr.co.hs.cleanarchitecturesample

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {

    @BookStoreRepositoryQualifier
    @Provides
    @Singleton
    fun provideBookStoreRepository() {
        TODO("BookStoreRepository 실체화")
    }
}