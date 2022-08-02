package kr.ac.kpu.ce2019152012.vegan_life.Login_Join

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kr.ac.kpu.ce2019152012.vegan_life.DataVo.JoinDataVo
import kr.ac.kpu.ce2019152012.vegan_life.databinding.ActivityJoinTwoBinding

class JoinStepTwoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityJoinTwoBinding

    private var VeganType : Int = 0
    private var ActivityType : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinTwoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val myData = intent?.getParcelableExtra<JoinDataVo>("Join")
        val text = "JoinData{${myData?.profileImage}, ${myData?.nickname}, ${myData?.email}, ${myData?.password}}"


        binding.vegan.setOnClickListener {
            binding.vegan.isSelected = binding.vegan.isSelected != true
            binding.lacto.isSelected = false
            binding.obo.isSelected = false
            binding.lactoObo.isSelected = false
            binding.fesco.isSelected = false

            VeganType = 1
        }
        binding.lacto.setOnClickListener {
            binding.lacto.isSelected = binding.lacto.isSelected != true
            binding.vegan.isSelected = false
            binding.obo.isSelected = false
            binding.lactoObo.isSelected = false
            binding.fesco.isSelected = false

            VeganType = 2
        }
        binding.obo.setOnClickListener {
            binding.obo.isSelected = binding.obo.isSelected != true
            binding.vegan.isSelected = false
            binding.lacto.isSelected = false
            binding.lactoObo.isSelected = false
            binding.fesco.isSelected = false

            VeganType = 3
        }
        binding.lactoObo.setOnClickListener {
            binding.lactoObo.isSelected = binding.lactoObo.isSelected != true
            binding.vegan.isSelected = false
            binding.lacto.isSelected = false
            binding.obo.isSelected = false
            binding.fesco.isSelected = false

            VeganType = 4
        }
        binding.fesco.setOnClickListener {
            binding.fesco.isSelected = binding.fesco.isSelected != true
            binding.vegan.isSelected = false
            binding.lacto.isSelected = false
            binding.obo.isSelected = false
            binding.lactoObo.isSelected = false

            VeganType = 5
        }

        binding.activeGroup.setOnClickListener {
            when(it){
                binding.activeHigh ->
                {
                    binding.activeHigh.isSelected = binding.activeHigh.isSelected != true
                    binding.activeMiddle.isSelected = false
                    binding.activeLow.isSelected = false

                    ActivityType = 1
                }
                binding.activeMiddle ->
                {
                    binding.activeMiddle.isSelected = binding.activeMiddle.isSelected != true
                    binding.activeHigh.isSelected = false
                    binding.activeLow.isSelected = false

                    ActivityType = 2
                }
                binding.activeLow ->
                {
                    binding.activeLow.isSelected = binding.activeLow.isSelected != true
                    binding.activeHigh.isSelected = false
                    binding.activeMiddle.isSelected = false

                    ActivityType = 3
                }
                else -> {
                    binding.activeHigh.isSelected = false
                    binding.activeMiddle.isSelected = false
                    binding.activeLow.isSelected = false

                    ActivityType = 0
                }
            }
        }

        binding.nextBtn.setOnClickListener {
            if(VeganType == 0 || ActivityType == 0){
                Toast.makeText(this,"필수정보가 미기입되어있습니다.",Toast.LENGTH_SHORT).show()
            }
            else {
                Log.d("Tag",text)
                Log.d("Tag","비건 타입: ${VeganType}, 활동량 타입: ${ActivityType}" +
                        "키:${binding.joinHeight.text.toString().trim()}, 몸무게: ${binding.joinWeight.text.toString().trim()}")
            }

        }


    }
}