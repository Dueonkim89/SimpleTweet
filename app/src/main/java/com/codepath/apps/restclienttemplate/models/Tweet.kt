package com.codepath.apps.restclienttemplate.models

import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

class Tweet {
    var body: String = ""
    var createdAt: String = ""
    var user: User? = null

    companion object {
        // returns a Tweet
        fun fromJson(jsonObject: JSONObject): Tweet {
            val tweet = Tweet()
            tweet.body = jsonObject.getString("text")
            tweet.createdAt = jsonObject.getString("created_at")
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"))
            return tweet
        }

        // returns a List of tweets using fromJson method
        fun fromJsonArray(jsonArray: JSONArray): List<Tweet>{
            val tweets = ArrayList<Tweet>()
            for (i in 0..jsonArray.length()) {
                tweets.add(fromJson(jsonArray.getJSONObject(i)))
            }
            return tweets
        }
    }
}