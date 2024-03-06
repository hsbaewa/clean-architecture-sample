package kr.co.hs.cleanarchitecturesample

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [SingletonModule::class]
)
object FakeSingletonModule {
    @BookStoreRepositoryQualifier
    @Provides
    @Singleton
    fun provideBookStoreRepository() = TestBookStoreRepository()
}