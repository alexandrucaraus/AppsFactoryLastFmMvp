package eu.caraus.appsflastfm.data.ui.main.albums

import eu.caraus.appsflastfm.common.schedulers.TestSchedulerProvider
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Album

import eu.caraus.appsflastfm.ui.main.albums.AlbumsContract
import eu.caraus.appsflastfm.ui.main.albums.AlbumsPresenter
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verifySequence
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Test
import rx.observers.TestSubscriber

class AlbumsPresenterTest {

    private val scheduler = TestSchedulerProvider()

    private var navg = mockk<AlbumsContract.Navigator>()
    private var view = mockk<AlbumsContract.View>( relaxed = true )
    private var intr = mockk<AlbumsContract.Interactor>( relaxed = true)


    @Test
    fun showAlbumsTest(){

        val testSubscriber = TestSubscriber<Any>()

        RxJavaPlugins.setErrorHandler {
            testSubscriber.onError(it)
        }

        val presenter = AlbumsPresenter( intr, navg, scheduler )

        presenter.onCreate()
        presenter.onViewAttached(view)
        presenter.onResume()

        val slot = slot<List<Album?>>()

        verifySequence {
            view.showLoading()
            view.hideLoading()
            view.updateAlbums( capture(slot))
        }

    }

}