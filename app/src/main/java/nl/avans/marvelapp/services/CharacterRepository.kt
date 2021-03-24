package nl.avans.marvelapp.services

import android.content.Context
import nl.avans.marvelapp.models.Character

class CharacterRepository(context: Context) : Repository<Character>(context, "characters") {
    override fun convert(json: String): Character {
        TODO("Not yet implemented")
    }

    override fun convertArray(json: String): Array<Character> {
        return arrayOf(Character(json))
    }
}