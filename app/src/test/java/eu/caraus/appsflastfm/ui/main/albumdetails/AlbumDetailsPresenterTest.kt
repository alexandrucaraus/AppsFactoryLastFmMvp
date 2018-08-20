package eu.caraus.appsflastfm.ui.main.albumdetails

import android.widget.ImageView
import eu.caraus.appsflastfm.common.bus.RxBus
import eu.caraus.appsflastfm.common.extensions.loading
import eu.caraus.appsflastfm.common.extensions.subOnIoObsOnUi
import eu.caraus.appsflastfm.common.extensions.success
import eu.caraus.appsflastfm.common.retrofit.Outcome
import eu.caraus.appsflastfm.common.schedulers.TestSchedulerProvider
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Album
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.TrackItem
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.Tracks
import eu.caraus.appsflastfm.services.youtube.busevents.client.ActionPlay
import eu.caraus.appsflastfm.services.youtube.busevents.client.ActionSeek
import eu.caraus.appsflastfm.services.youtube.busevents.client.ActionStop
import io.mockk.*

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.subjects.PublishSubject
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import rx.observers.TestSubscriber

class AlbumDetailsPresenterTest {

    private val scheduler = TestSchedulerProvider()
    private var disposable = CompositeDisposable()

    private var navigator = mockk<AlbumDetailsContract.Navigator>( relaxed = true)
    private var view = mockk<AlbumDetailsContract.View>( relaxed = true )

    private var rxBus = RxBus()


    @Before
    fun before(){
        val testSubscriber = TestSubscriber<Any>()
        RxJavaPlugins.setErrorHandler {
            assertEquals("isSuccess", it.localizedMessage)
            testSubscriber.onError(it)
        }
    }

    @After
    fun after(){
        disposable.dispose()
    }

    @Test
    fun showAlbumDetailsTest() {

        var slot = slot<Album>()

        var interactor = createAlbumDetailsInteractor( albumData()).apply {
            mockkObject(this)
        }

        val presenter = AlbumDetailsPresenter( interactor, navigator , scheduler, rxBus , disposable )

        presenter.onCreate() ; presenter.getAlbumInfo("item1" )
        presenter.onViewAttached(view)
        presenter.onResume()

        verify {
            view.showAlbumInfo( capture( slot ))
        }

    }

    @Test
    fun startPlayBackTest() {

        val youTubeVideoId = "PiZHNw1MtzI"

        var slot = slot<Album>()

        var interactor = createAlbumDetailsInteractor( albumData()).apply {
            mockkObject(this)
        }

        rxBus.client().subOnIoObsOnUi(scheduler).subscribe {
            when( it ){
                is ActionPlay -> {
                    assertThat( it.youTubeVideo.id, `is`( youTubeVideoId ))
                }
                else -> {
                    assertEquals("isSuccess","isFailed")
                }
            }
        }

        val presenter = AlbumDetailsPresenter( interactor, navigator , scheduler, rxBus , disposable )

        presenter.onCreate() ; presenter.getAlbumInfo("item1" )
        presenter.onViewAttached(view)
        presenter.onResume()

        verify {
            view.showAlbumInfo( capture( slot ))
        }

        presenter.triggerPlayTrack( slot.captured.tracks?.track?.get(0)!! )

    }

    @Test
    fun stopPlayBackTest() {

        var slot = slot<Album>()

        var interactor = createAlbumDetailsInteractor( albumData()).apply {
            mockkObject(this)
        }

        rxBus.client().subOnIoObsOnUi(scheduler).subscribe {
            when( it ){
                is ActionStop -> {
                    assertEquals("isSuccess","isSuccess")
                }
                else -> {
                    assertEquals("isSuccess","isFailed")
                }
            }
        }

        val presenter = AlbumDetailsPresenter( interactor, navigator , scheduler, rxBus , disposable )

        presenter.onCreate() ; presenter.getAlbumInfo("item1" )
        presenter.onViewAttached(view)
        presenter.onResume()

        verify {
            view.showAlbumInfo( capture( slot ))
        }

        presenter.triggerStopTrack( slot.captured.tracks?.track?.get(0)!! )

    }

    @Test
    fun seekPlayBackTest(){

        val seekTo = 10

        var slot = slot<Album>()

        var interactor = createAlbumDetailsInteractor( albumData()).apply {
            mockkObject(this)
        }

        rxBus.client().subOnIoObsOnUi(scheduler).subscribe {
            when( it ){
                is ActionSeek -> {
                    assertEquals( seekTo , it.seekTo )
                }
                else -> {
                    assertEquals("isSuccess","isFailed")
                }
            }
        }

        val presenter = AlbumDetailsPresenter( interactor, navigator , scheduler, rxBus , disposable )

        presenter.onCreate() ; presenter.getAlbumInfo("item1" )
        presenter.onViewAttached(view)
        presenter.onResume()

        verify {
            view.showAlbumInfo( capture( slot ))
        }

        presenter.triggerSeekTo( seekTo )

    }

    private fun createAlbumDetailsInteractor( album : Album ) : AlbumDetailsContract.Interactor {
        return object : AlbumDetailsContract.Interactor {
            private val albumInfoFetchResult = PublishSubject.create<Outcome<Album?>>()
            override fun getAlbumInfo(mbid: String) {
                albumInfoFetchResult.loading(true)
                albumInfoFetchResult.success( album )
            }
            override fun getAlbumInfoOutcome(): PublishSubject<Outcome<Album?>> {
                return  albumInfoFetchResult
            }
        }
    }

    private fun albumData() = Album().apply {
        mbid = "item1"
        artist = "artistItem1"
        name = "albumNameItem1"
        tracks = Tracks().apply {
            track = listOf(
                    TrackItem().apply {
                        id = 0L
                        mbidAlbumFk = mbid
                        name = "item1track1"
                        duration = "123"
                        url = "https://www.last.fm/music/AC%2FDC/_/Who+Made+Who"
                    },
                    TrackItem().apply {
                        id = 1L
                        mbidAlbumFk = mbid
                        name = "item1track2"
                        duration = "123"
                        url = "https://www.last.fm/music/AC%2FDC/_/Who+Made+Who"
                    })
        }
    }

}