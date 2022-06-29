package kr.ac.kpu.ce2019152012.vegan_life.Login_Join

import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import kr.ac.kpu.ce2019152012.vegan_life.R
import kr.ac.kpu.ce2019152012.vegan_life.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.joinText.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        binding.editPw.addTextChangedListener {
            if(it!!.length > 4){
                binding.loginBtn.isClickable = true
                binding.loginBtn.isEnabled = true
            } else {
                binding.loginBtn.isClickable = false
                binding.loginBtn.isEnabled = false
            }
        }


    }
}