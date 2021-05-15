package de.rayba.dmsinputservice.tvinput

import android.content.Context
import android.media.PlaybackParams
import android.view.Surface
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.analytics.AnalyticsCollector
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ts.DefaultTsPayloadReaderFactory
import com.google.android.exoplayer2.extractor.ts.TsExtractor
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Clock
import com.google.android.exoplayer2.util.EventLogger
import com.google.android.media.tv.companionlibrary.TvPlayer

private const val DEFAULT_MAX_BUFFER_MS = 10000
private const val DEFAULT_BUFFER_FOR_PLAYBACK_MS = 100

class Player(val context: Context) : TvPlayer {

    private val trackSelector = DefaultTrackSelector(context)
    private val analyticsListener = EventLogger(trackSelector)
    private val loadControl = DefaultLoadControl.Builder()
        .setBufferDurationsMs(DEFAULT_MAX_BUFFER_MS,
            DEFAULT_MAX_BUFFER_MS,
            DEFAULT_BUFFER_FOR_PLAYBACK_MS,
            DEFAULT_BUFFER_FOR_PLAYBACK_MS
        ).build()
    private val player = initPlayer()

    override fun seekTo(position: Long) {
        player.seekTo(position)
    }

    override fun setPlaybackParams(params: PlaybackParams?) {
        TODO("Not yet implemented")
    }

    override fun getCurrentPosition(): Long {
        return player.currentPosition
    }

    override fun getDuration(): Long {
        return player.duration
    }

    override fun setSurface(surface: Surface?) {
        player.setVideoSurface(surface)
    }

    override fun setVolume(volume: Float) {
        player.volume = volume
    }

    override fun pause() {
        player.playWhenReady = false
    }

    override fun play() {
        player.playWhenReady = true
    }

    override fun registerCallback(callback: TvPlayer.Callback?) {
        TODO("Not yet implemented")
    }

    override fun unregisterCallback(callback: TvPlayer.Callback?) {
        TODO("Not yet implemented")
    }

    fun stop() {
        player.stop()
        player.clearMediaItems()
    }

    fun release() {
        player.release()
    }

    fun preparePlayer(url: String) {
        val extractorFactory = DefaultExtractorsFactory()
        extractorFactory.setTsExtractorFlags(DefaultTsPayloadReaderFactory.FLAG_DETECT_ACCESS_UNITS or DefaultTsPayloadReaderFactory.FLAG_ALLOW_NON_IDR_KEYFRAMES)
        extractorFactory.setTsExtractorMode(TsExtractor.MODE_MULTI_PMT)
        val dataSourceFactory = DefaultDataSourceFactory(context, "dmsinputservice")
        val mediaItem = MediaItem.fromUri(url)
        val mediaSource: ProgressiveMediaSource =
            ProgressiveMediaSource.Factory(dataSourceFactory, extractorFactory)
                .createMediaSource(mediaItem)
        player.setMediaSource(mediaSource)
        player.prepare()
        player.play()
    }

    fun initPlayer(): SimpleExoPlayer {
        val analyticsCollector = AnalyticsCollector(Clock.DEFAULT)
        analyticsCollector.addListener(analyticsListener)
        return SimpleExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .setLoadControl(loadControl)
            .setAnalyticsCollector(analyticsCollector)
            .build()
    }

}