package kr.ac.kpu.ce2019152012.vegan_life.MainFragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kr.ac.kpu.ce2019152012.vegan_life.R
import kr.ac.kpu.ce2019152012.vegan_life.databinding.FragmentCalendarFoodinfoBinding
import java.time.LocalDate

class FoodInfoFragment : Fragment() {
    private var _binding: FragmentCalendarFoodinfoBinding? = null
    private val binding get() = _binding!!

    private var auth: FirebaseAuth? = null
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarFoodinfoBinding.inflate(inflater,container,false)
        val view = binding.root

        FirebaseApp.initializeApp(requireActivity())
        auth = FirebaseAuth.getInstance()

        setup()

        val daytime = arguments?.getString("daytime").toString()

        db.collection(auth?.currentUser?.email.toString()).document(daytime)
            .get().addOnSuccessListener {
                var foodname = it["foodName"].toString()
                var foodcar = it["Car"].toString() + "g"
                var foodpro = it["Pro"].toString() + "g"
                var foodfat = it["Fat"].toString() + "g"

                binding.foodDetailImage.setImageResource(it["foodPhoto"].toString().toInt())
                binding.foodDetailName.setText(foodname)
                binding.fooddetailCarkcal.setText(foodcar)
                binding.fooddetailProteinkcal.setText(foodpro)
                binding.fooddetailFatkcal.setText(foodfat)
            }

        val bundle = Bundle()
        val time: String = bundle.getString("time").toString()

        binding.txtTitle.text = time.substring(time.length-2,time.length)
        binding.foodDetailDatetime.text = time.substring(0,time.length-3)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.foodDetailRewrite.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_foodinfoFragment_to_foodInputFragment)
                .run {

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

    companion object {
        private const val TAG = "EmailPassword"
    }
}