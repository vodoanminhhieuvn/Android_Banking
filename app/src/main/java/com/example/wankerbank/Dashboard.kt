package com.example.wankerbank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_dashboard.*

class Dashboard : AppCompatActivity() {

    private val mAppUnitId: String by lazy {

        "ca-app-pub-5353080189302180~9775963223"
    }

    private val mInterstitialAdUnitId: String by lazy {

        "ca-app-pub-5353080189302180/2827411492"
    }

    private lateinit var  mInterstitialAd: InterstitialAd


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        mInterstitialAd =  InterstitialAd(this)

        initializeInterstitialAd(mAppUnitId)

        loadInterstitialAd(mInterstitialAdUnitId)

        runAdEvents()

        Balance_Text.setText(RequestApi.remainFund.toString())

        val leftAnim: Animation = AnimationUtils.loadAnimation(this, R.anim.left_animation)
        val rightAnim: Animation = AnimationUtils.loadAnimation(this, R.anim.right_animation)

        val leftAnim2: Animation = AnimationUtils.loadAnimation(this, R.anim.left_animation2)
        val rightAnim2: Animation = AnimationUtils.loadAnimation(this, R.anim.right_animation2)

        val leftAnim3: Animation = AnimationUtils.loadAnimation(this, R.anim.left_animation3)
        val rightAnim3: Animation = AnimationUtils.loadAnimation(this, R.anim.right_animation3)

        Deposit_View.startAnimation(leftAnim)
        Transfer_View.startAnimation(rightAnim)

        History_View.startAnimation(leftAnim2)
        Pending.startAnimation(rightAnim2)

        Withdrawal.startAnimation(leftAnim3)
        Online.startAnimation(rightAnim3)

        setSupportActionBar(toolbar)

        val menu = nav_view.menu

        nav_view.bringToFront()

        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawer_layout.addDrawerListener(toggle)

        toggle.syncState()

        nav_view.setCheckedItem(R.id.nav_home)
        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_transfer -> {
                    val intent = Intent(this@Dashboard, Transfer::class.java)
                    startActivity(intent)
                }

                R.id.nav_helper -> {
                    val intent = Intent(this@Dashboard, Helper::class.java)
                    startActivity(intent)
                }
            }

            drawer_layout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }

        Transfer_Btn.setOnClickListener {

            if (mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
            } else {
                Log.d("TAG", "The interstitial ad wasn't loaded yet.")
                startActivity(Intent(this@Dashboard, Transfer::class.java))
                finish()
            }
        }

        Widthdraw_Btn.setOnClickListener{
            if (mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
            } else {
                Log.d("TAG", "The interstitial ad wasn't loaded yet.")
                startActivity(Intent(this@Dashboard, WidthDraw::class.java))
                finish()
            }
        }

        Deposit_Btn.setOnClickListener {
            if (mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
            } else {
                Log.d("TAG", "The interstitial ad wasn't loaded yet.")
                startActivity(Intent(this@Dashboard,Deposit::class.java ))
                finish()
            }
        }

        History_Btn.setOnClickListener {
            val intent = Intent(this@Dashboard, History::class.java)
            startActivity(intent)
        }



    }

    private fun initializeInterstitialAd(appUnitId: String) {

        MobileAds.initialize(this, appUnitId)

    }

    private fun loadInterstitialAd(interstitialAdUnitId: String) {

        mInterstitialAd.adUnitId = interstitialAdUnitId
        mInterstitialAd.loadAd(AdRequest.Builder().build())
    }

    private fun runAdEvents() {

        mInterstitialAd.adListener = object : AdListener() {

            // If user clicks on the ad and then presses the back, s/he is directed to DetailActivity.
            override fun onAdClicked() {
                super.onAdOpened()
                mInterstitialAd.adListener.onAdClosed()
            }

            // If user closes the ad, s/he is directed to DetailActivity.
            override fun onAdClosed() {
                startActivity(Intent(this@Dashboard, Transfer::class.java))
                finish()
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }



}

