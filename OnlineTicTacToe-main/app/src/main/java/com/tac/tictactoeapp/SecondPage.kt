package com.tac.tictactoeapp
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_second_page.*
var Online = true;
class SecondPage : AppCompatActivity() {
    private var mRewardedAd: RewardedAd? = null
    private final var TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_second_page)

        lateinit var mAdView: AdView

        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView3)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)


        mAdView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                super.onAdLoaded();

            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(adError)
                adView.loadAd(adRequest)
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                super.onAdOpened()
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                super.onAdClicked()
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                super.onAdClicked()

            }
        }

        RewardedAd.load(this,"ca-app-pub-3887706176304341/2300249760", adRequest, object : RewardedAdLoadCallback() {
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




        // for online activity we will go the mode of codeactivity
        buttonOnline.setOnClickListener {
            startActivity(Intent(this , CodeActivity::class.java))
            singleUser = true;
            Online = true;
        }

        // for offline mode we will go to the intent of the main activity
        buttonOffline.setOnClickListener {

            if (mRewardedAd != null) {
                mRewardedAd?.show(this, OnUserEarnedRewardListener() {
                    fun onUserEarnedReward(rewardItem: RewardItem) {
                       // var rewardAmount = rewardItem.getReward()
                        var rewardType = rewardItem.getType()
                        Log.d(TAG.toString(), "User earned the reward.")
                    }
                })
            } else {
                Log.d(TAG.toString(), "The rewarded ad wasn't ready yet.")
            }
            startActivity(Intent(this , MainActivity::class.java))
            singleUser = false;
            Online = false;
        }
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