package de.rayba.dmsinputservice.tvinput

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.media.tv.companionlibrary.BaseTvInputService
import com.google.android.media.tv.companionlibrary.model.Channel
import com.google.android.media.tv.companionlibrary.model.Program

@RequiresApi(Build.VERSION_CODES.N)
class DMSRecordingSession(context: Context?, inputId: String?) : BaseTvInputService.RecordingSession(context, inputId) {

    override fun onStopRecording(programToRecord: Program?) {
        TODO("Not yet implemented")
    }

    override fun onRelease() {
        TODO("Not yet implemented")
    }

    override fun onStopRecordingChannel(channelToRecord: Channel?) {
        TODO("Not yet implemented")
    }

}