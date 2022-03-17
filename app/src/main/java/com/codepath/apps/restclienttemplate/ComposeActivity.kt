package com.codepath.apps.restclienttemplate

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose: EditText
    lateinit var btnTweet: Button
    lateinit var charCount: TextView

    lateinit var client: TwitterClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etTweetCompose)
        btnTweet = findViewById(R.id.btnTweet)
        charCount = findViewById(R.id.charCount)
        client = TwitterApplication.getRestClient(this)

        // set event for edit text change
        etCompose.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.i(TAG, "Before text change")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.i(TAG, "Text has changed!")
            }

            override fun afterTextChanged(p0: Editable?) {
                val lengthOfTweet = etCompose.text.toString().length
                val remainingChars = 280 - lengthOfTweet

                // turn text red and disable button
                if (remainingChars < 0) {
                    charCount.setTextColor(Color.RED)
                    btnTweet.isClickable = false
                    btnTweet.isEnabled = false
                } else {
                    charCount.setTextColor(Color.BLACK)
                    btnTweet.isClickable = true
                    btnTweet.isEnabled = true
                }
                // update remaining chars
                charCount.setText(remainingChars.toString())

            }
        })

        // handle click button
        btnTweet.setOnClickListener{
            // grab content of edit text
            val tweetContent = etCompose.text.toString()

            // validation logic for empty strings
            if (tweetContent.isEmpty()) {
                Toast.makeText(this, "Empty tweet", Toast.LENGTH_SHORT).show()
            } else
            // validation logic for long tweets
                if (tweetContent.length > 280) {
                    Toast.makeText(this, "Tweet too long! Limit is 280 characters,", Toast.LENGTH_SHORT).show()
                } else {
                    // make api call to send tweet
                    client.publishTweet(tweetContent, object: JsonHttpResponseHandler (){
                        override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                            // Log.i(TAG, "Tweet was succesful")
                            // parse the JSON response
                            val tweet = Tweet.fromJson(json.jsonObject)

                            val intent = Intent()
                            intent.putExtra("tweet", tweet)
                            setResult(RESULT_OK, intent)
                            finish()

                        }

                        override fun onFailure(
                            statusCode: Int,
                            headers: Headers?,
                            response: String?,
                            throwable: Throwable?
                        ) {
                           Log.e(TAG, "Failed to publish tweet", throwable)
                        }

                    })
                }



        }

    }
    companion object {
        val TAG = "ComposeActivity"
    }
}