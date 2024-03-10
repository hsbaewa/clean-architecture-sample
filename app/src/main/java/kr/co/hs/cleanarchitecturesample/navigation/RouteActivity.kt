package kr.co.hs.cleanarchitecturesample.navigation

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import kr.co.hs.cleanarchitecturesample.di.NavigatorQualifier
import kr.co.hs.cleanarchitecturesample.platform.Activity
import javax.inject.Inject

@AndroidEntryPoint
class RouteActivity : Activity() {

    @NavigatorQualifier
    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigator.startSearch(this)
    }
}