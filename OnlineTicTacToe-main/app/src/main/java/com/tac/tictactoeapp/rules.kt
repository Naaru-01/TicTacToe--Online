package com.tac.tictactoeapp

import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback


class rules : AppCompatActivity() {


    private var mRewardedAd: RewardedAd? = null
    private final var TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.rules)
        var adRequest = AdRequest.Builder().build()

        RewardedAd.load(this,"ca-app-pub-3887706176304341/5117984794", adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError?.message)
                mRewardedAd = null
                super.onAdFailedToLoad(adError)
            }

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                Log.d(TAG, "Ad was loaded.")
                mRewardedAd = rewardedAd
                super.onAdLoaded(mRewardedAd)
            }
        })
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
