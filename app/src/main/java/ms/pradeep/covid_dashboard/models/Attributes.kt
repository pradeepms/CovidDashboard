package ms.pradeep.covid_dashboard.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Attributes(
        val CumCase: Int,
        val CumDeath: Int,
        val OBJECTID: Int
) : Parcelable