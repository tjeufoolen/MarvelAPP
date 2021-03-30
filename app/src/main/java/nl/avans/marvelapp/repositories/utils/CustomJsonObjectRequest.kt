package nl.avans.marvelapp.repositories.utils

import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class CustomJsonObjectRequest(method: Int, url: String?, jsonRequest: JSONObject?, listener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener?)
: JsonObjectRequest(method, url, jsonRequest, listener, errorListener) {

    override fun deliverError(error: VolleyError) {
        if (error is NoConnectionError) {
            val entry: Cache.Entry? = this.cacheEntry
            if (entry != null) {
                val response: Response<JSONObject>? = parseNetworkResponse(NetworkResponse(entry.data))
                if (response != null) {
                    deliverResponse(response.result)
                }
                return
            }
        }
        super.deliverError(error)
    }
}