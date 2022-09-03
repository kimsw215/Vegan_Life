package kr.ac.kpu.ce2019152012.vegan_life.MainFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kr.ac.kpu.ce2019152012.vegan_life.DataVo.RecipeDataVo
import kr.ac.kpu.ce2019152012.vegan_life.R
import kr.ac.kpu.ce2019152012.vegan_life.databinding.FragmentHomeRecommendrecipeBinding

class RecipeNextFragment : Fragment() {
    private var _binding: FragmentHomeRecommendrecipeBinding? = null
    private val binding get() = _binding!!

    private var auth: FirebaseAuth? = null
    private lateinit var db: FirebaseFirestore

    private var likeCheck : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeRecommendrecipeBinding.inflate(inflater, container, false)
        val view = binding.root

        FirebaseApp.initializeApp(requireActivity())

        auth = FirebaseAuth.getInstance()
        setup()

        Glide.with(requireContext())
            .load(arguments?.getParcelable<RecipeDataVo>("item")?.recipephoto)
            .into(binding.foodimg)

        binding.txtRecipename.text = arguments?.getParcelable<RecipeDataVo>("item")?.recipename
        binding.ingredientItem.text = arguments?.getParcelable<RecipeDataVo>("item")?.ingredient
        binding.howItem.text = arguments?.getParcelable<RecipeDataVo>("item")?.how

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLike.setOnClickListener {
            if(likeCheck == false){
                Glide.with(requireContext())
                    .load(R.drawable.selected_heart)
                    .into(binding.btnLike)
                likeCheck = true
            } else {
                Glide.with(requireContext())
                    .load(R.drawable.like)
                    .into(binding.btnLike)
                likeCheck = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setup() {
        db = Firebase.firestore

        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        db.firestoreSettings = settings
    }
}