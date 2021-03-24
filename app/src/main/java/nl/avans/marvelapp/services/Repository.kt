package nl.avans.marvelapp.services

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import nl.avans.marvelapp.services.utils.VolleyRequestQueue
import java.math.BigInteger
import java.security.MessageDigest

abstract class Repository<T> constructor(private val context: Context, private val genericEndPoint: String) {
    private val url: String = "https://gateway.marvel.com/v1/public"

    fun getAll(callback: (Array<T>) -> Unit) {
        executeRequest { response ->
            callback(convertArray(response))
        }
    }

    abstract fun convert(json: String): T
    abstract fun convertArray(json: String): Array<T>

    private fun executeRequest(endpoint: String = "", callback: Response.Listener<String>) {
        VolleyRequestQueue.getInstance(context).addToRequestQueue(
            StringRequest(Request.Method.GET, createRequestUrl(endpoint), callback,
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
        val publicKey = "f4e95a9f23f3da74276017816fe6cb8a"
        val privateKey = "a6250f1ff7da337c2c587cc3db149aabe071560b"
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