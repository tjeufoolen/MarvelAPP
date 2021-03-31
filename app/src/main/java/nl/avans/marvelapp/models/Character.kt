package nl.avans.marvelapp.models
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import nl.avans.marvelapp.fragments.interfaces.IRowObject

@Parcelize
data class Character(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: CharacterThumbnail,
    val urls: List<CharacterUrl>
) : Parcelable, IRowObject {

    @Parcelize
    data class CharacterThumbnail(
        val path: String,
        val extension: String
    ) : Parcelable {
        val url: String
            get() = ("$path.$extension").replace("http://", "https://")
    }

    @Parcelize
    data class CharacterUrl(
        val type: String,
        val url: String
    ) : Parcelable

    override fun getRowTitle(): String {
        return name
    }
}


