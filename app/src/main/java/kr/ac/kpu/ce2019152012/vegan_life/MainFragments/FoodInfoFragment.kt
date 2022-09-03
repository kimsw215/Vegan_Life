package kr.ac.kpu.ce2019152012.vegan_life.MainFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kr.ac.kpu.ce2019152012.vegan_life.R
import kr.ac.kpu.ce2019152012.vegan_life.databinding.FragmentCalendarFoodinfoBinding

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
    private fun updateUI(user: FirebaseUser?) {
    }

    private fun reload() {
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