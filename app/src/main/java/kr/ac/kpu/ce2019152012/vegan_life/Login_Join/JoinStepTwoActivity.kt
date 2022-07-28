package kr.ac.kpu.ce2019152012.vegan_life.Login_Join

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kr.ac.kpu.ce2019152012.vegan_life.DataVo.JoinDataVo
import kr.ac.kpu.ce2019152012.vegan_life.databinding.ActivityJoinTwoBinding

class JoinStepTwoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityJoinTwoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinTwoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val myData = intent?.getParcelableExtra<JoinDataVo>("Join")
        val text = "JoinData{${myData?.profileImage}, ${myData?.nickname}, ${myData?.email}, ${myData?.password}}"

        Log.d("Tag",text)

    }
}