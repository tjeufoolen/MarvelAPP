package nl.avans.marvelapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import nl.avans.marvelapp.fragments.interfaces.IRowObject

@Parcelize
data class Comic(
    val id: Int,
    val title: String
) : Parcelable, IRowObject {
    override fun getRowTitle(): String {
        return title
    }
}
