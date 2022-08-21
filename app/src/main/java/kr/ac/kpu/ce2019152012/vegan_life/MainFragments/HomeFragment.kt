package kr.ac.kpu.ce2019152012.vegan_life.MainFragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kr.ac.kpu.ce2019152012.vegan_life.Login_Join.LoginActivity
import kr.ac.kpu.ce2019152012.vegan_life.R
import kr.ac.kpu.ce2019152012.vegan_life.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var auth: FirebaseAuth? = null
    private lateinit var db: FirebaseFirestore


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        val view = binding.root

        FirebaseApp.initializeApp(requireActivity())

        auth = FirebaseAuth.getInstance()
        setup()

        var UserList = arrayListOf<String>()

        db.collection(auth?.currentUser?.email.toString().trim())
            .document("Info")
            .get()
            .addOnSuccessListener {
                UserList.add(it["nickname"].toString().trim())

                binding.homeNickname.text = it["nickname"].toString().trim() + " 님"

                binding.homeRecipeName.text = it["nickname"].toString().trim() + " 님의 추천 식단"
                Log.d("info",it["nickname"].toString().trim())
                UserList.add(it["basiccal"].toString().trim())

                Log.d("info","닉네임, 칼로리" + UserList.toString().trim())
            }.addOnFailureListener { exception ->
                Log.d("error", "Error getting documents: ", exception)
            }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
