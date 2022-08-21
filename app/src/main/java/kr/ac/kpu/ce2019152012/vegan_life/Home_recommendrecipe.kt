package kr.ac.kpu.ce2019152012.vegan_life

import android.app.appsearch.AppSearchManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.ac.kpu.ce2019152012.vegan_life.databinding.ActivityHomeRecommendrecipeBinding

class Home_recommendrecipe : AppCompatActivity() {
    private lateinit var binding: ActivityHomeRecommendrecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeRecommendrecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

}