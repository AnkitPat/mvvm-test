package com.unitracer.instructpro

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.unitracer.instructpro.presentation.splash.SplashViewModel
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.MockitoAnnotations


@RunWith(JUnit4::class)
class SplashViewModelTest {


    @Rule @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private var splashViewModel: SplashViewModel? = null

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        splashViewModel = SplashViewModel(1000)
    }


    @Test
    fun checkSplashTimeIsCorrect() {
        assertEquals(splashViewModel?.time, 1000L)
    }
}

