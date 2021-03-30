package nl.avans.marvelapp.repositories

import android.content.Context
import com.beust.klaxon.Klaxon
import nl.avans.marvelapp.models.Comic
import org.json.JSONObject

class ComicRepository(context: Context) : Repository<Comic>(context, "comics") {
    override fun convert(json: JSONObject): Comic? {
        val data = convertArray(json)
        return if (data != null && data.isNotEmpty()) data[0] else null
    }

    override fun convertArray(json: JSONObject): List<Comic>? {
        val jsonArray = json.getJSONObject("data").getJSONArray("results")
        return Klaxon().parseArray(jsonArray.toString())
    }
}