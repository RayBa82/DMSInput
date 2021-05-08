package de.rayba.dmsinputservice.tvinput

import android.content.Context
import android.media.PlaybackParams
import android.view.Surface
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ts.DefaultTsPayloadReaderFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.media.tv.companionlibrary.TvPlayer


class Player(val context: Context) : TvPlayer {

    private val trackSelector = DefaultTrackSelector(context)
    private var player = SimpleExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .build()

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
    }

    fun release() {
        player.release()
    }

    fun getSelectedTrack(type: Int): Int {
        return player.trackSelector(type)
    }

    fun preparePlayer(url: String) {
        val extractorFactory = DefaultExtractorsFactory()
        extractorFactory.setTsExtractorFlags(DefaultTsPayloadReaderFactory.FLAG_DETECT_ACCESS_UNITS or DefaultTsPayloadReaderFactory.FLAG_ALLOW_NON_IDR_KEYFRAMES)
        val dataSourceFactory = DefaultDataSourceFactory(context, "dmsinputservice")
        val mediaItem = MediaItem.fromUri(url)
        val mediaSource: ProgressiveMediaSource = ProgressiveMediaSource.Factory(dataSourceFactory, extractorFactory).createMediaSource(mediaItem)
        player.setMediaSource(mediaSource)
        player.prepare()
        player.play()
    }
}