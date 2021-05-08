package de.rayba.dmsinputservice.setup

import android.R
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.leanback.app.GuidedStepSupportFragment

class SetupActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (null == savedInstanceState) {
            GuidedStepSupportFragment.addAsRoot(this, FirstStepFragment(), R.id.content)
        }
    }

}