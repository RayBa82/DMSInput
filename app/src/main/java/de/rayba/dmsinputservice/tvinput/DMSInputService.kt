package de.rayba.dmsinputservice.tvinput

import android.media.tv.TvInputService
import android.util.Log
import android.view.accessibility.CaptioningManager
import com.google.android.media.tv.companionlibrary.BaseTvInputService

class DMSInputService : BaseTvInputService() {

    private val TAG = DMSSession::class.java.name
    private lateinit var mCaptioningManager: CaptioningManager

    override fun onCreate() {
        super.onCreate()
        mCaptioningManager = getSystemService(CAPTIONING_SERVICE) as CaptioningManager
    }

    override fun onCreateSession(inputId: String): TvInputService.Session? {
        Log.i(TAG, "onCreateSession")
        val session = DMSSession(this, inputId)
        session.setOverlayViewEnabled(true)
        return super.sessionCreated(session)
    }


}