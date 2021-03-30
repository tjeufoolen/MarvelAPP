package nl.avans.marvelapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import nl.avans.marvelapp.fragments.interfaces.IRowObject

@Parcelize
data class Comic(
    val id: Int,
    val title: String,
    val thumbnail: ComicThumbnail,
    val description: String?,
    val isbn: String?,
    val ean: String?,
    val pageCount: Int?,
) : Parcelable, IRowObject {

    @Parcelize
    data class ComicThumbnail(
        val path: String,
        val extension: String
    ) : Parcelable {
        val url: String
            get() = ("$path.$extension").replace("http://", "https://")
    }

    override fun getRowTitle(): String {
        return title
    }
}
