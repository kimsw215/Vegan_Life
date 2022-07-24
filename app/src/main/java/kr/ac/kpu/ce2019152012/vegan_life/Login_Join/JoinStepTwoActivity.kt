package kr.ac.kpu.ce2019152012.vegan_life.Login_Join

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.ac.kpu.ce2019152012.vegan_life.R
import kr.ac.kpu.ce2019152012.vegan_life.databinding.ActivityJoinFourSelectBinding

class JoinStepTwoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityJoinFourSelectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinFourSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}