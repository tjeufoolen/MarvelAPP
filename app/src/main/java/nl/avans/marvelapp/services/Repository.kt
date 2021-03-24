package nl.avans.marvelapp.services

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.beust.klaxon.Klaxon
import nl.avans.marvelapp.R
import nl.avans.marvelapp.models.Character
import nl.avans.marvelapp.services.utils.VolleyRequestQueue
import org.json.JSONObject
import java.math.BigInteger
import java.security.MessageDigest

abstract class Repository<T> constructor(private val context: Context, private val genericEndPoint: String) {
    private val url: String = context.resources.getString(R.string.api_base_url)

    fun getAll(callback: (List<T>?) -> Unit) {
        executeRequest { response ->
            callback(convertArray(response))
        }
    }

    fun getById(id: Int, callback: (T?) -> Unit) {
        executeRequest("/$id") { response ->
            callback(convert(response))
        }
    }

    protected abstract fun convert(json: JSONObject): T?
    protected abstract fun convertArray(json: JSONObject): List<T>?

    // region Helpers
    private fun executeRequest(endpoint: String = "", callback: Response.Listener<JSONObject>) {
        VolleyRequestQueue.getInstance(context).addToRequestQueue(
            JsonObjectRequest(Request.Method.GET, createRequestUrl(endpoint), null, callback,
                {
                    Log.d(Log.ERROR.toString(), it.message.toString())
                }
            )
        )
    }

    private fun createRequestUrl(endpoint: String) : String {
        // Build url
        var requestUrl = "$url/$genericEndPoint$endpoint"

        // Append authentication
        val publicKey = context.resources.getString(R.string.api_public_key)
        val privateKey = context.resources.getString(R.string.api_private_key)
        val timestamp = 1
        val hash = stringToMD5("$timestamp$privateKey$publicKey")

        requestUrl += "?apikey=$publicKey&ts=$timestamp&hash=$hash"

        // Return full url
        return requestUrl
    }

    private fun stringToMD5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }
    // endregion Helpers
}