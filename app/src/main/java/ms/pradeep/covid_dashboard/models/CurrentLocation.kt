package ms.pradeep.covid_dashboard.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrentLocation(
        var latitude: Double = 0.0, var longitude: Double = 0.0,
        var fullAddress: String = "",
        var countryIsoCode: String = "",
        var countryName: String = "") : Parcelable