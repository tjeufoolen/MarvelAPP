package nl.avans.marvelapp.services

import android.content.Context
import nl.avans.marvelapp.models.Character
import org.json.JSONObject

class CharacterRepository(context: Context) : Repository<Character>(context, "characters") {
    override fun convert(json: JSONObject): Character {
        TODO("Not yet implemented")
    }

    override fun convertArray(json: JSONObject): Array<Character> {
        val array = json.getJSONObject("data").getJSONArray("results")



        return arrayOf(Character(array.toString()))
    }
}