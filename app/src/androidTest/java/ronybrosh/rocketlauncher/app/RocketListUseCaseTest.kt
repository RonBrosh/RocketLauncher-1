package ronybrosh.rocketlauncher.app

import androidx.test.platform.app.InstrumentationRegistry
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subscribers.TestSubscriber
import org.junit.Before
import org.junit.Test
import ronybrosh.rocketlauncher.data.db.RocketLauncherDatabase
import ronybrosh.rocketlauncher.data.db.dao.RocketDao
import ronybrosh.rocketlauncher.domain.entities.ResultState
import ronybrosh.rocketlauncher.domain.entities.Rocket
import ronybrosh.rocketlauncher.domain.usecases.RocketListUseCase

class RocketListUseCaseTest : BaseUseCaseTest() {

    private lateinit var useCase: RocketListUseCase
    private lateinit var local: RocketDao

    @Before
    fun setup() {
        useCase = provideRocketListUseCase()
        local = RocketLauncherDatabase.getInstance(InstrumentationRegistry.getInstrumentation().targetContext)
            .getRocketDao()
    }

    @Test
    fun testGetRocketList() {
        val compositeDisposable = CompositeDisposable()

        local.deleteRocketTable()
        Thread.sleep(3000)

        compositeDisposable.addAll(useCase.getRocketList().subscribe({
            logResultState(it)
        }, {
            logError(it)
        }))

        Thread.sleep(3000)
        useCase.refreshRocketList()

        Thread.sleep(3000)

        compositeDisposable.clear()
        val testObserver: TestSubscriber<ResultState<List<Rocket>>> = TestSubscriber()
        testObserver.await()
    }
}