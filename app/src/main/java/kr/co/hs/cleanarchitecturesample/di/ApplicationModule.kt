package kr.co.hs.cleanarchitecturesample.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.co.hs.cleanarchitecturesample.navigation.Navigator
import kr.co.hs.cleanarchitecturesample.navigation.NavigatorImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    @Provides
    @Singleton
    @NavigatorQualifier
    fun provideNavigator(): Navigator = NavigatorImpl()
}