package kr.ac.kpu.ce2019152012.vegan_life.Login_Join

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.ac.kpu.ce2019152012.vegan_life.databinding.ActivityJoinCompleteBinding

class JoinCompleteActivity : AppCompatActivity() {
    private lateinit var binding : ActivityJoinCompleteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}