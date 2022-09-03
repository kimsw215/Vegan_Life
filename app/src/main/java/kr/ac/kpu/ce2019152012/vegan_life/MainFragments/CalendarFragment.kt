package kr.ac.kpu.ce2019152012.vegan_life.MainFragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kr.ac.kpu.ce2019152012.vegan_life.R
import kr.ac.kpu.ce2019152012.vegan_life.databinding.FragmentCalendarBinding
import java.time.LocalDate
import java.util.*

class CalendarFragment : Fragment() {
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private var auth: FirebaseAuth? = null
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater,container,false)
        val view = binding.root

        // 요일 + 날짜 설정
        val today: String? = doDayOfWeek()
        binding.dayTitle.setText(today)

        val current : LocalDate =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDate.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        var date = current.toString()
        binding.datetime.setText(date)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.breakfastImage.setOnClickListener {
            val bundle = Bundle()
            if(binding.breakfastImage.drawable == null){
                var timelist : String = binding.datetime.toString() + "아침"
                bundle.putString("time",timelist)
                Log.d("time",timelist.slice(timelist.length-2 until timelist.length))
                view?.findNavController()?.navigate(R.id.action_calendarFragment_to_foodInputFragment,bundle)
                    .run {
                        bundle.clear() }
            } else {
                view?.findNavController()?.navigate(R.id.action_calendarFragment_to_foodinfoFragment,bundle)
            }
        }
        binding.lunchImage.setOnClickListener {
            var timelist : String = binding.datetime.toString() + "점심"
            val bundle = Bundle()
            bundle.putString("time",timelist)
            if(binding.breakfastImage.drawable == null){
                view?.findNavController()?.navigate(R.id.action_calendarFragment_to_foodInputFragment,bundle)
            } else {
                view?.findNavController()?.navigate(R.id.action_calendarFragment_to_foodinfoFragment,bundle)
            }
        }
        binding.dinnerImage.setOnClickListener {
            var timelist : String = binding.datetime.toString() + "저녁"
            val bundle = Bundle()
            bundle.putString("time",timelist)
            if(binding.breakfastImage.drawable == null){
                view?.findNavController()?.navigate(R.id.action_calendarFragment_to_foodInputFragment,bundle)
            } else {
                view?.findNavController()?.navigate(R.id.action_calendarFragment_to_foodinfoFragment,bundle)
            }
        }
        binding.snackImage.setOnClickListener {
            var timelist : String = binding.datetime.toString() + "간식"
            val bundle = Bundle()
            bundle.putString("time",timelist)
            if(binding.breakfastImage.drawable == null){
                view?.findNavController()?.navigate(R.id.action_calendarFragment_to_foodInputFragment,bundle)
            } else {
                view?.findNavController()?.navigate(R.id.action_calendarFragment_to_foodinfoFragment,bundle)
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
}