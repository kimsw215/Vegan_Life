package kr.ac.kpu.ce2019152012.vegan_life

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kakao.sdk.common.util.Utility

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val keyHash = Utility.getKeyHash(this)//onCreate 안에 입력해주자
        Log.d("Hash", keyHash)
    }
}