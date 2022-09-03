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

        // 날짜 설정
        val current : LocalDate =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDate.now()
            } else {
                TODO("VERSION.SDK_INT < O")
            }
        var date = current.toString()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nextBtn.setOnClickListener {
            datas.add(
                CalendarFoodDataVo(0,null,
                    binding.inputName.toString(),"",
                    binding.inputKcal.toString().toInt(),binding.inputCapacity.toString().toInt(),
                    binding.inputCar.toString().toInt(),binding.inputProtein.toString().toInt(),
                    binding.inputFat.toString().toInt())
            )
            val bundle = Bundle()
            bundle.putParcelable("item",datas[0])
            view?.findNavController()?.navigate(R.id.action_foodinputdetailFragment_to_foodInputFragment,bundle)
                .run {
                    db.collection(auth?.currentUser?.email.toString()).document(binding.inputName.toString())
                        .set(datas, SetOptions.merge())
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun doDayOfWeek(): String? {
        val cal: Calendar = Calendar.getInstance()
        var strWeek: String? = null
        val nWeek: Int = cal.get(Calendar.DAY_OF_WEEK)

        if (nWeek == 1) {
            strWeek = "일"
        } else if (nWeek == 2) {
            strWeek = "월"
        } else if (nWeek == 3) {
            strWeek = "화"
        } else if (nWeek == 4) {
            strWeek = "수"
        } else if (nWeek == 5) {
            strWeek = "목"
        } else if (nWeek == 6) {
            strWeek = "금"
        } else if (nWeek == 7) {
            strWeek = "토"
        }
        return strWeek
    }

    fun setup() {
        db = Firebase.firestore

        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        db.firestoreSettings = settings
    }
}