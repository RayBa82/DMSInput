package de.rayba.dmsinputservice.setup

import android.app.Activity
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.leanback.app.GuidedStepSupportFragment
import androidx.leanback.widget.GuidanceStylist
import androidx.leanback.widget.GuidedAction
import de.rayba.dmsinputservice.R


class FirstStepFragment : GuidedStepSupportFragment() {

    @NonNull
    override fun onCreateGuidance(@NonNull savedInstanceState: Bundle?): GuidanceStylist.Guidance {
        val title = getString(R.string.input_label)
        val description = getString(R.string.setup_first_step_description)
        val icon = activity!!.getDrawable(R.drawable.app_icon_your_company)
        return GuidanceStylist.Guidance(title, description, null, icon)
    }

    override fun onCreateActions(
        @NonNull actions: MutableList<GuidedAction?>,
        savedInstanceState: Bundle?
    ) {
        actions.add(
            GuidedAction.Builder(context)
                .id(GuidedAction.ACTION_ID_NEXT)
                .title(R.string.setup_add_channel)
                .hasNext(true)
                .build()
        )
        actions.add(
            GuidedAction.Builder(context)
                .id(GuidedAction.ACTION_ID_CANCEL)
                .title(R.string.setup_cancel)
                .build()
        )
    }

    override fun onGuidedActionClicked(action: GuidedAction) {
        if (action.id == GuidedAction.ACTION_ID_NEXT) {
            add(fragmentManager, SecondStepFragment())
        } else if (action.id === GuidedAction.ACTION_ID_CANCEL) {
            activity!!.setResult(Activity.RESULT_CANCELED)
            activity!!.finishAfterTransition()
        }
    }

}