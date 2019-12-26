package com.unitracer.instructpro

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.ee.core.extension.loading
import com.ee.core.extension.toLiveData
import com.ee.core.networking.AppScheduler
import com.ee.core.networking.Outcome
import com.ee.core.networking.Scheduler
import com.unitracer.instructpro.common.ApiConstants
import com.unitracer.instructpro.data.remote.model.currentUser.UserResponse
import com.unitracer.instructpro.data.remote.model.login.LoginRequest
import com.unitracer.instructpro.data.remote.model.login.LoginResponse
import com.unitracer.instructpro.data.remote.service.InstructProService
import com.unitracer.instructpro.data.repository.implementor.LoginRepositoryImpl
import com.unitracer.instructpro.data.repository.interactor.LoginRepository
import com.unitracer.instructpro.di.component.DaggerAppComponent
import com.unitracer.instructpro.di.component.LoginComponent
import com.unitracer.instructpro.di.module.AppModule
import com.unitracer.instructpro.di.module.LoginModule
import com.unitracer.instructpro.presentation.login.LoginViewModel
import com.unitracer.instructpro.presentation.splash.SplashViewModel
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import it.cosenonjaviste.daggermock.DaggerMockRule
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.plugins.RxAndroidPlugins






@RunWith(JUnit4::class)
class LoginViewModelTest {


    @Rule @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    @Mock
    val mLoginRemote: LoginRepository.Remote? = null

    var mCompoDisposable: CompositeDisposable = CompositeDisposable()

    @Mock
    internal var observer: Observer<Outcome<LoginResponse>>? = null


    @Mock
    internal var observer1: Observer<Outcome<UserResponse>>? = null

    @Mock
    var mInstructProService: InstructProService? = null

    private var loginViewModel: LoginViewModel? = null

    private val loginRequest = LoginRequest("","", "", "","", "")

    private val loginHashMap  =HashMap<String, String>()

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> Schedulers.trampoline() }

        MockitoAnnotations.initMocks(this)

        val mLoginRepository = LoginRepositoryImpl(mLoginRemote!!,AppScheduler(), mCompoDisposable)

        loginViewModel = LoginViewModel(mLoginRepository, mCompoDisposable)
        loginViewModel?.mLoginResponse?.observeForever(observer!!)
        loginViewModel?.mCurrentUserResponse?.observeForever(observer1!!)
        loginHashMap[ApiConstants.KEY_GRANT_TYPE] = loginRequest.grant_type
        loginHashMap[ApiConstants.KEY_USER_NAME] = loginRequest.username
        loginHashMap[ApiConstants.KEY_PASSWORD] = loginRequest.password
        loginHashMap[ApiConstants.KEY_CLIENT_ID] = loginRequest.client_id
        loginHashMap[ApiConstants.KEY_CLIENT_SECRET] = loginRequest.client_secret
        loginHashMap[ApiConstants.KEY_SCOPE] = loginRequest.scope

    }


    @Test
    fun testApiFetchDataSuccess() {
        Mockito.`when`(mLoginRemote?.doLogin(loginRequest)).thenReturn(Flowable.just(LoginResponse("", "", "", "")))
        loginViewModel?.doLogin(loginRequest)

        loginViewModel?.mLoginResponse?.observeForever(observer!!)

        Mockito.verify<Observer<Outcome<LoginResponse>>>(observer!!).onChanged(Outcome.Progress(true))
        Mockito.verify<Observer<Outcome<LoginResponse>>>(observer!!).onChanged(Outcome.Success<LoginResponse>(LoginResponse("", "", "", "")))
    }



    @Test
    fun testApi1FetchDataSuccess() {
        Mockito.`when`(mLoginRemote?.fetchCurrentUser("")).thenReturn(Flowable.just(UserResponse()))
        loginViewModel?.fetchCurrentUser("")

        loginViewModel?.mCurrentUserResponse?.observeForever(observer1!!)

        Mockito.verify<Observer<Outcome<UserResponse>>>(observer1).onChanged(Outcome.Progress(true))
        Mockito.verify<Observer<Outcome<UserResponse>>>(observer1).onChanged(Outcome.Success<UserResponse>(UserResponse()))
    }

}

