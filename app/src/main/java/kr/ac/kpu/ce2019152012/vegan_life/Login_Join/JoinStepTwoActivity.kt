package kr.ac.kpu.ce2019152012.vegan_life.Login_Join

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import kr.ac.kpu.ce2019152012.vegan_life.DataVo.JoinDataVo
import kr.ac.kpu.ce2019152012.vegan_life.R
import kr.ac.kpu.ce2019152012.vegan_life.databinding.ActivityJoinTwoBinding

class JoinStepTwoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJoinTwoBinding

    var VeganType: Int = 0
    var ActivityType: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinTwoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myData = intent?.getParcelableExtra<JoinDataVo>("Join")
        val text =
            "JoinData{${myData?.profileImage}, ${myData?.nickname}, ${myData?.email}, ${myData?.password}}"


/*        binding.VeganType.setOnClickListener {
            when (it.id) {
                R.id.vegan -> {
                    binding.vegan.isSelected = binding.vegan.isSelected != true
                    binding.lacto.isSelected = false
                    binding.obo.isSelected = false
                    binding.lactoObo.isSelected = false
                    binding.fesco.isSelected = false

                    VeganType = 1
                }

                R.id.lacto -> {
                    binding.lacto.isSelected = binding.lacto.isSelected != true
                    binding.vegan.isSelected = false
                    binding.obo.isSelected = false
                    binding.lactoObo.isSelected = false
                    binding.fesco.isSelected = false

                    VeganType = 2
                }

                R.id.obo -> {
                    binding.obo.isSelected = binding.obo.isSelected != true
                    binding.vegan.isSelected = false
                    binding.lacto.isSelected = false
                    binding.lactoObo.isSelected = false
                    binding.fesco.isSelected = false

                    VeganType = 3
                }

                R.id.lacto_obo -> {
                    binding.lactoObo.isSelected = binding.lactoObo.isSelected != true
                    binding.vegan.isSelected = false
                    binding.lacto.isSelected = false
                    binding.obo.isSelected = false
                    binding.fesco.isSelected = false

                    VeganType = 4
                }

                R.id.fesco -> {
                    binding.fesco.isSelected = binding.fesco.isSelected != true
                    binding.vegan.isSelected = false
                    binding.lacto.isSelected = false
                    binding.obo.isSelected = false
                    binding.lactoObo.isSelected = false

                    VeganType = 5
                }
                else -> VeganType = 0
            }
        }*/

        binding.vegan.setOnClickListener {
            binding.vegan.isSelected = binding.vegan.isSelected != true
            binding.lacto.isSelected = false
            binding.obo.isSelected = false
            binding.lactoObo.isSelected = false
            binding.fesco.isSelected = false

            if(binding.vegan.isSelected){
                VeganType = 1
            } else {
                VeganType = 0
            }
        }
        binding.lacto.setOnClickListener {
            binding.lacto.isSelected = binding.lacto.isSelected != true
            binding.vegan.isSelected = false
            binding.obo.isSelected = false
            binding.lactoObo.isSelected = false
            binding.fesco.isSelected = false

            if(binding.lacto.isSelected){
                VeganType = 2
            } else {
                VeganType = 0
            }
        }
        binding.obo.setOnClickListener {
            binding.obo.isSelected = binding.obo.isSelected != true
            binding.vegan.isSelected = false
            binding.lacto.isSelected = false
            binding.lactoObo.isSelected = false
            binding.fesco.isSelected = false

            if(binding.obo.isSelected){
                VeganType = 3
            } else {
                VeganType = 0
            }
        }
        binding.lactoObo.setOnClickListener {
            binding.lactoObo.isSelected = binding.lactoObo.isSelected != true
            binding.vegan.isSelected = false
            binding.lacto.isSelected = false
            binding.obo.isSelected = false
            binding.fesco.isSelected = false

            if(binding.lactoObo.isSelected){
                VeganType = 4
            } else {
                VeganType = 0
            }
        }
        binding.fesco.setOnClickListener {
            binding.fesco.isSelected = binding.fesco.isSelected != true
            binding.vegan.isSelected = false
            binding.lacto.isSelected = false
            binding.obo.isSelected = false
            binding.lactoObo.isSelected = false

            if(binding.fesco.isSelected){
                VeganType = 5
            } else {
                VeganType = 0
            }
        }

/*        binding.activeGroup.setOnClickListener {
            when (it) {
                binding.activeHigh -> {
                    binding.activeHigh.isSelected = binding.activeHigh.isSelected != true
                    binding.activeMiddle.isSelected = false
                    binding.activeLow.isSelected = false

                    ActivityType = 1
                }
                binding.activeMiddle -> {
                    binding.activeMiddle.isSelected = binding.activeMiddle.isSelected != true
                    binding.activeHigh.isSelected = false
                    binding.activeLow.isSelected = false

                    ActivityType = 2
                }
                binding.activeLow -> {
                    binding.activeLow.isSelected = binding.activeLow.isSelected != true
                    binding.activeHigh.isSelected = false
                    binding.activeMiddle.isSelected = false

                    ActivityType = 3
                }
                else -> ActivityType = 0
            }
        }*/

        binding.activeHigh.setOnClickListener {
            binding.activeHigh.isSelected = binding.activeHigh.isSelected != true
            binding.activeMiddle.isSelected = false
            binding.activeLow.isSelected = false

            if(binding.activeHigh.isSelected){
                ActivityType = 1
            } else {
                ActivityType = 0
            }
        }
        binding.activeMiddle.setOnClickListener {
            binding.activeMiddle.isSelected = binding.activeMiddle.isSelected != true
            binding.activeHigh.isSelected = false
            binding.activeLow.isSelected = false

            if(binding.activeMiddle.isSelected){
                ActivityType = 2
            } else {
                ActivityType = 0
            }
        }

        binding.activeLow.setOnClickListener {
            binding.activeLow.isSelected = binding.activeLow.isSelected != true
            binding.activeHigh.isSelected = false
            binding.activeMiddle.isSelected = false

            if(binding.activeLow.isSelected){
                ActivityType = 3
            } else {
                ActivityType = 0
            }
        }

        binding.nextBtn.setOnClickListener {
            if (VeganType == 0 && ActivityType == 0) {
                Toast.makeText(this, "필수정보가 미기입되어있습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Log.d(
                    "Tag", "OneActivity에서 넘어온 정보: " + text +
                            "비건 타입: ${VeganType}, 활동량 타입: ${ActivityType}" +
                            "키:${
                                binding.joinHeight.text.toString().trim()
                            }, 몸무게: ${binding.joinWeight.text.toString().trim()}"
                )
            }
        }
    }
}