package nl.avans.marvelapp.services

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import nl.avans.marvelapp.R
import nl.avans.marvelapp.services.utils.VolleyRequestQueue
import org.json.JSONObject
import java.math.BigInteger
import java.security.MessageDigest

abstract class Repository<T> constructor(private val context: Context, private val genericEndPoint: String) {
    private val url: String = context.resources.getString(R.string.api_base_url)

    fun getAll(callback: (Array<T>) -> Unit) {
        executeRequest { response ->
            callback(convertArray(response))
        }
    }

    abstract fun convert(json: JSONObject): T
    abstract fun convertArray(json: JSONObject): Array<T>

    private fun executeRequest(endpoint: String = "", callback: Response.Listener<JSONObject>) {
        VolleyRequestQueue.getInstance(context).addToRequestQueue(
            JsonObjectRequest(Request.Method.GET, createRequestUrl(endpoint), null, callback,
                {
                    print("oh oh, a error occurred!")
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
}