package de.rayba.dmsinputservice.tvinput

import android.content.Context
import android.media.tv.TvInputManager
import android.media.tv.TvTrackInfo
import android.net.Uri
import android.os.Build
import android.util.Log
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.media.tv.companionlibrary.BaseTvInputService
import com.google.android.media.tv.companionlibrary.TvPlayer
import com.google.android.media.tv.companionlibrary.model.Program
import com.google.android.media.tv.companionlibrary.model.RecordedProgram

class DMSSession(context: Context, inputId: String) : BaseTvInputService.Session(context, inputId), com.google.android.exoplayer2.Player.EventListener {

    private val TAG = DMSSession::class.java.name
    private val player = Player(context, this)

    override fun onTune(channelUri: Uri): Boolean {
        Log.i(TAG, "onTune")
        notifyVideoUnavailable(TvInputManager.VIDEO_UNAVAILABLE_REASON_TUNING)
        player.stop()
        return super.onTune(channelUri)
    }

    override fun onSetCaptionEnabled(enabled: Boolean) {
        Log.i(TAG, "onSetCaptionEnabled")
    }

    override fun getTvPlayer(): TvPlayer {
        return player
    }

    override fun onPlayProgram(program: Program?, startPosMs: Long): Boolean {
        Log.i(TAG, "onPlayProgram")
        if (program == null) {
            notifyVideoUnavailable(TvInputManager.VIDEO_UNAVAILABLE_REASON_TUNING)
            return false
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notifyTimeShiftStatusChanged(TvInputManager.TIME_SHIFT_STATUS_AVAILABLE)
        }
        player.preparePlayer(program.internalProviderData.videoUrl)
        return true
    }

    override fun onPlayRecordedProgram(recordedProgram: RecordedProgram?): Boolean {
        Log.i(TAG, "onPlayRecordedProgram")
        return false
    }

    override fun onRelease() {
        Log.i(TAG, "onRelease")
        super.onRelease()
        releasePlayer()
    }


    private fun releasePlayer() {
        player.stop()
        player.release()
    }

    override fun onPlaybackStateChanged(state: Int) {
        Log.i(TAG, "onPlaybackStateChanged")
        if(state == ExoPlayer.STATE_READY) {
            player.play()
            notifyVideoAvailable()
        }
    }

    override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray ) {
    }


}