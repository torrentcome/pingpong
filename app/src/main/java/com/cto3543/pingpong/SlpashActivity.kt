package com.cto3543.pingpong

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View.*
import com.airbnb.lottie.LottieAnimationView

class SlpashActivity : AppCompatActivity() {

    var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slpash)
        context = this

        val P: LottieAnimationView = findViewById(R.id.P) as LottieAnimationView
        val I: LottieAnimationView = findViewById(R.id.I) as LottieAnimationView
        val O: LottieAnimationView = findViewById(R.id.O) as LottieAnimationView
        val N: LottieAnimationView = findViewById(R.id.N) as LottieAnimationView
        val G: LottieAnimationView = findViewById(R.id.G) as LottieAnimationView

        P.playAnimation()
        I.playAnimation()
        N.playAnimation()
        G.playAnimation()

        G.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                I.visibility = INVISIBLE
                O.visibility = VISIBLE

                O.playAnimation()
                N.playAnimation()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })

        O.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                startActivity(Intent(context, MainActivity::class.java))
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
    }
}
