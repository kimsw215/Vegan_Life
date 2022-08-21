package kr.ac.kpu.ce2019152012.vegan_life.MainFragments

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kr.ac.kpu.ce2019152012.vegan_life.Adapter.HomeRecipeAdapter
import kr.ac.kpu.ce2019152012.vegan_life.DataVo.RecipeDataVo
import kr.ac.kpu.ce2019152012.vegan_life.R
import kr.ac.kpu.ce2019152012.vegan_life.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var auth: FirebaseAuth? = null
    private lateinit var db: FirebaseFirestore

    // Adapter 변수 선언
    private lateinit var Adapter : HomeRecipeAdapter

    // gridManager
    private lateinit var Gmanager : GridLayoutManager

    val datas = mutableListOf<RecipeDataVo>()
//    private lateinit var datas: ArrayList<RecipeDataVo>


    // 시간 변수
    var _context: Context? = null
    private var br: BroadcastReceiver? = null
    private var am: AlarmManager? = null

    // 24시 자정 제크 함수
    private var receiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            /*Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String string = bundle.getString("");
                int resultCode = bundle.getInt("");
            }*/
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        val view = binding.root

        FirebaseApp.initializeApp(requireActivity())

        auth = FirebaseAuth.getInstance()

        // db 셋업
        setup()
        // 24시간 자정 기준 이벤트 변화
        broad_setup()

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

    fun broad_setup(){
        br = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {

                Adapter = HomeRecipeAdapter()

                Gmanager = GridLayoutManager(requireContext(), 2)

                binding.rvList.apply {
                    layoutManager = Gmanager
                    adapter = Adapter
                    setHasFixedSize(true)
                }

                datas.apply {
                    add(RecipeDataVo("아스파라거스 크림파스타",R.drawable.asparagus_cream_pasta,
                        "파스타 80g\n아스파라거스 100g\n두유 160ml\n캐슈넛가루 1스푼 (생략가능)\n마늘 4알\n양파 \n올리브오일 1스푼\n파스타 삶을 물 1800cc\n소금 2큰술\n후추\n",
                            "1 아스파라거스 손질은 끝 부분만 칼이나 채칼로 살짝 벗겨내준다. \n섬유질이 굵어서 나무같이 씹힐 수 있기 때문. \n딱히 거부감이 없다면 그대로 먹어도 됨!\n2 아스파라거스 머리 부분을 잡고 채칼로 앞뒤를 쓱 쓱 썰어준다. \n3 다진 마늘, 양파, 올리브오일을 넣고 팬에 볶아주다가 마늘이 익으면 아스파라거스를 넣고 볶는다.\n4 두유 160ml, 캐슈넛 가루, 면수 국자 한스푼을 넣고 익혀주다가 7분 삶은 면을 넣어준다. \n소금, 후추로 간한다. 면수가 들어가서 짭짤할테니 맛을 보고 소금을 추가한다."))

                    add(RecipeDataVo("비트 리조또",R.drawable.beet_risotto,
                        "비트 중 사이즈 1개 (100g)\n보리 100g\n식초 2T\n소금 0.5t\n캐슈넛 30g\n다시마 2조각\n올리브오일 1T (선택)\n",
                        "1. 비트는 깨끗이 씼어서 껍질 채로 40분 이상 푹 삶는다. \n오래 삶아야 부드럽고 잘 갈리고 은은한 단맛이 난다. 덜 익은 비트는 딱딱하고 약간 흙 맛이 난다.\n2 1시간 이상 충분히 불린 보리는 다시마 2조각을 넣고 끓인다. \n물은 보리와 동일한 양을 넣는다. 강불에서 3분 - 불 줄여서 2분 - 3분 뜸들이기. \n나는 냄비에서 했지만 리조또를 만들 팬에서 바로 조리해도 좋다. (타지 않도록 주의할 것!)\n3 불린 캐슈넛은 블렌더에 넣고 갈아준다. 잘 갈리지 않을 땐 두유나 물을 한스푼씩 넣어 갈아본다. 한꺼번에\n 많이 넣지 않고 조금씩 증량하며 갈아주기. 크림같은 질감이 되면 완성\n4 푹 익은 비트는 옥수수같은 냄새가 난다. 껍질을 벗기면 홍시처럼 잘 벗겨진다! 껍질 벗기고 적당한 크기로 썰어 블렌더에 갈아준다. \n이 때 식초 2스푼을 함께 넣어준다. 식초가 비트의 맛을 확 끌어올린다. \n잘 안갈리면 물을 조금씩 넣어(스푼으로)보며 갈아준다. 물 한꺼번에 많이 넣지 않기..\n5 팬에 보리 넣고, 다시마 물(보리밥 한 냄비에 물 부어서 씀)을 조금씩 넣어 볶아주다가 소금 반 티스푼을 넣는다. \n간을 보고 적절하게 소금양을 조절한다.\n"))

                    add(RecipeDataVo("통양파 김치찜",R.drawable.braised_kimchi_with_onion,
                        "김치 1/4포기 (200g)\n통양파 1개 (200g)\n두부 반모 (200g)\n들기름 1스푼\n된장 0.5스푼\n쌀뜨물 500ml\n",
                        "1 김치, 양파 통으로, 쌀뜨물 300ml 넣는다. (기호에 따라 된장 반스푼 추가)\n2 뚜껑 닫고 팔팔 끓으면 중불로 20분 끓인다.\n3 35분 정도 끓였을 때 들기름 1스푼을 넣는다. \n5분 정도만 더 끓여준다. 그동안 두부를 데친다.\n4 40분 정도 끓이면 완성~~"))

                    add(RecipeDataVo("꽈리꼬추조림",R.drawable.braised_short_necked_red_pepper_paste,
                        "꽈리고추 100g\n감자 작은거 2개\n가지 1/2개\n마늘 4개\n간장 1.5T\n설탕 1T\n물 3T\n들기름 1T\n참깨\n",
                        "1 꽈리고추는 손가락 마디 길이로 잘라준다. 끝부분이 매운지 살짝 냄새만 맡았는데도 매운향이 난다.\n2 감자, 가지도 비슷한 길이로 썰어준다. 마늘은 적당히 가로로 두툼하게 썰기\n3 양념장을 미리 준비한다.- 진간장 1.5스푼, 설탕 1스푼, 물 3스푼 넣어 준비한다. 들기름은 마지막에 둘러줄 것이니 제외!\n4 단단한 순서로 익힌다. 먼저 감자익히기. 기름 대신에 물 부어서 볶는다\n5 감자가 약간 불투명하게 색이 바뀌면 가지, 마늘, 양념장을 넣고 볶는다.\n6 꽈리고추 매우니까 푹 익혀야 할 것 같은 느낌이라 조금 더 물로 볶아주다가(사실 익혀도 매운건 마찬가지..) 마지막에 들기름 1스푼, 참깨 뿌려주었다.\n"))

             /*       add(RecipeDataVo("우엉잡채",R.drawable.burdock_japchae,
                        "우엉 150g\n초록 피망 100g\n파프리카 빨간색 100g\n양파 1/4\n간장 2스푼\n조청 1스푼\n참기름 1스푼\n",
                        "1 우엉의 특유의 향과 맛은 껍질에 있기 때문에 되도록 껍질을 벗기지 않도록 깨끗하게 씻는다. 손바닥 길이 정도로 얇게 채로 썰어준다. \n썰고 나서는 바로 식초가 들어간 물에 담궈준다. 갈변을 막기 위해..라고 함\n2 물에 소금과 식초를 넣어 팔팔 끓으면 1분동안 우엉을 데쳐낸다. 건져서 찬물에 담궈둔다. \n이렇게 하면 우엉에 탄력도 생기고 조리 시간도 단축된다.\n3 청피망, 빨간 파프리카와 양파를 얇게 채썰어준다.\n4 양념장 만들기. 진간장2스푼, 조청1스푼, 물 6스푼 넣고 잘 섞어준다. \n단맛을 좋아한다면 설탕을 조금 더 추가한다.\n5 팬에 우엉과 양념장을 넣고 센불에 조리한다. \n수분이 줄어들고 거품이 커지고 타기 전까지 볶는다. 대략 4분 정도면 됨\n"))

                    add(RecipeDataVo("chick_bean_cabbage_roll",R.drawable.chick_bean_cabbage_roll,"",""))
                    add(RecipeDataVo("chives_tofu_and_dumplings",R.drawable.chives_tofu_and_dumplings,"",""))
                    add(RecipeDataVo("chocolate_chip_cookies",R.drawable.chocolate_chip_cookies,"",""))
                    add(RecipeDataVo("chungmu_style_kimbap",R.drawable.chungmu_style_kimbap,"",""))
                    add(RecipeDataVo("cream_potato_spaghetti",R.drawable.cream_potato_spaghetti,"",""))

                    add(RecipeDataVo("creamy_cabbage",R.drawable.creamy_cabbage,"",""))
                    add(RecipeDataVo("dried_eggplant_lasagna",R.drawable.dried_eggplant_lasagna,"",""))
                    add(RecipeDataVo("eggplant_burger",R.drawable.eggplant_burger,"",""))
                    add(RecipeDataVo("fried_tofu_kimbap",R.drawable.fried_tofu_kimbap,"",""))
                    add(RecipeDataVo("garlic_perilla_oil_festo",R.drawable.garlic_perilla_oil_festo,"",""))
                    add(RecipeDataVo("green_onion_vinaigrette",R.drawable.green_onion_vinaigrette,"",""))
                    add(RecipeDataVo("grilled_bok_choy_natto_salad",R.drawable.grilled_bok_choy_natto_salad,"",""))
                    add(RecipeDataVo("grilled_eggplant_with_miso",R.drawable.grilled_eggplant_with_miso,"",""))
                    add(RecipeDataVo("kale_tofu_ssambap",R.drawable.kale_tofu_ssambap,"",""))
                    add(RecipeDataVo("lotus_root_sesame_salad",R.drawable.lotus_root_sesame_salad,"",""))

                    add(RecipeDataVo("madeleine",R.drawable.madeleine,"",""))
                    add(RecipeDataVo("mallow_soup",R.drawable.mallow_soup,"",""))
                    add(RecipeDataVo("morning_bread",R.drawable.morning_bread,"",""))
                    add(RecipeDataVo("mushroom_gangjeong",R.drawable.mushroom_gangjeong,"",""))
                    add(RecipeDataVo("onion_with_rice",R.drawable.onion_with_rice,"",""))
                    add(RecipeDataVo("perilla_chwinamul_pasta",R.drawable.perilla_chwinamul_pasta,"",""))
                    add(RecipeDataVo("red_cabbage_ragu_pasta",R.drawable.red_cabbage_ragu_pasta,"",""))
                    add(RecipeDataVo("romaine_salad",R.drawable.romaine_salad,"",""))
                    add(RecipeDataVo("salary_boiled_dumplings",R.drawable.salary_boiled_dumplings,"",""))
                    add(RecipeDataVo("sesong_mushroom_cold_raw_fish_soup",R.drawable.sesong_mushroom_cold_raw_fish_soup,"",""))

                    add(RecipeDataVo("shiitake_mushroom_pizza",R.drawable.shiitake_mushroom_pizza,"",""))
                    add(RecipeDataVo("shiitake_mushroom_rice",R.drawable.shiitake_mushroom_rice,"",""))
                    add(RecipeDataVo("spinach_curry",R.drawable.spinach_curry,"",""))
                    add(RecipeDataVo("spinach_udon",R.drawable.spinach_udon,"",""))
                    add(RecipeDataVo("stir_fried_lettuce",R.drawable.stir_fried_lettuce,"",""))
                    add(RecipeDataVo("stir_fried_white_shiitake_mushrooms_and_perilla_seeds",R.drawable.stir_fried_white_shiitake_mushrooms_and_perilla_seeds,"",""))
                    add(RecipeDataVo("tofu_fried_tofu_rice_balls",R.drawable.tofu_fried_tofu_rice_balls,"",""))
                    add(RecipeDataVo("tofu_mayonnaise",R.drawable.tofu_mayonnaise,"",""))
                    add(RecipeDataVo("tofu_noodles",R.drawable.tofu_noodles,"",""))
                    add(RecipeDataVo("vegun_dired_sweet_potato_stratch_sheet",R.drawable.vegun_dired_sweet_potato_stratch_sheet,"",""))

                    add(RecipeDataVo("wholemeal_banna_bread",R.drawable.wholemeal_banna_bread,"",""))
*/

                    Adapter.dataList = datas
                    Adapter.notifyDataSetChanged()
                }

                var IndexList = arrayListOf<Int>()
                // 자정 지나면 레시피 변경
                if(Intent.ACTION_DATE_CHANGED == intent!!.action){

                }

                Adapter.setOnItemClickListener(object : HomeRecipeAdapter.OnItemClickListener{
                    override fun onItemClick(v: View, data: RecipeDataVo, post: Int) {

                    }
                })
            }

            private fun tempData(): ArrayList<RecipeDataVo> {
                var tempData = ArrayList<RecipeDataVo>()



                return tempData
            }
        }
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}
