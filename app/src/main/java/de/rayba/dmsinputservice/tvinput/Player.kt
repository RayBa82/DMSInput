package de.rayba.dmsinputservice.tvinput

import android.content.Context
import android.media.PlaybackParams
import android.util.Log
import android.view.Surface
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.analytics.AnalyticsCollector
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ts.DefaultTsPayloadReaderFactory
import com.google.android.exoplayer2.extractor.ts.TsExtractor
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Clock
import com.google.android.exoplayer2.util.EventLogger
import com.google.android.media.tv.companionlibrary.TvPlayer

private const val DEFAULT_MAX_BUFFER_MS = 10000
private const val DEFAULT_BUFFER_FOR_PLAYBACK_MS = 100

class Player(val context: Context) : TvPlayer {

    private val TAG = Player::class.java.name
    private val trackParams =
        DefaultTrackSelector.ParametersBuilder(context)
            .setPreferredAudioMimeTypes("audio/vnd.dts.hd", "audio/vnd.dts", "audio/ac3")
            .build()
    private val trackSelector = DefaultTrackSelector(trackParams, AdaptiveTrackSelection.Factory())
    private val analyticsListener = EventLogger(trackSelector)
    private val loadControl = DefaultLoadControl.Builder()
        .setBufferDurationsMs(
            DEFAULT_MAX_BUFFER_MS,
            DEFAULT_MAX_BUFFER_MS,
            DEFAULT_BUFFER_FOR_PLAYBACK_MS,
            DEFAULT_BUFFER_FOR_PLAYBACK_MS
        ).build()
    private var player = initPlayer()

    override fun seekTo(position: Long) {
        Log.i(TAG, "seekTo")
        player.seekTo(position)
    }

    override fun setPlaybackParams(params: PlaybackParams?) {
        Log.i(TAG, "setPlaybackParams")
    }

    override fun getCurrentPosition(): Long {
        Log.i(TAG, "getCurrentPosition")
        return player.currentPosition
    }

    override fun getDuration(): Long {
        Log.i(TAG, "getDuration")
        return player.duration
    }

    override fun setSurface(surface: Surface?) {
        Log.i(TAG, "setSurface")
        player.setVideoSurface(surface)
    }

    override fun setVolume(volume: Float) {
        Log.i(TAG, "setVolume")
        player.volume = volume
    }

    override fun pause() {
        Log.i(TAG, "pause")
        player.pause()
    }

    override fun play() {
        Log.i(TAG, "play")
        player.play()
    }

    override fun registerCallback(callback: TvPlayer.Callback?) {
        TODO("Not yet implemented")
    }

    override fun unregisterCallback(callback: TvPlayer.Callback?) {
        TODO("Not yet implemented")
    }

    fun stop() {
        Log.i(TAG, "stop")
        player.stop()
        player.clearMediaItems()
    }

    fun release() {
        Log.i(TAG, "release")
        player.release()
    }

    fun preparePlayer(url: String) {
        Log.i(TAG, "preparePlayer")
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

    private fun initPlayer(): SimpleExoPlayer {
        Log.i(TAG, "initPlayer")
        val analyticsCollector = AnalyticsCollector(Clock.DEFAULT)
        analyticsCollector.addListener(analyticsListener)
        val audioAttributes: AudioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.CONTENT_TYPE_MOVIE)
            .build()
        return SimpleExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .setLoadControl(loadControl)
            .setAnalyticsCollector(analyticsCollector)
            .setAudioAttributes(audioAttributes, true)
            .build()
    }

}