package kr.ac.kpu.ce2019152012.vegan_life.MainFragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kr.ac.kpu.ce2019152012.vegan_life.Adapter.CalendarLineaAdapter
import kr.ac.kpu.ce2019152012.vegan_life.DataVo.CalendarFoodDataVo
import kr.ac.kpu.ce2019152012.vegan_life.DataVo.CalendarFoodListDataVo
import kr.ac.kpu.ce2019152012.vegan_life.R
import kr.ac.kpu.ce2019152012.vegan_life.databinding.FragmentCalendarFoodinputBinding

class FoodInputFragment : Fragment() {
    private var _binding: FragmentCalendarFoodinputBinding? = null
    private val binding get() = _binding!!

    private var viewFoodfile : View? = null
    var pickImageFromAlbum = 0
    var fbStorage : FirebaseStorage? = null

    // Uri 받아오기 위한 전역 함수
    private var ImgUri: Uri? = null

    private lateinit var datas: ArrayList<CalendarFoodListDataVo>

    // Adapter 변수 선언
    private lateinit var Adapter: CalendarLineaAdapter

    // gridManager
    private lateinit var Lmanager: LinearLayoutManager

    val SelectDataList = mutableListOf<CalendarFoodListDataVo>()

    private var auth: FirebaseAuth? = null
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarFoodinputBinding.inflate(inflater, container, false)
        val view = binding.root

        Toast.makeText(requireContext(), "데이터가 없으면 직접기록 버튼 먼저 눌러주세요.", Toast.LENGTH_SHORT).show()

        FirebaseApp.initializeApp(requireActivity())
        auth = FirebaseAuth.getInstance()
        fbStorage = FirebaseStorage.getInstance()

        setup()
//        initRecycler()

        if (arguments?.getParcelable<CalendarFoodDataVo>("item") != null) {
            var foodname: String? =
                arguments?.getParcelable<CalendarFoodDataVo>("item")?.foodname.toString()
            var kcal: String? =
                arguments?.getParcelable<CalendarFoodDataVo>("item")?.kcal.toString() + "Kcal"

            binding.foodname.setText(foodname)
            binding.foodkcal.setText(kcal)
            binding.delete.setText("삭제")
        }

        var day = arguments?.getParcelable<CalendarFoodDataVo>("item")?.day.toString()
        var dat_sub = day.substring(0 until day.length - 2)
        Log.d("time", "캘랜더에서 보낸 시간: " + arguments?.getString("daytime").toString())
        Log.d("time", "input에서 보낸 시간: " + dat_sub)

        var calendartime = arguments?.getString("daytime").toString()
        var calendartime_sub = calendartime.substring(0 until calendartime.length - 2)

        if (day == null && calendartime != null) {
            binding.editDate.setText(calendartime_sub)
        } else if (day != null) {
            binding.editDate.setText(dat_sub)
        }
        return view
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.food.setOnClickListener {
            when {
                // 갤러리 접근 권한이 있는 경우
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
                -> {
                    navigateGallery()
                }

                // 갤러리 접근 권한이 없는 경우 & 교육용 팝업을 보여줘야 하는 경우
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                -> {
                    showPermissionContetxtPopup()
                }

                // 권한 요청하기(requestPermissions) -> 갤러리 접근(onRequestPermissionResult)
                else -> requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    1000
                )
            }
        }

        val bundle = Bundle()
        binding.btnAddfood.setOnClickListener {

            var dattime: String? = arguments?.getString("daytime")
            var time_sub = dattime?.substring(dattime.length - 2 until dattime.length)

            bundle.putString("Timetype", dattime)
            Log.d("whattime", "time: " + dattime)
            view?.findNavController()
                ?.navigate(R.id.action_foodInputFragment_to_foodinputdetailFragment, bundle)
        }

        binding.delete.setOnClickListener {
            binding.foodname.text = ""
            binding.foodkcal.text = ""
            binding.delete.text = ""
        }

        binding.nextBtn.setOnClickListener {
            var dattime: String? = arguments?.getString("daytime")
            view.findNavController().navigate(R.id.action_foodInputFragment_to_calendarFragment)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1000 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    navigateGallery()
                else
                    Toast.makeText(requireContext(), "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                //
            }
        }
    }

    private fun navigateGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        // 가져올 콘턴츠들 중에서 Image 만을 가져온다
        intent.type = "image/*"
        // 갤러리에서 이미지를 선택한 후, 프로필 이미지 뷰를 수정하기 위해 갤러리에서 수행한 값을 받아오는 startActivityForeResult를 사용한다.
        startActivityForResult(intent, 2000)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 예외처리
        if (resultCode != RESULT_OK)
            return

        when (requestCode) {
            // 2000: 이미지 컨텐츠를 가져오는 액티비티를 수행한 후 실행되는 Activity 일 때만 수행하기 위해서
            2000 -> {
                val selectedImageUri: Uri? = data?.data
                if (selectedImageUri != null) {
//                    binding.food.setImageURI(selectedImageUri)
                    Glide.with(this).load(selectedImageUri).into(binding.food)
                    ImgUri = selectedImageUri
                    var dattime: String? = arguments?.getString("daytime")
                    val photomap = hashMapOf(
                        "foodPhoto" to ImgUri
                    )
                    db.collection(auth?.currentUser?.email.toString()).document(dattime.toString())
                        .set(photomap).addOnSuccessListener{}
                } else {
                    Toast.makeText(requireContext(), "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(requireContext(), "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showPermissionContetxtPopup() {
        AlertDialog.Builder(requireContext())
            .setTitle("권한이 필요합니다.")
            .setMessage("음식 이미지를 추가하기 위해서는 갤러리 접근 권한이 필요합니다.")
            .setPositiveButton("동의") { _, _ ->
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    1000
                )
            }
            .setNegativeButton("취소") { _, _ -> }
            .create()
            .show()
    }

    // SelectedAdapter
    /*  @SuppressLint("NotifyDataSetChanged")
private fun initRecycler(){
    Adapter = CalendarLineaAdapter()
    Lmanager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

    binding.selectedFood.apply {
        layoutManager = Lmanager
        adapter = Adapter
        setHasFixedSize(true)
    }
    var name = arguments?.getParcelable<CalendarFoodDataVo>("item")?.foodname.toString()
    var kcal = arguments?.getParcelable<CalendarFoodDataVo>("item")?.kcal.toString()

    SelectDataList.add((CalendarFoodListDataVo(name,kcal+"kcal","삭제")))

    binding.selectedFood.adapter?.notifyDataSetChanged()

    Adapter.setOnItemClickListener(object : CalendarLineaAdapter.OnItemClickListener {
        override fun onItemClick(v: View, data: CalendarFoodListDataVo, pos: Int) {

        }
    })
}*/
/*    @SuppressLint("NotifyDataSetChanged")
    private fun deleteRecycler(data: CalendarFoodListDataVo){
        SelectDataList.remove(data)
        binding.selectedFood.adapter?.notifyDataSetChanged()
    }*/

/*    fun ImageUpload(view : View, time : String){
        var imgFileName = auth?.currentUser?.email.toString() + time + "_.png"
        var storageRef = fbStorage?.reference?.child("images")?.child(imgFileName)

        storageRef?.putFile()
    }*/

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

