package kr.ac.kpu.ce2019152012.vegan_life.MainFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kr.ac.kpu.ce2019152012.vegan_life.Adapter.SearchAdapter
import kr.ac.kpu.ce2019152012.vegan_life.DataVo.CalendarFoodDataVo
import kr.ac.kpu.ce2019152012.vegan_life.DataVo.CalendarFoodListDataVo
import kr.ac.kpu.ce2019152012.vegan_life.R
import kr.ac.kpu.ce2019152012.vegan_life.databinding.FragmentCalendarFoodinputBinding

class FoodInputFragment : Fragment() {
    private var _binding: FragmentCalendarFoodinputBinding? = null
    private val binding get() = _binding!!

    // Adapter 변수 선언
    private lateinit var SearchAdapter: SearchAdapter

    // gridManager
    private lateinit var SearchGManager: GridLayoutManager

    // 검색 정보 변수 선언
    private lateinit var datas: ArrayList<CalendarFoodListDataVo>

    // 검색창에 넣을 데이터
    val SearchDataList = mutableListOf<CalendarFoodListDataVo>()

    // 검색창에서 선택한 데이터를 가져오는 일
    val SelectDataList = mutableListOf<CalendarFoodDataVo>()

    private var auth: FirebaseAuth? = null
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarFoodinputBinding.inflate(inflater,container,false)
        val view = binding.root

        Toast.makeText(requireContext(),"데이터가 없으면 직접기록 버튼 먼저 눌러주세요.",Toast.LENGTH_SHORT).show()

        // inputDetail 에서 받아온 정보를 선택한 음식에 넣어야 함.
        val bundle = Bundle()
        bundle.getParcelable<CalendarFoodDataVo>("item")


//        binding.searchFood.setOnQueryTextListener()
//        setAdapter()

       return view
    }

/*    var searchViewTextListener: SearchView.OnQueryTextListener =
        object : SearchView.OnQueryTextListener{
            // 검색버튼 입력 시 호출, 검색 버튼이 없으므로 사용하지 않음
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            // 텍스트 입력/수정시에 호출
            override fun onQueryTextChange(p0: String?): Boolean {
                SearchAdapter.filter.filter(p0)
                return false
            }
        }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddfood.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_foodInputFragment_to_foodinputdetailFragment)
        }

        val dattime: String? = arguments?.getString("time")
        val time = dattime!!.slice(dattime.length-2 until dattime.length)

        binding.nextBtn.setOnClickListener {
            when(time){
                "아침" -> {

                }
                "점심" -> {

                }
                "저녁" -> {

                }
                "간식" -> {

                }
            }
        }
    }

    // SearchAdapter
    private fun setAdapter() {
        SearchAdapter = SearchAdapter(datas)
        SearchGManager = GridLayoutManager(requireContext(),2,LinearLayoutManager.VERTICAL,false)

        binding.searchRecycler.apply {
            layoutManager = SearchGManager
            adapter = SearchAdapter
            setHasFixedSize(true)
        }

        SearchAdapter.setOnItemClickListener(object : SearchAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: CalendarFoodListDataVo, post: Int) {

            }
        })
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
