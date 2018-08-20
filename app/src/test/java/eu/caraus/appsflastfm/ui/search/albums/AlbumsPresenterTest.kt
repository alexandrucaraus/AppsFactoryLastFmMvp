package eu.caraus.appsflastfm.ui.search.albums

import android.widget.ImageView
import eu.caraus.appsflastfm.common.extensions.loading
import eu.caraus.appsflastfm.common.extensions.success
import eu.caraus.appsflastfm.common.retrofit.Outcome
import eu.caraus.appsflastfm.common.schedulers.TestSchedulerProvider

import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Album
import eu.caraus.appsflastfm.data.domain.lastFm.albums.AlbumItem

import io.mockk.mockk
import io.mockk.slot
import io.mockk.verifyOrder
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import rx.observers.TestSubscriber

class AlbumsPresenterTest {


    private val scheduler = TestSchedulerProvider()

    private var navigator = mockk<AlbumsContract.Navigator>( relaxed = true)
    private var view = mockk<AlbumsContract.View>( relaxed = true )

    private var imageView = mockk<ImageView>( relaxed = true )

    @Before
    fun before(){
        val testSubscriber = TestSubscriber<Any>()
        RxJavaPlugins.setErrorHandler {
            Assert.assertEquals("isSuccess", it.localizedMessage )
            testSubscriber.onError( it )
        }
    }

    @After()
    fun after() {

    }

    @Test
    fun showAlbumsTestPopulatedData() {

        val slot = slot<List<AlbumItem?>>()

        val presenter = AlbumsPresenter( createAlbumsInteractor( albumsList()), navigator , scheduler )

        presenter.onCreate() ; presenter.getAlbums("item1")
        presenter.onViewAttached( view )
        presenter.onResume()

        verifyOrder {
            view.hidePlaceholder()
            view.showFoundAlbums( capture( slot))
            view.hideLoading()
            view.showList()
        }

    }


    private fun createAlbumsInteractor( albums : List<AlbumItem?> )
            : AlbumsContract.Interactor = object : AlbumsContract.Interactor {

        private val albumsFetchResult = PublishSubject.create<Outcome<List<AlbumItem?>>>()
        private val albumsSaveResult = PublishSubject.create<Outcome<Album>>()

        override fun getAlbums( artistName : String ) {
            albumsFetchResult.loading(true)
            albumsFetchResult.success( albums )
        }

        override fun saveAlbum(artistName: String, albumName: String) {

        }

        override fun getAlbumsOutcome(): PublishSubject<Outcome<List<AlbumItem?>>>
                = albumsFetchResult

        override fun getAlbumSaveOutcome(): PublishSubject<Outcome<Album>>
                = albumsSaveResult

    }

    private fun albumsList() = listOf (
            AlbumItem().apply {
                mbid = "item1"
                url  = "imageUrl1"
            },
            AlbumItem().apply {
                mbid = "item2"
                url  = "imageUrl2"
            },
            AlbumItem().apply {
                mbid = "item3"
                url  = "imageUrl1"
            }
    )

}