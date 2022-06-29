package kr.ac.kpu.ce2019152012.vegan_life.SplashActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kr.ac.kpu.ce2019152012.vegan_life.Login_Join.LoginActivity
import kr.ac.kpu.ce2019152012.vegan_life.MainActivity
import kr.ac.kpu.ce2019152012.vegan_life.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }, Duration)
    }

    companion object {
        private const val Duration: Long = 2000
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}