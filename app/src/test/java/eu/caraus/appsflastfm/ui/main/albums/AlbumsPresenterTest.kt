package eu.caraus.appsflastfm.ui.main.albums

import android.widget.ImageView
import eu.caraus.appsflastfm.common.extensions.loading
import eu.caraus.appsflastfm.common.extensions.success
import eu.caraus.appsflastfm.common.retrofit.Outcome
import eu.caraus.appsflastfm.common.schedulers.TestSchedulerProvider
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Album
import io.mockk.*
import io.reactivex.disposables.CompositeDisposable

import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import rx.observers.TestSubscriber

class AlbumsPresenterTest {

    private val scheduler = TestSchedulerProvider()

    private var navigator = mockk<AlbumsContract.Navigator>( relaxed = true)
    private var view = mockk<AlbumsContract.View>( relaxed = true )

    private var imageView = mockk<ImageView>( relaxed = true )

    private var disposable = CompositeDisposable()

    @Before
    fun before(){

        val testSubscriber = TestSubscriber<Any>()

        RxJavaPlugins.setErrorHandler {
            testSubscriber.onError(it)
        }

    }

    @After()
    fun after() {
        disposable.dispose()
    }

    @Test
    fun showAlbumsTestPopulatedData() {

        val slot = slot<List<Album?>>()

        val presenter = AlbumsPresenter( createAlbumsInteractor( populatedItems()),
                                            navigator , scheduler, disposable )

        presenter.onCreate()
        presenter.onViewAttached( view )
        presenter.onResume()

        verifyOrder {
            view.hidePlaceholder()
            view.updateAlbums( capture( slot))
            view.hideLoading()
            view.showList()
        }

    }

    @Test
    fun showAlbumsTestEmptyData() {

        val slot = slot<List<Album?>>()

        val presenter = AlbumsPresenter( createAlbumsInteractor( listOf() ) ,
                                            navigator , scheduler, disposable )

        presenter.onCreate()
        presenter.onViewAttached( view )
        presenter.onResume()

        verifyOrder {
            view.showPlaceholder()
            view.updateAlbums( capture( slot))
            view.hideLoading()
            view.hideList()
        }

    }

    @Test
    fun showAlbumsDetailsTest() {

        val albumListSlot = slot<List<Album?>>()
        val albumItemSlot = slot<String>()

        val presenter = AlbumsPresenter( createAlbumsInteractor( populatedItems()),
                                            navigator, scheduler, disposable )
        presenter.onCreate()
        presenter.onViewAttached( view )
        presenter.onResume()

        verifyOrder {
            view.hidePlaceholder()
            view.updateAlbums( capture( albumListSlot))
            view.hideLoading()
            view.showList()
        }

        val firstItem = 0

        presenter.showAlbumDetails( albumListSlot.captured[firstItem]?.mbid!! )

        verify {
            navigator.showAlbumDetails( capture( albumItemSlot ) )
        }

        assertEquals( albumItemSlot.captured , "item1" )

    }

    @Test
    fun showAlbumsDetailsWithAnimationTest() {

        val albumListSlot = slot<List<Album?>>()
        val albumItemSlot = slot<String>()
        val albumUrlSlot  = slot<String>()

        val presenter = AlbumsPresenter( createAlbumsInteractor( populatedItems()),
                                            navigator, scheduler, disposable )

        presenter.onCreate()
        presenter.onViewAttached( view )
        presenter.onResume()

        verifyOrder {
            view.hidePlaceholder()
            view.updateAlbums( capture( albumListSlot))
            view.hideLoading()
            view.showList()
        }

        val firstItem = 0

        presenter.showAlbumDetails( albumListSlot.captured[firstItem]?.mbid!!,
                                    albumListSlot.captured[firstItem]?.url!! ,
                                    imageView )

        verify {
            navigator.showAlbumDetails( capture( albumItemSlot ),
                                        capture( albumUrlSlot),
                                        capture( slot()))
        }

        assertEquals( albumItemSlot.captured , "item1" )
        assertEquals( albumUrlSlot.captured  , "imageUrl1")

    }

    @Test
    fun deleteAlbumTest(){

        val albumListSlot = slot<List<Album>>()
        val albumItemSlot = slot<Album>()

        val interactor = createAlbumsInteractor( populatedItems()).apply {
            mockkObject(this)
        }

        val presenter = AlbumsPresenter( interactor, navigator, scheduler, disposable )

        presenter.onCreate()
        presenter.onViewAttached( view )
        presenter.onResume()

        verifyOrder {
            view.hidePlaceholder()
            view.updateAlbums( capture( albumListSlot))
            view.hideLoading()
            view.showList()
        }

        val firstItem = 0

        presenter.deleteAlbum( albumListSlot.captured[ firstItem ] )

        verify {
            interactor.deleteAlbum( capture( albumItemSlot) )
        }

        assertEquals( albumListSlot.captured[firstItem].mbid, albumItemSlot.captured.mbid)

    }

    private fun createAlbumsInteractor( albums : List<Album> )
            : AlbumsContract.Interactor = object : AlbumsContract.Interactor {
        private val albumsFetchResult = PublishSubject.create<Outcome<List<Album?>>>()
        private val albumDeleteResult = PublishSubject.create<Outcome<Boolean>>()
        override fun getAlbums() {
            albumsFetchResult.loading(true)
            albumsFetchResult.success( albums )
        }
        override fun getAlbumsOutcome(): PublishSubject<Outcome<List<Album?>>> {
            return albumsFetchResult
        }
        override fun deleteAlbum(album: Album) {
        }
        override fun deleteAlbumOutcome(): PublishSubject<Outcome<Boolean>> {
            return albumDeleteResult
        }
    }

    private fun populatedItems() = listOf (
            Album().apply {
               mbid = "item1"
               url  = "imageUrl1"
            },
            Album().apply {
                mbid = "item2"
                url  = "imageUrl2"
            },
            Album().apply {
                mbid = "item3"
                url  = "imageUrl1"
            }
    )

}