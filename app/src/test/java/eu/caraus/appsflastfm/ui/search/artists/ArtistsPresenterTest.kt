package eu.caraus.appsflastfm.ui.search.artists

import eu.caraus.appsflastfm.common.extensions.loading
import eu.caraus.appsflastfm.common.extensions.success
import eu.caraus.appsflastfm.common.retrofit.Outcome
import eu.caraus.appsflastfm.common.schedulers.TestSchedulerProvider
import eu.caraus.appsflastfm.data.domain.lastFm.artists.ArtistItem
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verifyOrder
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import rx.observers.TestSubscriber

class ArtistsPresenterTest {


    private val scheduler = TestSchedulerProvider()

    private var navigator = mockk<ArtistsContract.Navigator>( relaxed = true)

    private var view = mockk<ArtistsContract.View>( relaxed = true )

    private val disposable = CompositeDisposable()

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
    fun showArtistTestPopulatedData() {

        val slot = slot<List<ArtistItem?>>()

        val presenter = ArtistsPresenter( createArtistInteractor( artistList()), navigator , scheduler )

        presenter.onCreate() ; presenter.searchArtist("artist" )
        presenter.onViewAttached( view )
        presenter.onResume()

        verifyOrder {
            view.hidePlaceholder()
            view.showFoundArtists( capture( slot))
            view.hideLoading()
            view.showList()
        }

    }

    private fun createArtistInteractor( artists : List<ArtistItem?> )
            : ArtistsContract.Interactor = object : ArtistsContract.Interactor {

        private val artistFetchResult = PublishSubject.create<Outcome<List<ArtistItem?>>>()


        override fun getArtists(artistName: String) {
            artistFetchResult.loading(true)
            artistFetchResult.success(artists)
        }

        override fun getArtistsOutcome(): PublishSubject<Outcome<List<ArtistItem?>>>
                = artistFetchResult

    }

    private fun artistList() = listOf (
            ArtistItem().apply {
                mbid = "item1"
                url  = "imageUrl1"
            },
            ArtistItem().apply {
                mbid = "item2"
                url  = "imageUrl2"
            },
            ArtistItem().apply {
                mbid = "item3"
                url  = "imageUrl1"
            }
    )


}