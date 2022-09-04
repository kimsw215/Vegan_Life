package kr.ac.kpu.ce2019152012.vegan_life.MainFragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kotlinx.parcelize.parcelableCreator
import kr.ac.kpu.ce2019152012.vegan_life.DataVo.CalendarFoodDataVo
import kr.ac.kpu.ce2019152012.vegan_life.R
import kr.ac.kpu.ce2019152012.vegan_life.databinding.FragmentCalendarFoodinputdetailBinding
import java.time.LocalDate
import java.util.*
import kotlin.collections.HashMap

class FoodAddDetailFragment : Fragment() {
    private var _binding: FragmentCalendarFoodinputdetailBinding? = null
    private val binding get() = _binding!!

    private var auth: FirebaseAuth? = null
    private lateinit var db: FirebaseFirestore

    val datas = mutableListOf<CalendarFoodDataVo>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarFoodinputdetailBinding.inflate(inflater,container,false)
        val view = binding.root

        auth = FirebaseAuth.getInstance()
        setup()



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nextBtn.setOnClickListener {
            var dattime: String? = arguments?.getString("Timetype")
            var time_sub = dattime?.substring(dattime.length-2 until dattime.length)
//            var time:String = dattime!!.slice(dattime.length-2 until dattime.length)

            var Capacity : String= binding.inputCapacity.text.toString()

            datas.add(
                CalendarFoodDataVo(
                    time_sub,null,
                    binding.inputName.text.toString(),dattime,
                    binding.inputKcal.text.toString().toInt(), Capacity,
                    binding.inputCar.text.toString().toInt(), binding.inputProtein.text.toString().toInt(),
                    binding.inputFat.text.toString().toInt())
            )

            var FoodData = hashMapOf(
                "Timetype" to datas[0].timetype,
                "foodPhoto" to datas[0].foodphoto,
                "foodName" to datas[0].foodname,
                "Day" to datas[0].day,
                "Kcal" to datas[0].kcal,
                "Capacity" to datas[0].gml,
                "Car" to datas[0].car,
                "Pro" to datas[0].protein,
                "Fat" to datas[0].fat
            )
            db.collection(auth?.currentUser?.email.toString()).document(dattime.toString())
                .set(FoodData, SetOptions.merge())

            val bundle = Bundle()
            bundle.putParcelable("item",datas[0])
            view?.findNavController()?.navigate(R.id.action_foodinputdetailFragment_to_foodInputFragment,bundle)
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
}