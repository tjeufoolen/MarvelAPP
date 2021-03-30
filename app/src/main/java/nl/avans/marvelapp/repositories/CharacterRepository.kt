package nl.avans.marvelapp.repositories

import android.content.Context
import com.beust.klaxon.Klaxon
import nl.avans.marvelapp.models.Character
import org.json.JSONObject

class CharacterRepository(context: Context) : Repository<Character>(context, "characters") {
    override fun convert(json: JSONObject): Character? {
        val data = convertArray(json)
        return if (data != null && data.isNotEmpty()) data[0] else null
    }

    override fun convertArray(json: JSONObject): List<Character>? {
        val jsonArray = json.getJSONObject("data").getJSONArray("results")
        return Klaxon().parseArray(jsonArray.toString())
    }
}