package eu.caraus.appsflastfm.data.ui.main.albumdetails

import eu.caraus.appsflastfm.common.bus.RxBus
import eu.caraus.appsflastfm.common.extensions.subOnIoObsOnUi
import eu.caraus.appsflastfm.common.schedulers.TestSchedulerProvider
import eu.caraus.appsflastfm.data.domain.lastFm.albuminfo.TrackItem
import eu.caraus.appsflastfm.services.youtube.busevents.client.ActionPlay
import eu.caraus.appsflastfm.services.youtube.busevents.client.ActionSeek
import eu.caraus.appsflastfm.services.youtube.busevents.client.ActionStop
import eu.caraus.appsflastfm.services.youtube.busevents.service.ElapsedUpdate
import eu.caraus.appsflastfm.services.youtube.busevents.service.PlayingUpdate
import eu.caraus.appsflastfm.services.youtube.busevents.service.StoppedUpdate
import eu.caraus.appsflastfm.services.youtube.model.youtube.YouTubeVideo
import eu.caraus.appsflastfm.ui.main.albumdetails.AlbumDetailsContract
import eu.caraus.appsflastfm.ui.main.albumdetails.AlbumDetailsPresenter
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.reactivex.plugins.RxJavaPlugins
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Test
import rx.observers.TestSubscriber

class AlbumDetailsPresenterTest {

    private val scheduler = TestSchedulerProvider()

    private var navg = mockk<AlbumDetailsContract.Navigator>()
    private var view = mockk<AlbumDetailsContract.View>( relaxed = true )
    private var intr = mockk<AlbumDetailsContract.Interactor>( relaxed = true)

    @Test
    fun playbackStartTest(){

        val testSubscriber = TestSubscriber<Any>()

        RxJavaPlugins.setErrorHandler {
            testSubscriber.onError(it)
        }

        val rxBus = RxBus()

        val track = mockk<TrackItem>()

        every { track.url } returns "https://www.last.fm/music/AC%2FDC/_/Who+Made+Who"
        every { track.id  } returns 0

        val presenter = AlbumDetailsPresenter( intr, navg, scheduler, rxBus )

        presenter.onCreate()
        presenter.onViewAttached(view)
        presenter.onResume()

        rxBus.client().subOnIoObsOnUi(scheduler)
                .subscribe({
                    assertThat( it, instanceOf( ActionPlay::class.java))
                    it as ActionPlay
                    rxBus.sendEventToClient( PlayingUpdate( it.youTubeVideo) )
                },{
                    testSubscriber.onError(it)
                })

        rxBus.service().subOnIoObsOnUi(scheduler)
                .subscribe({
                    assertThat( it, instanceOf( PlayingUpdate::class.java))
                },{
                    testSubscriber.onError(it)
                })

        // Trigger Action
        presenter.triggerPlayTrack( track )

        testSubscriber.assertNoErrors()

        var slot = slot<YouTubeVideo>()

        verify { view.updateTrackItem( capture( slot ) ) }

    }

    @Test
    fun playbackStopTest() {

        val testSubscriber = TestSubscriber<Any>()

        RxJavaPlugins.setErrorHandler {
            testSubscriber.onError(it)
        }

        val rxBus = RxBus()

        val track = mockk<TrackItem>()

        every { track.url } returns "https://www.last.fm/music/AC%2FDC/_/Who+Made+Who"
        every { track.id  } returns 0

        val presenter = AlbumDetailsPresenter( intr, navg, scheduler, rxBus )

        presenter.onCreate()
        presenter.onViewAttached(view)
        presenter.onResume()

        rxBus.client().subOnIoObsOnUi(scheduler)
                .subscribe({
                    assertThat( it, instanceOf( ActionStop::class.java))
                    it as ActionStop
                    rxBus.sendEventToClient( StoppedUpdate(YouTubeVideo()) )
                },{
                    testSubscriber.onError(it)
                })

        rxBus.service().subOnIoObsOnUi(scheduler)
                .subscribe({
                    assertThat( it, instanceOf( StoppedUpdate::class.java))
                },{
                    testSubscriber.onError(it)
                })

        presenter.triggerStopTrack( track )

        testSubscriber.assertNoErrors()

        var slot = slot<YouTubeVideo>()

        verify { view.updateTrackItem( capture( slot ) ) }

    }

    @Test
    fun playbackSeekTest() {

        val testSubscriber = TestSubscriber<Any>()

        RxJavaPlugins.setErrorHandler {
            testSubscriber.onError(it)
        }

        val rxBus = RxBus()

        val track = mockk<TrackItem>()

        every { track.url } returns "https://www.last.fm/music/AC%2FDC/_/Who+Made+Who"
        every { track.id  } returns 0

        val presenter = AlbumDetailsPresenter( intr, navg, scheduler, rxBus )

        presenter.onCreate()
        presenter.onViewAttached(view)
        presenter.onResume()

        rxBus.client().subOnIoObsOnUi(scheduler)
                .subscribe({
                    assertThat( it, instanceOf( ActionSeek::class.java))
                    rxBus.sendEventToClient( ElapsedUpdate( YouTubeVideo().apply { this.trackElapsed = 10 }) )
                },{
                    testSubscriber.onError(it)
                })

        rxBus.service().subOnIoObsOnUi(scheduler)
                .subscribe({
                    assertThat( it, instanceOf( ElapsedUpdate::class.java))
                    it as ElapsedUpdate
                    assertEquals( it.youTubeVideo.trackElapsed , 10)
                },{
                    testSubscriber.onError(it)
                })

        presenter.triggerSeekTo(10)

        testSubscriber.assertNoErrors()

        var slot = slot<YouTubeVideo>()

        verify { view.updateTrackItem( capture( slot ) ) }

    }

}