package nl.avans.marvelapp.repositories

import android.content.Context
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import nl.avans.marvelapp.R
import nl.avans.marvelapp.repositories.utils.CustomJsonObjectRequest
import nl.avans.marvelapp.repositories.utils.VolleyRequestQueue
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

    fun getPaginated(itemOffset: Int, callback: (List<T>?) -> Unit) {
        executeRequest("", mapOf("offset" to itemOffset.toString())) { response ->
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
    private fun executeRequest(endpoint: String = "", queryParams: Map<String, String>? = null, callback: Response.Listener<JSONObject>) {
        VolleyRequestQueue.getInstance(context).addToRequestQueue(
            CustomJsonObjectRequest(Request.Method.GET, createRequestUrl(endpoint, queryParams), null, callback,
                {
                    // Frontend should ignore the fact that a request can go wrong and handle this
                    // already in its own way. However, we keep logging the error so that we as
                    // developers, have an easier time debugging a problem.
                    Log.d("Error", it.message.toString())
                }
            ).setRetryPolicy(DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        )
    }

    private fun createRequestUrl(endpoint: String, queryParams: Map<String, String>? = null): String {
        // Build url
        var requestUrl = "$url/$genericEndPoint$endpoint"

        // Create map of all query params
        val allQueryParams: MutableMap<String, String> = mutableMapOf()
        if (queryParams != null) {
            allQueryParams.putAll(queryParams)
        }

        // Append authentication to query params
        allQueryParams.putAll(generateAuthenticationHeaders())

        // Append query string to the request url
        requestUrl += appendQueryString(allQueryParams)
        return requestUrl
    }

    private fun stringToMD5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    private fun appendQueryString(input: Map<String, String>): String {
        if (input.isEmpty()) return ""

        var returnString = "?"
        val stringBuilder = StringBuilder()
        for ((key, value) in input) {
            if (stringBuilder.isNotEmpty()) {
                stringBuilder.append("&")
            }
            stringBuilder.append("$key=$value")
        }
        returnString += stringBuilder.toString()

        return returnString
    }

    private fun generateAuthenticationHeaders(): Map<String, String> {
        val publicKey = context.resources.getString(R.string.api_public_key)
        val privateKey = context.resources.getString(R.string.api_private_key)
        val timestamp = 1
        val hash = stringToMD5("$timestamp$privateKey$publicKey")

        return mapOf("apikey" to "$publicKey&ts=$timestamp&hash=$hash")
    }
    // endregion Helpers
}