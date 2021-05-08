package de.rayba.dmsinputservice.tvinput

import android.content.Context
import android.media.tv.TvInputManager
import android.net.Uri
import android.os.Build
import android.util.Log
import com.google.android.media.tv.companionlibrary.BaseTvInputService
import com.google.android.media.tv.companionlibrary.TvPlayer
import com.google.android.media.tv.companionlibrary.model.Program
import com.google.android.media.tv.companionlibrary.model.RecordedProgram

class DMSSession(context: Context, inputId: String) : BaseTvInputService.Session(context, inputId) {

    private val TAG = DMSSession::class.java.name
    private val player = Player(context)

    override fun onTune(channelUri: Uri): Boolean {
        notifyVideoUnavailable(TvInputManager.VIDEO_UNAVAILABLE_REASON_TUNING)
        releasePlayer()
        return super.onTune(channelUri)
    }

    override fun onSetCaptionEnabled(enabled: Boolean) {
    }

    override fun getTvPlayer(): TvPlayer {
        return player
    }

    override fun onPlayProgram(program: Program?, startPosMs: Long): Boolean {
        if (program == null) {
            notifyVideoUnavailable(TvInputManager.VIDEO_UNAVAILABLE_REASON_TUNING)
            return false
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notifyTimeShiftStatusChanged(TvInputManager.TIME_SHIFT_STATUS_AVAILABLE)
        }
        player.preparePlayer(program.internalProviderData.videoUrl)

        notifyVideoAvailable()
        return true
    }

    override fun onPlayRecordedProgram(recordedProgram: RecordedProgram?): Boolean {
        Log.i(TAG, "not implemented")
        return false
    }

    override fun onRelease() {
        super.onRelease()
        releasePlayer()
    }


    private fun releasePlayer() {
        player.setSurface(null)
        player.stop()
        player.release()
    }

}