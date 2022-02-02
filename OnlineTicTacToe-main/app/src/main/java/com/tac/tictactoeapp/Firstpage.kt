package com.tac.tictactoeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.android.synthetic.main.activity_firstpage.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess

var singleUser = false

class Firstpage : AppCompatActivity() {

    private var mRewardedAd: RewardedAd? = null
    private var TAG = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_firstpage)


        lateinit var mAdView: AdView

        MobileAds.initialize(this) {}

        mAdView = findViewById(
            R.id.adViewPage
        )
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
            "ca-app-pub-3887706176304341/9248801499",
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG.toString(), adError.message)

                    super.onAdFailedToLoad(adError)


                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    Log.d(TAG, "Ad was loaded.")
                    mRewardedAd = rewardedAd
                    super.onAdLoaded(mRewardedAd)

                }
            })




        rulesforGame.setOnClickListener {
            startActivity(Intent(this, rules::class.java))
            singleUser = true

            if (mRewardedAd != null) {
                mRewardedAd?.show(this, OnUserEarnedRewardListener {
                    fun onUserEarnedReward(rewardItem: RewardItem) {
                        //var rewardAmount = rewardItem.getReward()
                        var rewardType = rewardItem.type
                        Log.d(TAG.toString(), "User earned the reward.")
                    }
                })
            } else {
                Log.d(TAG.toString(), "The rewarded ad wasn't ready yet.")
            }
        }

        button11.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            singleUser = true

            if (mRewardedAd != null) {
                mRewardedAd?.show(this, OnUserEarnedRewardListener {
                    fun onUserEarnedReward(rewardItem: RewardItem) {
                        //var rewardAmount = rewardItem.getReward()
                        var rewardType = rewardItem.type
                        Log.d(TAG.toString(), "User earned the reward.")
                    }
                })
            } else {
                Log.d(TAG.toString(), "The rewarded ad wasn't ready yet.")
            }
        }
        button12.setOnClickListener {
            startActivity(Intent(this, SecondPage::class.java))
            singleUser = false

            if (mRewardedAd != null) {
                mRewardedAd?.show(this, OnUserEarnedRewardListener {
                    fun onUserEarnedReward(rewardItem: RewardItem) {
                        //var rewardAmount = rewardItem.getReward()
                        var rewardType = rewardItem.type
                        Log.d(TAG, "User earned the reward.")
                    }
                })
            } else {
                Log.d(TAG.toString(), "The rewarded ad wasn't ready yet.")
            }
        }


    }

    override fun onBackPressed() {

        if (mRewardedAd != null) {
            mRewardedAd?.show(this, OnUserEarnedRewardListener {
                fun onUserEarnedReward(rewardItem: RewardItem) {
                    //var rewardAmount = rewardItem.getReward()
                    var rewardType = rewardItem.type
                    Log.d(TAG, "User earned the reward.")
                }
            })
        } else {
            Log.d(TAG.toString(), "The rewarded ad wasn't ready yet.")
        }

        ActivityCompat.finishAffinity(this)
        exitProcess(1)
    }
}
