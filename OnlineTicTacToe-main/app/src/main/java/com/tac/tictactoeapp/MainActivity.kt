package com.tac.tictactoeapp

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.android.synthetic.main.activity_main.*
import java.util.prefs.AbstractPreferences
import kotlin.collections.ArrayList
import kotlin.system.exitProcess

var playerTurn = true

class MainActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_main)



        button10.setOnClickListener {
            reset()

        }



        // Step 4 : Google Admob Banner add


        // main Screen ad
        lateinit var mAdView: AdView

        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView)
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

        RewardedAd.load(this,"ca-app-pub-3887706176304341/5309556483", adRequest, object : RewardedAdLoadCallback() {
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


    var player1Count = 0
    var player2Count = 0


    fun clickfun(view: View) {
        if (playerTurn) {
            val but = view as Button
            var cellID = 0
            //Toast.makeText(this,but.id.toString() , Toast.LENGTH_SHORT).show();
            when (but.id) {
                R.id.button -> cellID = 1
                R.id.button2 -> cellID = 2
                R.id.button3 -> cellID = 3
                R.id.button4 -> cellID = 4
                R.id.button5 -> cellID = 5
                R.id.button6 -> cellID = 6
                R.id.button7 -> cellID = 7
                R.id.button8 -> cellID = 8
                R.id.button9 -> cellID = 9

            }
            playerTurn = false;
            Handler().postDelayed(Runnable { playerTurn = true }, 600)
            playnow(but, cellID)

        }
    }

    var player1 = ArrayList<Int>()
    var player2 = ArrayList<Int>()

    var emptyCells = ArrayList<Int>()
    var activeUser = 1
    fun playnow(buttonSelected: Button, currCell: Int) {
        val audio = MediaPlayer.create(this, R.raw.poutch)
        if (activeUser == 1) {
            buttonSelected.text = "X"
            buttonSelected.setTextColor(Color.parseColor("#EC0C0C"))
            player1.add(currCell)
            emptyCells.add(currCell)
            audio.start()
            //Handler().postDelayed(Runnable { audio.pause() } , 500)
            buttonSelected.isEnabled = false
            Handler().postDelayed(Runnable { audio.release() }, 200)
            val checkWinner = checkwinner()
            if (checkWinner == 1) {
                Handler().postDelayed(Runnable { reset() }, 2000)
            } else if (singleUser) {
                Handler().postDelayed(Runnable { robot() }, 500)
                Toast.makeText(this, " Opponent's Move", Toast.LENGTH_SHORT).show()
            } else
                activeUser = 2

        } else {
            buttonSelected.text = "O"
            audio.start()
            buttonSelected.setTextColor(Color.parseColor("#D22BB804"))
            //Handler().postDelayed(Runnable { audio.pause() } , 500)
            activeUser = 1
            player2.add(currCell)
            emptyCells.add(currCell)
            Handler().postDelayed(Runnable { audio.release() }, 200)
            buttonSelected.isEnabled = false
            val checkWinner = checkwinner()
            if (checkWinner == 1)
                Handler().postDelayed(Runnable { reset() }, 4000)
        }

    }


    fun checkwinner(): Int {
        val audio = MediaPlayer.create(this, R.raw.success)
        val audioMatchWin = MediaPlayer.create(this,R.raw.success1)

        if ((player1.contains(1) && player1.contains(2) && player1.contains(3)) || (player1.contains(
                1
            ) && player1.contains(4) && player1.contains(7)) ||
            (player1.contains(3) && player1.contains(6) && player1.contains(9)) || (player1.contains(
                7
            ) && player1.contains(8) && player1.contains(9)) ||
            (player1.contains(4) && player1.contains(5) && player1.contains(6)) || (player1.contains(
                1
            ) && player1.contains(5) && player1.contains(9)) ||
            player1.contains(3) && player1.contains(5) && player1.contains(7) || (player1.contains(2) && player1.contains(
                5
            ) && player1.contains(8))
        ) {




           player1Count+=10


            buttonDisable()
            audio.start()
            disableReset()
            Handler().postDelayed(Runnable { audio.release() }, 4000)
            val build = AlertDialog.Builder(this)

            if (player1Count >= 100) {

                buttonDisable()
                audioMatchWin.start()
                disableReset()

                build.setTitle("  !  YOU WON BY  $player1Count - $player2Count  !")
                player1Count = 0
                player2Count = 0


            } else {


                build.setTitle("            !     YOU WON        !")
            }
            build.setPositiveButton("CONTINUE") { dialog, which ->
                reset()
                audioMatchWin.release()
            }
            build.setNegativeButton("GO HOME") { dialog, which ->
                audio.release()
                exitProcess(1)

            }
            Handler().postDelayed(Runnable { build.show() }, 2000)
            return 1


        } else if ((player2.contains(1) && player2.contains(2) && player2.contains(3)) || (player2.contains(
                1
            ) && player2.contains(4) && player2.contains(7)) ||
            (player2.contains(3) && player2.contains(6) && player2.contains(9)) || (player2.contains(
                7
            ) && player2.contains(8) && player2.contains(9)) ||
            (player2.contains(4) && player2.contains(5) && player2.contains(6)) || (player2.contains(
                1
            ) && player2.contains(5) && player2.contains(9)) ||
            player2.contains(3) && player2.contains(5) && player2.contains(7) || (player2.contains(2) && player2.contains(
                5
            ) && player2.contains(8))
        ) {


           player2Count+=10

            audio.start()
            buttonDisable()
            disableReset()
            Handler().postDelayed(Runnable { audio.release() }, 4000)
            val build = AlertDialog.Builder(this)

            if (player2Count >= 100) {

                buttonDisable()
                audioMatchWin.start()
                disableReset()

                Handler().postDelayed(Runnable { audio.release() }, 4000)
                build.setTitle("OPPONENT WON  BY $player2Count - $player1Count   \n RESET")

                player1Count = 0
                player2Count = 0
            } else {


                build.setTitle("            !     OPPONENT WON        !")
            }

            build.setPositiveButton("CONTINUE") { dialog, which ->
                reset()
                audioMatchWin.release()
            }
            build.setNegativeButton("GO HOME") { dialog, which ->
                audio.release()
                exitProcess(1)
            }
            Handler().postDelayed(Runnable { build.show() }, 2000)
            return 1
        } else if (emptyCells.contains(1) && emptyCells.contains(2) && emptyCells.contains(3) && emptyCells.contains(
                4
            ) && emptyCells.contains(5) && emptyCells.contains(6) && emptyCells.contains(7) &&
            emptyCells.contains(8) && emptyCells.contains(9)
        ) {

            player1Count += 5
            player2Count += 5

            val build = AlertDialog.Builder(this)
            if (player2Count == 100 && player1Count == 100 && player1Count == player2Count) {
                build.setTitle("DRAWN ")
            } else {


                build.setTitle("   DRAWN  !")
            }

            build.setPositiveButton(" CONTINUE ") { dialog, which ->
//                reset()
//                player1Count=0
//                player2Count=0
            }
            build.setNegativeButton(" GO HOME ") { dialog, which ->
                exitProcess(1)
            }
            build.show()
            return 1

        }
        return 0
    }

    fun reset() {
        player1.clear()
        player2.clear()
        emptyCells.clear()
        activeUser = 1;
        for (i in 1..9) {
            var buttonselected: Button?
            buttonselected = when (i) {
                1 -> button
                2 -> button2
                3 -> button3
                4 -> button4
                5 -> button5
                6 -> button6
                7 -> button7
                8 -> button8
                9 -> button9
                else -> {
                    button
                }
            }
            buttonselected.isEnabled = true
            buttonselected.text = ""
            textView.text = "YOU : $player1Count"
            textView2.text = "OPPONENT : $player2Count"
        }
    }

    fun robot() {
        val rnd = (1..9).random()
        if (emptyCells.contains(rnd))
            robot()
        else {
            val buttonselected: Button?
            buttonselected = when (rnd) {
                1 -> button
                2 -> button2
                3 -> button3
                4 -> button4
                5 -> button5
                6 -> button6
                7 -> button7
                8 -> button8
                9 -> button9
                else -> {
                    button
                }
            }
            emptyCells.add(rnd);
            val audio = MediaPlayer.create(this, R.raw.poutch)
            audio.start()
            Handler().postDelayed(Runnable { audio.release() }, 500)
            buttonselected.text = "O"
            buttonselected.setTextColor(Color.parseColor("#D22BB804"))
            player2.add(rnd)
            buttonselected.isEnabled = false

            var checkWinner = checkwinner()
            if (checkWinner == 1)
                Handler().postDelayed(Runnable { reset() }, 2000)

        }
    }

    fun buttonDisable() {
        for (i in 1..9) {
            val buttonSelected = when (i) {
                1 -> button
                2 -> button2
                3 -> button3
                4 -> button4
                5 -> button5
                6 -> button6
                7 -> button7
                8 -> button8
                9 -> button9
                else -> {
                    button
                }

            }
            if (buttonSelected.isEnabled == true)
                buttonSelected.isEnabled = false
        }
    }


    fun disableReset() {
        button10.isEnabled = false
        Handler().postDelayed(Runnable { button10.isEnabled = true }, 2200)
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
