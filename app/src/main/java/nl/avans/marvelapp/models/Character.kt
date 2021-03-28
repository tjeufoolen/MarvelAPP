package nl.avans.marvelapp.models

data class Character(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: CharacterThumbnail,
    val urls: List<CharacterUrl>
) {
    data class CharacterThumbnail(
        val path: String,
        val extension: String
    ) {
        val url: String
            get() = "$path.$extension"
    }

    data class CharacterUrl(
        val type: String,
        val url: String
    )
}


