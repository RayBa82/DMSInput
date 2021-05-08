package de.rayba.dmsinputservice.tvinput

import android.media.tv.TvInputService
import android.view.accessibility.CaptioningManager
import com.google.android.media.tv.companionlibrary.BaseTvInputService

class DMSInputService : BaseTvInputService() {

    private lateinit var mCaptioningManager: CaptioningManager

    override fun onCreate() {
        super.onCreate()
        mCaptioningManager = getSystemService(CAPTIONING_SERVICE) as CaptioningManager
    }

    override fun onCreateSession(inputId: String): TvInputService.Session? {
        val session = DMSSession(this, inputId)
        session.setOverlayViewEnabled(true)
        return super.sessionCreated(session)
    }


}