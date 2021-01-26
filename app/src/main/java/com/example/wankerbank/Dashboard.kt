package com.example.wankerbank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.activity_dashboard.*

class Dashboard : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

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
            }

            drawer_layout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }

        Transfer_Btn.setOnClickListener {
            val intent = Intent(this@Dashboard, Transfer::class.java)
            startActivity(intent)
        }

        Widthdraw_Btn.setOnClickListener{
            val intent = Intent(this@Dashboard, WidthDraw::class.java)
            startActivity(intent)
        }

        Deposit_Btn.setOnClickListener {
            val intent = Intent(this@Dashboard,Deposit::class.java )
            startActivity(intent)
        }

        History_Btn.setOnClickListener {
            val intent = Intent(this@Dashboard, History::class.java)
            startActivity(intent)
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

