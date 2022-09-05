package kr.ac.kpu.ce2019152012.vegan_life.MainFragments

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.FirebaseApp
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

    private var SumCar: Int= 0
    private var SumPro: Int= 0
    private var SumFat: Int= 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater,container,false)
        val view = binding.root

        FirebaseApp.initializeApp(requireActivity())
        auth = FirebaseAuth.getInstance()

        setup()

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

        // 사용자의 탄단지 설정
        db.collection(auth?.currentUser?.email.toString()).document("Info")
            .get().addOnSuccessListener {
                var cal: Int = (it["basiccal"].toString().toDouble() * (0.5)).toInt()
                var pro: Int = (it["basiccal"].toString().toDouble() * (0.3)).toInt()
                var fat: Int = (it["basiccal"].toString().toDouble() * (0.2)).toInt()

                binding.carKcal.text = "/" + cal.toString() + "g"
                binding.proteinKcal.text = "/" + pro.toString() + "g"
                binding.fatKcal.text = "/" + fat.toString() + "g"
            }

        // 현재 섭취한 탄단지 설정
        var todayMor : String = binding.datetime.text.toString() + "아침"
        var todayLun : String = binding.datetime.text.toString() + "점심"
        var todayEve : String = binding.datetime.text.toString() + "저녁"
        var todaySna : String = binding.datetime.text.toString() + "간식"

/*        db.collection(auth?.currentUser?.email.toString()).document(todayMor)
            .get().addOnSuccessListener {
                if(it["Car"].toString().toInt() != null){
                    SumCar += it["Car"].toString().toInt()
                } else {SumCar = 0}

                if(it["Pro"].toString().toInt() != null){
                    SumPro += it["Pro"].toString().toInt()
                } else {SumCar = 0}
                if(it["Fat"].toString().toInt() != null){
                    SumFat += it["Fat"].toString().toInt()
                } else {SumCar = 0}
            }

        db.collection(auth?.currentUser?.email.toString()).document(todayLun)
            .get().addOnSuccessListener {
                if(it["Car"].toString().toInt() != null){
                    SumCar += it["Car"].toString().toInt()
                } else {SumCar = 0}
                if(it["Pro"].toString().toInt() != null){
                    SumPro += it["Pro"].toString().toInt()
                } else {SumCar = 0}
                if(it["Fat"].toString().toInt() != null){
                    SumFat += it["Fat"].toString().toInt()
                } else {SumCar = 0}
            }

        db.collection(auth?.currentUser?.email.toString()).document(todayEve)
            .get().addOnSuccessListener {
                if(it["Car"].toString().toInt() != null){
                    SumCar += it["Car"].toString().toInt()
                } else {SumCar = 0}
                if(it["Pro"].toString().toInt() != null){
                    SumPro += it["Pro"].toString().toInt()
                } else {SumCar = 0}
                if(it["Fat"].toString().toInt() != null){
                    SumFat += it["Fat"].toString().toInt()
                } else {SumCar = 0}
            }

        db.collection(auth?.currentUser?.email.toString()).document(todaySna)
            .get().addOnSuccessListener {
                if(it["Car"].toString().toInt() != null){
                    SumCar += it["Car"].toString().toInt()
                } else {SumCar = 0}
                if(it["Pro"].toString().toInt() != null){
                    SumPro += it["Pro"].toString().toInt()
                } else {SumCar = 0}
                if(it["Fat"].toString().toInt() != null){
                    SumFat += it["Fat"].toString().toInt()
                } else {SumCar = 0}
            }*/

        binding.carIntakeKacl.setText(SumCar.toString())
        binding.proteinIntakeKcal.setText(SumPro.toString())
        binding.fatIntakeKacl.setText(SumFat.toString())

        Log.d("ima","아침 음식 사진: "+binding.breakfastImage.drawable.toString())

        // 아침 점심 저녁 간식 화면 구성하기
        if(binding.breakfastImage.drawable != null){
            db.collection(auth?.currentUser?.email.toString()).document(todayMor)
                .get().addOnSuccessListener {
                    var foodKcal = it["Kcal"].toString()

                    Glide.with(this).load(it["foodPhoto"]).into(binding.breakfastImage)
                    binding.breakfastKcal.setText(foodKcal)
                }
        }
        if(binding.lunchImage.drawable != null){
            db.collection(auth?.currentUser?.email.toString()).document(todayLun)
                .get().addOnSuccessListener {
                    var foodKcal = it["Kcal"].toString()
                    binding.lunchImage.setImageResource(it["foodPhoto"].toString().toInt())
                    binding.lunchKacl.setText(foodKcal)
                }
        }
        if(binding.dinnerImage.drawable != null){
            db.collection(auth?.currentUser?.email.toString()).document(todayEve)
                .get().addOnSuccessListener {
                    var foodKcal = it["Kcal"].toString()
                    binding.dinnerImage.setImageResource(it["foodPhoto"].toString().toInt())
                    binding.dinnerKcal.setText(foodKcal)
                }
        }
        if(binding.snackImage.drawable != null){
            db.collection(auth?.currentUser?.email.toString()).document(todaySna)
                .get().addOnSuccessListener {
                    var foodKcal = it["Kcal"].toString()
                    binding.snackImage.setImageResource(it["foodPhoto"].toString().toInt())
                    binding.snackKcal.setText(foodKcal)
                }
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.breakfastImage.setOnClickListener {
            val bundle = Bundle()
            if(binding.breakfastImage.drawable == null){
                var timelist : String = binding.datetime.text.toString() + "아침"
                bundle.putString("daytime",timelist)
                Log.d("time",timelist.slice(timelist.length-2 until timelist.length))
                view?.findNavController()?.navigate(R.id.action_calendarFragment_to_foodInputFragment,bundle)
            } else {
                view?.findNavController()?.navigate(R.id.action_calendarFragment_to_foodinfoFragment,bundle)
            }
        }
        binding.lunchImage.setOnClickListener {
            var timelist : String = binding.datetime.text.toString() + "점심"
            val bundle = Bundle()
            bundle.putString("daytime",timelist)
            if(binding.breakfastImage.drawable == null){
                view?.findNavController()?.navigate(R.id.action_calendarFragment_to_foodInputFragment,bundle)
            } else {
                view?.findNavController()?.navigate(R.id.action_calendarFragment_to_foodinfoFragment,bundle)
            }
        }
        binding.dinnerImage.setOnClickListener {
            var timelist : String = binding.datetime.text.toString() + "저녁"
            val bundle = Bundle()
            bundle.putString("daytime",timelist)
            if(binding.breakfastImage.drawable == null){
                view?.findNavController()?.navigate(R.id.action_calendarFragment_to_foodInputFragment,bundle)
            } else {
                view?.findNavController()?.navigate(R.id.action_calendarFragment_to_foodinfoFragment,bundle)
            }
        }
        binding.snackImage.setOnClickListener {
            var timelist : String = binding.datetime.text.toString() + "간식"
            val bundle = Bundle()
            bundle.putString("daytime",timelist)
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