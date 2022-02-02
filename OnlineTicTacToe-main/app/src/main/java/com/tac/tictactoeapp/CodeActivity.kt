package com.tac.tictactoeapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_code.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_third_page.*

var isCodeMaker = true
var code = "null"
var codeFound = false
var checkTemp = true
var keyValue: String = "null"
var joinCodeOnline: String = "null"
var CreateCodeOnline: String = "null"

class CodeActivity : AppCompatActivity() {

    private var mRewardedAd: RewardedAd? = null
    private var TAG = "MainActivity"

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_code)

        lateinit var mAdView: AdView

        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adViewOnline2)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)




        mAdView.adListener = object : AdListener() {

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(adError)
                adView.loadAd(adRequest)
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                super.onAdClicked()

            }
        }


        RewardedAd.load(
            this,
            "ca-app-pub-3887706176304341/1206659945",
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG.toString(), adError.message)
                    mRewardedAd = null

                    super.onAdFailedToLoad(adError)
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    Log.d(TAG.toString(), "Ad was loaded.")
                    mRewardedAd = rewardedAd
                    super.onAdLoaded(mRewardedAd)
                }
            })


        val alphabet: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

// Build list from 20 random samples from the alphabet,
// and convert it to a string using "" as element separator
        val randomString1: String = List(3) { alphabet.random() }.joinToString("")
        val randomString2: String = List(2) { alphabet.random() }.joinToString("-")
        val randomString3: String = List(2) { alphabet.random() }.joinToString("-")

        val UniqueCode: String = randomString1 + randomString2 + randomString3



        unique.text = "Shareable Game ID : " + UniqueCode
        val code3: String = unique.text.toString()


        val shareButton = findViewById<Button>(R.id.unique)
        shareButton.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "HEY CHECK OUT THIS GREAT APP NOW.\n\n" + code3 + "\n\nEnter " + code3 + " In Game ID Box And Join To Play.\n\nDownload App Now \n" +
                        "https://play.google.com/store/apps/details?id=com.tac.tictactoeapp\n" +
                        "\n" +
                        "Subscribe Us On YouTube \n" +
                        " https://www.youtube.com/channel/UCjQfHvT8kMuahKSOxgeCniA"
            )
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share Game ID "))
        }
// function to make the code
        Create.setOnClickListener {
            code = "null"
            codeFound = false
            checkTemp = true
            keyValue = "null"

            // receiving the gamecode here and now converting it into the string code
            code = GameCode.text.toString()
            CreateCodeOnline = code
            unique.visibility = View.GONE
            Create.visibility = View.GONE
            Join.visibility = View.GONE
            GameCode.visibility = View.GONE
            textView4.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            if (code != "null" && code != "") {

                isCodeMaker = true

                FirebaseDatabase.getInstance().reference.child("codes")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            var check = isValueAvailable(snapshot, code)
                            // here the  value of the code's key is retainged in the keyvalue variable
                            Handler().postDelayed({
                                if (check) {

                                    unique.visibility = View.VISIBLE
                                    Create.visibility = View.GONE
                                    Join.visibility = View.VISIBLE
                                    GameCode.visibility = View.VISIBLE
                                    textView4.visibility = View.VISIBLE
                                    progressBar.visibility = View.GONE

                                } else {
                                    FirebaseDatabase.getInstance().reference.child("codes").push()
                                        .setValue(code)
                                    isValueAvailable(snapshot, code)
                                    checkTemp = false
                                    Handler().postDelayed({
                                        accepted()
                                        errorMsg(" Game Started Please Don't Go Back ")
                                    }, 200)

                                }
                            }, 1000)


                        }

                    })
            } else {
                unique.visibility = View.VISIBLE
                Create.visibility = View.VISIBLE
                Join.visibility = View.VISIBLE
                GameCode.visibility = View.VISIBLE
                textView4.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                errorMsg("Please, Enter Code Properly ")
            }
        }

        // functiont to join the code
        Join.setOnClickListener {
            code = "null"
            codeFound = false
            checkTemp = true
            keyValue = "null"
            code = GameCode.text.toString()
            joinCodeOnline = code

            if (code != "null" && code != null && code != "") {
                unique.visibility = View.GONE
                Create.visibility = View.GONE
                Join.visibility = View.GONE
                GameCode.visibility = View.GONE
                textView4.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                isCodeMaker = false
                FirebaseDatabase.getInstance().reference.child("codes")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            var data: Boolean = isValueAvailable(snapshot, code)

                            Handler().postDelayed({
                                if (data == true) {
                                    codeFound = true
                                    accepted()
                                    Create.visibility = View.VISIBLE
                                    Join.visibility = View.VISIBLE
                                    GameCode.visibility = View.VISIBLE
                                    textView4.visibility = View.VISIBLE
                                    progressBar.visibility = View.GONE
                                } else {
                                    unique.visibility = View.VISIBLE
                                    Create.visibility = View.VISIBLE
                                    Join.visibility = View.VISIBLE
                                    GameCode.visibility = View.VISIBLE
                                    textView4.visibility = View.VISIBLE
                                    progressBar.visibility = View.GONE
                                    errorMsg("Invalid Code \nGenerate New or Enter Correct Code")
                                }
                            }, 2000)


                        }


                    })

            } else {
                errorMsg("Please, Enter Correct Code  ")
            }
        }

    }


    fun accepted() {
        startActivity(Intent(this, ThirdPage::class.java))
        Create.visibility = View.VISIBLE
        Join.visibility = View.VISIBLE
        GameCode.visibility = View.VISIBLE
        textView4.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

    }

    fun errorMsg(value: String) {
        Toast.makeText(this, value, Toast.LENGTH_SHORT).show()
    }

    fun isValueAvailable(snapshot: DataSnapshot, code: String): Boolean {
        var data = snapshot.children
        data.forEach {
            var value = it.value.toString()
            if (value == code) {
                keyValue = it.key.toString()
                return true
            }
        }
        return false
    }


    override fun onBackPressed() {
        if (mRewardedAd != null) {
            mRewardedAd?.show(this, OnUserEarnedRewardListener {
                fun onUserEarnedReward(rewardItem: RewardItem) {
                  //  var rewardAmount = rewardItem.getReward()
                    var rewardType = rewardItem.type
                    Log.d(TAG.toString(), "User earned the reward.")
                }
            })

            finish()
        } else {
            super.onBackPressed()
            finish()
        }


    }
}