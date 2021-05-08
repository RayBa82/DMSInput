package de.rayba.dmsinputservice.setup

import android.os.Bundle
import androidx.annotation.NonNull
import androidx.leanback.widget.GuidanceStylist
import com.google.android.media.tv.companionlibrary.setup.ChannelSetupStepSupportFragment
import de.rayba.dmsinputservice.R
import de.rayba.dmsinputservice.tvinput.DMSEpgSyncJobService


class SecondStepFragment : ChannelSetupStepSupportFragment<DMSEpgSyncJobService>() {


    override fun getEpgSyncJobServiceClass(): Class<DMSEpgSyncJobService> {
        return DMSEpgSyncJobService::class.java
    }

    override fun onCreateGuidance(@NonNull savedInstanceState: Bundle?): GuidanceStylist.Guidance {
        val title = getString(R.string.input_label)
        val description = getString(R.string.tif_channel_setup_description)
        val icon = activity!!.getDrawable(R.drawable.app_icon_your_company)
        return GuidanceStylist.Guidance(title, description, null, icon)
    }

    override fun onScanError(errorCode: Int) {
        super.onScanError(errorCode)
    }

    override fun onScanFinished() {
        super.onScanFinished()
    }


}