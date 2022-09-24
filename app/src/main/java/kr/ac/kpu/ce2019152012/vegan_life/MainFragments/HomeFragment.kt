package kr.ac.kpu.ce2019152012.vegan_life.MainFragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kr.ac.kpu.ce2019152012.vegan_life.Adapter.HomeRecipeAdapter
import kr.ac.kpu.ce2019152012.vegan_life.Adapter.RecipeAdapter
import kr.ac.kpu.ce2019152012.vegan_life.DataVo.HomeRecipeDataVo
import kr.ac.kpu.ce2019152012.vegan_life.DataVo.RecipeDataVo
import kr.ac.kpu.ce2019152012.vegan_life.R
import kr.ac.kpu.ce2019152012.vegan_life.databinding.FragmentHomeBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var auth: FirebaseAuth? = null
    private lateinit var db: FirebaseFirestore

    // Adapter 변수 선언
    private lateinit var Adapter: RecipeAdapter

    // gridManager
    private lateinit var Gmanager: GridLayoutManager

    val datas = mutableListOf<RecipeDataVo>()
    val timedatas = mutableListOf<RecipeDataVo>()
    val randnum = mutableSetOf<Int>()

    private var SumCar: Int = 0
    private var SumPro: Int = 0
    private var SumFat: Int = 0

    var basickcal: Int = 0

    // 시간 변수
    var _context: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _context = context
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        FirebaseApp.initializeApp(requireActivity())
        auth = FirebaseAuth.getInstance()

        setup()
        initRecycler()

        db.collection(auth?.currentUser?.email.toString().trim())
            .document("Info")
            .get()
            .addOnSuccessListener {
                binding.homeNickname.text = it["nickname"]?.toString()?.trim() + " 님"
                binding.homeRecipeName.text = it["nickname"].toString().trim() + " 님의 추천 식단"

            }.addOnFailureListener { exception ->
                Log.d("error", "Error getting documents: ", exception)
            }

        // 사용자의 탄단지 설정
        db.collection(auth?.currentUser?.email.toString()).document("Info")
            .get().addOnSuccessListener {
                Log.d("Infor", "기초대사량 : " + it["basickcal"].toString())
                Log.d("Infor", "닉네임 : " + it["nickname"].toString())

                basickcal = it["basiccal"].toString().toDouble().toInt()

                binding.restKcal.text = "오늘의 잔여 칼로리는 ${basickcal}kcal 입니다."
                var cal: Int = (it["basiccal"].toString().toDouble() * (0.5) / 4).toInt()
                var pro: Int = (it["basiccal"].toString().toDouble() * (0.3) / 4).toInt()
                var fat: Int = (it["basiccal"].toString().toDouble() * (0.2) / 9).toInt()

                binding.MaxCal.text = "/" + cal.toString() + "g"
                binding.MaxPro.text = "/" + pro.toString() + "g"
                binding.MaxFat.text = "/" + fat.toString() + "g"
            }

        val current: LocalDate =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDate.now()
            } else {
                TODO("VERSION.SDK_INT < O")
            }
        var date = current.toString()

        // 현재 섭취한 탄단지 설정
        var todayMor: String = date + "아침"
        var todayLun: String = date + "점심"
        var todayEve: String = date + "저녁"
        var todaySna: String = date + "간식"

/*        db.collection(auth?.currentUser?.email.toString()).document(todayMor)
            .get().addOnSuccessListener {
                if(it["Car"].toString().toInt() == null){
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

        binding.textView7.setText(SumCar.toString())
        binding.textView9.setText(SumPro.toString())
        binding.textView12.setText(SumFat.toString())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycler() {
        Adapter = RecipeAdapter()
        Gmanager =
            GridLayoutManager(requireContext(), RawCount, LinearLayoutManager.HORIZONTAL, false)

        binding.rvList.apply {
            layoutManager = Gmanager
            adapter = Adapter
            setHasFixedSize(true)
        }

        datas.apply {
            add(
                RecipeDataVo(
                    "아스파라거스 크림파스타", R.drawable.asparagus_cream_pasta,
                    "파스타 80g\n아스파라거스 100g\n두유 160ml\n캐슈넛가루 1스푼 (생략가능)\n마늘 4알\n양파 \n올리브오일 1스푼\n파스타 삶을 물 1800cc\n소금 2큰술\n후추\n",
                    "1 아스파라거스 손질은 끝 부분만 칼이나 채칼로 살짝 벗겨내준다. \n섬유질이 굵어서 나무같이 씹힐 수 있기 때문임. \n딱히 거부감이 없다면 그대로 먹어도 된다. \n2 아스파라거스 머리 부분을 잡고 채칼로 앞뒤를 쓱 쓱 썰어준다. \n3 다진 마늘, 양파, 올리브오일을 넣고 팬에 볶아주다가 마늘이 익으면 아스파라거스를 넣고 볶는다.\n4 두유 160ml, 캐슈넛 가루, 면수 국자 한스푼을 넣고 익혀주다가 7분 삶은 면을 넣어준다. \n소금, 후추로 간한다. 면수가 들어가서 짭짤할테니 맛을 보고 소금을 추가한다.",
                    5
                )
            )

            add(
                RecipeDataVo(
                    "비트 리조또", R.drawable.beet_risotto,
                    "비트 중 사이즈 1개 (100g)\n보리 100g\n식초 2T\n소금 0.5t\n캐슈넛 30g\n다시마 2조각\n올리브오일 1T (선택)\n",
                    "1. 비트는 깨끗이 씼어서 껍질 채로 40분 이상 푹 삶는다. \n오래 삶아야 부드럽고 잘 갈리고 은은한 단맛이 난다. 덜 익은 비트는 딱딱하고 약간 흙 맛이 난다.\n2 1시간 이상 충분히 불린 보리는 다시마 2조각을 넣고 끓인다. \n물은 보리와 동일한 양을 넣는다. 강불에서 3분 - 불 줄여서 2분 - 3분 뜸들이기. \n냄비에서 했지만 리조또를 만들 팬에서 바로 조리해도 좋다. (타지 않도록 주의할 것!)\n3 불린 캐슈넛은 블렌더에 넣고 갈아준다. 잘 갈리지 않을 땐 두유나 물을 한스푼씩 넣어 갈아본다. 한꺼번에\n 많이 넣지 않고 조금씩 증량하며 갈아주기. 크림같은 질감이 되면 완성\n4 푹 익은 비트는 옥수수같은 냄새가 난다. 껍질을 벗기면 홍시처럼 잘 벗겨진다 껍질 벗기고 적당한 크기로 썰어 블렌더에 갈아준다. \n이 때 식초 2스푼을 함께 넣어준다. 식초가 비트의 맛을 확 끌어올린다. \n잘 안갈리면 물을 조금씩 넣어(스푼으로)보며 갈아준다. 물 한꺼번에 많이 넣지 않기.\n5 팬에 보리 넣고, 다시마 물(보리밥 한 냄비에 물 부어서 씀)을 조금씩 넣어 볶아주다가 소금 반 티스푼을 넣는다. \n간을 보고 적절하게 소금양을 조절한다.\n",
                    5
                )
            )

            add(
                RecipeDataVo(
                    "통양파 김치찜", R.drawable.braised_kimchi_with_onion,
                    "김치 1/4포기 (200g)\n통양파 1개 (200g)\n두부 반모 (200g)\n들기름 1스푼\n된장 0.5스푼\n쌀뜨물 500ml\n",
                    "1 김치, 양파 통으로, 쌀뜨물 300ml 넣는다. (기호에 따라 된장 반스푼 추가)\n2 뚜껑 닫고 팔팔 끓으면 중불로 20분 끓인다.\n3 35분 정도 끓였을 때 들기름 1스푼을 넣는다. \n5분 정도만 더 끓여준다. 그동안 두부를 데친다.\n4 40분 정도 끓이면 완성~~",
                    2
                )
            )

            add(
                RecipeDataVo(
                    "꽈리꼬추조림", R.drawable.braised_short_necked_red_pepper_paste,
                    "꽈리고추 100g\n감자 작은거 2개\n가지 1/2개\n마늘 4개\n간장 1.5T\n설탕 1T\n물 3T\n들기름 1T\n참깨\n",
                    "1 꽈리고추는 손가락 마디 길이로 잘라준다. 끝부분이 매운지 살짝 냄새만 맡았는데도 매운향이 난다.\n2 감자, 가지도 비슷한 길이로 썰어준다. 마늘은 적당히 가로로 두툼하게 썰기\n3 양념장을 미리 준비한다.- 진간장 1.5스푼, 설탕 1스푼, 물 3스푼 넣어 준비한다. 들기름은 마지막에 둘러줄 것이니 제외!\n4 단단한 순서로 익힌다. 먼저 감자익히기. 기름 대신에 물 부어서 볶는다\n5 감자가 약간 불투명하게 색이 바뀌면 가지, 마늘, 양념장을 넣고 볶는다.\n6 꽈리고추 매우니까 푹 익혀야 할 것 같은 느낌이라 조금 더 물로 볶아주다가(사실 익혀도 매운건 마찬가지..) 마지막에 들기름 1스푼, 참깨 뿌려주었다.\n",
                    2
                )
            )

            add(
                RecipeDataVo(
                    "우엉잡채", R.drawable.burdock_japchae,
                    "우엉 150g\n초록 피망 100g\n파프리카 빨간색 100g\n양파 1/4\n간장 2스푼\n조청 1스푼\n참기름 1스푼\n",
                    "1 우엉의 특유의 향과 맛은 껍질에 있기 때문에 되도록 껍질을 벗기지 않도록 깨끗하게 씻는다. 손바닥 길이 정도로 얇게 채로 썰어준다. 썰고 나서는 바로 식초가 들어간 물에 담궈준다. 갈변을 막기 위해..라고 함\n2 물에 소금과 식초를 넣어 팔팔 끓으면 1분동안 우엉을 데쳐낸다. 건져서 찬물에 담궈둔다. 이렇게 하면 우엉에 탄력도 생기고 조리 시간도 단축된다.\n3 청피망, 빨간 파프리카와 양파를 얇게 채썰어준다.\n4 양념장 만들기. 진간장2스푼, 조청1스푼, 물 6스푼 넣고 잘 섞어준다. 단맛을 좋아한다면 설탕을 조금 더 추가한다.\n5 팬에 우엉과 양념장을 넣고 센불에 조리한다. 수분이 줄어들고 거품이 커지고 타기 전까지 볶는다. 대략 4분 정도면 됨\n",
                    2
                )
            )

            add(
                RecipeDataVo(
                    "병아리콩 양배추롤", R.drawable.chick_bean_cabbage_roll,
                    "양배추 겉잎 6~7장\n병아리콩 100g\n양파 1/4\n파프리카 1/4\n소금 0.5티스푼, 후추\n토마토 퓨레 250ml (반통)\n마늘 3~4개\n타임, 바질 (선택)\n",
                    "1 병아리콩은 충분히 삶아준다. 양배추롤을 만들때도 익히기는 하지만 콩 종류는 완전히 익혀야 안전하다. 눌렀을 때 부스러질 정도면 OK. 익힌 후 물에서 건져 수분을 제거한다.\n2 양배추는 삶아준다. 전자레인지에 겉잎만 떼어 뚜껑 덮고 돌려주면 더 간편하다.\n3 양파, 파프리카, 마늘은 잘게 다져준다.\n4 블렌더에 병아리콩, 마늘을 넣고 살짝 갈아준다. 대충 으스러질 정도면 되고, 포크나 주걱으로 눌러주어도 상관없다.\n5 병아리콩, 다진 마늘과 파프리카, 양파, 소금 0.5티스푼, 후추를 넣어 잘 섞어준다.\n6 양배추 잎에 병아리콩 뭉친것을 올려 잘 말아준다. 딱딱한 줄기 부분은 살짝 제거하고 토마토 소스에 졸일 때 같이 넣어줌\n7 팬에 다진 양파, 양배추 줄기 자른것, 물을 넣고 살짝 익히다가 토마토 소스 반통(250ml)를 넣는다. 올리브 오일을 넣고 싶다면 이 때 재료를 넣어 익힌 후 토마토 소스를 넣으면 됨!\n8 양배추 롤 접힌 면을 바닥으로 두고 토마토 소스와 물 반컵을 부어준다. 10분 정도 소스를 끼얹어 주며 졸여준다.\n",
                    5
                )
            )

            add(
                RecipeDataVo(
                    "부추 두부 만두", R.drawable.chives_tofu_and_dumplings,
                    "부추 100g\n두부 200g (1/2모)\n양배추 약간\n소금\n식용유\n중력분 100g\n미지근한 물 종이컵 1/3\n",
                    "1. 밀가루 100g과, 소금 1꼬집 정도 넣고 미지근한 물을 섞어 반죽을 시작한다. 물은 조금씩 추가하면서 매끄러워질 때 까지 반죽한다\n2. 두부 수분 제거하기 아래 같은 고운 채망(다이소에서 구입)을 준비하고 두부 반모를 올려서 손으로 으깨주면서 물기를 뺀다.\n3. 만두소 만들기 두부 넣고, 소금 3꼬집 넣어 으깨주기\n4. 만두피 만들시간 작업대를 소독하고 덧밀가루 뿌려서 반죽을 길게 말아준다.\n5 만두피에 소 넣어주기. 작은 한스푼정도 소를 넣고 만두피의 절반 부분에 물을 묻혀 닫아준다. 원하는 모양대로 만들면 됨\n",
                    3
                )
            )

            add(
                RecipeDataVo(
                    "비건 초코칩 쿠키", R.drawable.chocolate_chip_cookies,
                    "<액체재료>\n오일 1/3 컵\n물 1/3 컵\n설탕 1/2 컵\n<가루재료>\n중력분 1 컵\n베이킹파우더 1/2 티스푼\n베이킹소다 1/2 티스푼\n소금 1/4 티스푼\n<토핑>\n초콜릿칩 혹은 초콜릿\n아몬드슬라이스(옵션)\n",
                    "1. 오일에 설탕을 풀어 잘 녹여주세요. 완전히 녹일 필요 없어요. 골고루 섞일 정도로만 풀어주세요.\n2. 그 다음 물을 넣어 작 섞어준다. 이 때 설탕이 더 많이 녹는다.\n3. 오일, 설탕, 물이 고루 잘 섞는다. 이렇게 모든 액체재료들을 먼저 섞어주시는 이유는 설탕이 반죽 전체에 고루 퍼질 수 있게 하기 위함.\n4. 잘 섞인 액체재료에 가루재료를 모두 붓고 빠르게 저어서 섞어주세요.\n5. 이정도 묽기와 되기가 나왔으면, 제 초코칩쿠키 기본 반죽 완성.\n6 토핑은 반죽에 넣기 전에 적당한 크기로 조각 내주세요 잘게 자른 토핑들을 반죽에 넣어 실리콘주걱(혹은 그냥 밥주걱이나 숟가락도 괜찮아요.)으로 골고루 섞어주세요.\n7. 이렇게 초코칩쿠키 반죽 완성.\n8. 완성된 반죽은 래핑해서 20분 냉장고에서 방치해둔다.\n9. 20분 후, 숟가락 두 개와 쿠키팬에 종이호일이나 테프론시트를 깔아서 준비해둔다.\n10. 숟가락 두 개를 사용해서 적당한 크기와 간격으로 쿠키모양을 잡아준다.\n11. 쿠키형태를 모두 잡아주었으면, 이대로 다시 한 번 냉장고에 5분만 휴지시킨다. 이 때 오븐을 180도로 예열해준다.\n",
                    6
                )
            )

            add(
                RecipeDataVo(
                    "비건충무김밥", R.drawable.chungmu_style_kimbap,
                    "풋마늘 2-3개 (100g) \n새송이버섯 한컵 (100g)\n김밥김 1장\n밥 한공기 (200g)\n참기름\n참깨\n[소스]\n고추가루 3스푼, 간장 2스푼, 설탕 2스푼, 식초 1스푼, 물 1스푼, 참기름과 참깨 약간",
                    "1 새송이버섯은 찜기에 5분정도 쪄준다. 물에 익혀도 좋지만 이 방법이 훨씬 채즙도 풍부하고 쫄깃해서 맛있다. 찬물에 담궈서 탱탱하게\n2 풋마늘은 적당한 크기로 자르고 반으로 잘라준다. 찜기에 1-2분 정도 찌고 물기를 꽉 짜준다.\\n3 풋마늘도 1-2분 정도 살짝 쪄준 후 찬물에 헹궈 물기를 꼭 짜준다.\n4 새송이버섯과 풋마늘을 넣고 양념장(고추가루3, 진간장2, 설탕2, 식초1, 물1)을 넣고 잘 섞어준다. 참깨와 참기름으로 마무리하면 무침 완성!!\n5 김밥. 밥은 따뜻하게 준비하고 참기름을 넣어 잘 섞어준다.\n6 김밥김은 반으로 나누고 밥 한공기를 나누어 말아준다. 4등분으로 썰어주기\n",
                    7
                )
            )

            add(
                RecipeDataVo(
                    "들기름 취나물 파스타", R.drawable.cream_potato_spaghetti,
                    "취나물 한바가지 (100g)\n스파게티면 80g\n마늘 5알\n들기름 2스푼\n연두 1스푼 (없으면 진간장 반스푼)\n면 삶을 물: 소금 1스푼, 물 1000cc\n",
                    "1 물 1000cc에 소금 1스푼을 넣어 끓인다. 물이 끓으면 면을 넣고 7분간 익힌다.\n2 취나물은 5등분으로 칼로 썰어준다. 마늘도 가로로 썰어준다.\n3 팬에 마늘과 면수 1스푼, 들기름 1스푼을 넣고 *약불*에서 익히다가 취나물을 넣고 섞어준다.\\n4 면을 건져서 넣고 면수를 반국자 정도 넣어 익힌다. 이 때 중불로 올리고 연두나 진간장을 넣어준다. 면수로 이미 간이 되어 있을 테니 꼭 간을 보고 싱거우면 1스푼, 짭짤하다면 반스푼정도만 넣는다.\n5 1분 정도만 더 볶다가 불을 끄고 들기름 1스푼을 더 넣어준다. 후추도 살짝 뿌려도 주었다.\n",
                    5
                )
            )

            add(
                RecipeDataVo(
                    "적양배추 라구 파스타", R.drawable.creamy_cabbage,
                    "\n적양배추 1/2통 (250g)\n감자 2개 (150g)\n마늘 6-7개 (25g)\n토마토퓨레 100ml\n레드와인 50ml\n물 200ml\n올리브오일 1스푼\n연두 1스푼 (or 간장 1스푼)\n소금 2꼬집\n설탕 1 티스푼(선택)\n향신료(로즈마리, 타임, 바질 등..)\n후추",
                    "1 적양배추 썰어주기. 나는 보통 심지까지 요리해서 같이 다졌다.\n2 깊은 냄비에 올리브유 1스푼과 다진마늘을 볶아준다. 양파를 다져서 넣는다면 이 때 같이 볶아준다.\n3 마늘이 익은 냄새가 나면 적양배추, 감자, 토마토퓨레, 연두1스푼(혹은 간장1스푼), 레드와인, 물 100ml 넣고 끓여준다. 센 불에서 1-2분 정도 뒤적여 준다. 보글보글 끓으면 불을 줄이고 냄비 뚜껑을 닫고 15분 동안 푹 끓인다.\n4 15분 뒤. 뚜껑을 열어보니 수분이 많이 줄어들었고 보라색이 많이 빠지면서 전체적으로 어두워졌다. 수분이 없으면 눌러붙어 타게 되니까 물을 추가로 100ml 넣고 끓인다. 이 때 향신료(타임, 로즈마리, 바질, 월계수 잎 등..)를 넣어준다. 나는 타임과 스위트 바질을 넣음.\n5 10분 뒤, 간을 보고 소금으로 간을 맞춘다. 나는 소금 2꼬집, 설탕 1티스푼 넣었다. 싱거우면 소금을 조금씩 더 넣어가며 맛을 보며 추가하기! 불 끄고 후추 뿌려주면 적양배추 라구소스는 완성!\n6 소금 넣고 면을 삶아준다. 그리고 캐슈넛은 믹서기에 넣어 가루로 만든다. 캐슈넛, 아몬드를 섞었다. 둘 중에 하나만 해도 상관없다.! 견과류 가루를 뿌리면 고소하고 달달해진다.\n7 면 올리고 + 적양배추 라구 소스 한스푼 듬뿍 올리면 완성!\n",
                    5
                )
            )

            add(
                RecipeDataVo(
                    "말린가지 라자냐", R.drawable.dried_eggplant_lasagna,
                    "라자냐 면 5장\n말린가지 한줌(20g)\n양파 1/2\n토마토 1개\n토마토소스 1컵(200ml)\n두유치즈 & 캐슈넛치즈 100g\n올리브오일\n아몬드 가루\n",
                    "1 말린 가지는 따뜻한 물에 미리 불려 놓는다.\n2 밀가루 100g, 소금, 미지근한 물을 넣어 반죽하다가 넓게 펴서 라자냐 그릇만큼 크기로 잘라준다. 소금물에 한장씩 익히고 말려준다.\n3 양파, 토마토, 가지 다져놓기\n4 올리브유 두르고 양파 먼저 볶기\n5 다음 가지 볶기, 소금 약간\n6 토마토, 양파 넣어 볶아주다가 토마토 소스 넣고 끓이기\n7 아몬드 가루를 추가했다. 없으면 패쓰!\n8 라자냐 용기에 면 깔고  토마토 소스 - 치즈 순서로 쌓아준다.\n9 에어프라이어로~170도에서 20분.\n",
                    5
                )
            )

            add(
                RecipeDataVo(
                    "가지패티버거", R.drawable.eggplant_burger,
                    "[가지 패티]\n가지 90g (작은것 1개)\n삶은 병아리콩 15g\n빵가루 20g\n마늘 1알\n소금 3꼬집\n[버거 재료]\n버거 빵\n토마토 슬라이스 1개\n양파 슬라이스 2개\n양상추 1~2장\n올리브유\n두부마요네즈 or 칠리소스 or 허니머스타드(홀그레인 머스타드) or 토마토케챱\n",
                    "1 가지를 깍둑썰기 하고 찜기에 2분동안 쪄준다.\n2 양파는 2-3cm 두께로 가로로 썰고 후라이팬에 앞 뒤로 갈색이 될 때까지 노릇하게 구워준다. \n3 익은 가지는 물에 살짝 데쳐서 식히고 수분을 제거한다. 병아리콩이랑 같이 으깨준다. 병아리콩은 푹 삶아야 잘 으깨진다.\n4 병아리콩, 가지 으깬 것에 빵가루, 소금 3꼬집, 후추, 다진마늘을 넣는다. 맛을 보고 싱거우면 소금과 후추를 추가한다. 잘 섞어주고 동그랗게(빵 크기에 맞춰) 뭉쳐준다.\n5 가지패티를 올리브유를 두른 팬에 앞 뒤로 노릇하게 구워준다. 남는 자리에 빵도 살짝 굽기.\n6 빵 위에 두유 마요네즈 바르고 > 양상추 올림 > 가지 패티 > 양파 > 토마토 > 소스 순서로 올려주면 끝!\n",
                    5
                )
            )

            add(
                RecipeDataVo(
                    "유부조림김밥", R.drawable.fried_tofu_kimbap,
                    "김밥김 1장\n잡곡밥 140g\n슬라이스 유브 50g\n간장 1 티스푼 + 물1T + 설탕 1티스푼\n부추 50g\n우엉조림 (생략가능)\n단무지 (생략가능)\n참기름\n참깨\n",
                    "1 유부는 끓는 물에 2분동안 데친다. 꺼내서 물에 헹궈 물기를 제거해준다. 너무 꾹 짜내지는 않고 적당히..!\n2 팬에 유부와 간장 1티스푼, 물 1스푼, 설탕 1티스푼 넣고 볶아준다. 수분이 날아갈 정도로만 졸여준다. 수분이 있으면 김밥 쌀 때 다 터짐\n3 부추는 찜기에 2분동안 데치고 찬물에 살짝 헹궈서 물기를 쭉 빼낸다.\n4 밥은 따뜻하게 데워  따로 소금간은 하지 않고 깨, 참기름만 넣어 섞어준다.\n5 김밥김 밥 올리고 - 깻잎 - 유부 - 부추 + 기타 재료들을 넣고 말아준다. \n",
                    7
                )
            )

            add(
                RecipeDataVo(
                    "마늘종 들기름 페스토", R.drawable.garlic_perilla_oil_festo,
                    "마늘종 70g\n견과류 50g\n들기름 30g\n두유 30ml\n소금 2g (1티스푼)\n",
                    "1 마늘종은 끓는 물에 30초 정도 데쳐서 꺼낸다. 찬물에 헹궈 식혀준다.\n2 블렌더에 마늘종, 견과류, 들기름, 두유, 소금 넣고 갈아준다. 처음에는 저속으로 갈면서 중간 중간 섞어주고, 고속으로 곱게 갈아준다.\n3 생 마늘종의 알싸한 맛도 주고 싶어서 생으로 송송 썰어 넣어주었다. 유리병에 담으면 완성!\n",
                    5
                )
            )

            add(
                RecipeDataVo(
                    "대파 비네그레트", R.drawable.green_onion_vinaigrette,
                    "대파 300g\n레몬즙 1스푼\n올리브유 3스푼\n화이트 와인 비니거(또는 사과식초) 1스푼\n소금 0.5스푼\n설탕 1스푼\n후추\n머스타드\n다진마늘, 다진양파 약간(없으면 패스)\n",
                    "1 대파 썰기. 보통 대파는 뿌리 가까운 흰 부분과 중간 부분만 사용하지만 나는 초록 부분도 함께 사용했다.\n2 팬에 올리브유를 두르고 대파를 구워줍니다.\n3 비네그레이트 드레싱 만들기 올리브유 3스푼, 화이트비니거 1스푼, 레몬즙 1스푼, 소금 0.5스푼, 설탕 1스푼, 후추, 홀그레인 머스터드를 넣고 잘 섞어준다. 다진마늘 양파 없으면 생략 가능\n4 대파 위에 드레싱 끼얹어 주고, 냉장고에서 30분 이상 보관한다.\n",
                    7
                )
            )

            add(
                RecipeDataVo(
                    "청경채구이 나또샐러드", R.drawable.grilled_bok_choy_natto_salad,
                    "청경채 100g (3~4개)\n오일, 소금\n나또\n간장, 겨자\n",
                    "1 청경채는 물에 씻어서 준비 아래 줄기 부분을 포함하여 4등분으로 세로로 잘라준다.\n2 오븐에 들어갈 준비 올리브오일과 소금 살짝. 나또랑 먹으면 간장으로 간이 되어 있으니 소금은 생략해도 좋다.\n3 오븐 170도 예열 5분 + 14분 동안 구워준다. 에어프라이어도 170도 동일\n4 남는 시간동안에는 나또를 준비한다.\n5 겨자소스를 넣어준다. 간장은 티스푼으로 1스푼\n6 청경채 위에 나또 올리기",
                    4
                )
            )

            add(
                RecipeDataVo(
                    "가지된장구이", R.drawable.grilled_eggplant_with_miso,
                    "가지 1개\n된장 1/2스푼\n설탕 1/2스푼\n다진 양파, 다진 파 (없으면 패스)\n물 반컵\n깨\n",
                    "1 가지는 길게 세로로 썬다. 기름과 양념이 잘 베일 수 있도록 앞뒷면에 X자로 칼집을 내준다.\n2 그동안 양념장 만들기 된장 반스푼, 물 한스푼, 설탕 반스푼, 다진 양파와 파를 넣고 섞어준다.\n3 구워진 가지 위로 섞어둔 양념장을 바르고 물 반컵을 붓고 뚜껑 닫고 5분\n",
                    3
                )
            )

            add(
                RecipeDataVo(
                    "케일 두부 쌈밥", R.drawable.kale_tofu_ssambap,
                    "\n케일 7~9장 (100g)\n밥한공기 (120g)\n두부 1/4 (100g)\n된장 반스푼\n참깨\n참기름\n버섯 (없으면 생략)\n",
                    "1 케일을 씻어 물기 털고 준비\n2 케일 데치는 법 물이 끓을 때 쯤 15초 정도 데쳐준다. 줄기 - 앞면 - 뒷면 5초씩 세고 건져내기 찬물에 헹궈 물기 살짝 빼놓기 케일은 열에 영양소가 손상되니 금방 건진다.\n3 두부 1/4, 된장 반스푼, 참기름, 참깨 갈은것, 대파 다진것을 넣고 섞어준다. 두부쌈장 완성!\n4 나는 추가로 새송이 버섯을 살짝 구워 추가했다. 어떤 버섯이든 OK, 없으면 패스\n5 케일을 펼치고(줄기는 잘라준다.)  끝부분에 밥 반, 두부쌈장 반, 새송이버섯 넣고 말아준다한번 말고 양쪽 끝을 접고, 나머지 말아주면 잘 말린다.\n",
                    7
                )
            )

            add(
                RecipeDataVo(
                    "연근 참깨 샐러드", R.drawable.lotus_root_sesame_salad,
                    "연근 1개 (대략 100g)\n두부마요네즈 2스푼\n참깨 2스푼\n소금\n",
                    "1 손질된 연근을 물에 잠깐 담궈둔다. (생연근일 경우 흙을 깨끗이 씻어서 얇게 슬라이스 한다.)식초를 약간 넣으면 떫은 맛을 제거해준다.\n2 끓는 물에 2분정도만 잠깐 넣고 꺼내어 찬물로 식혀준다. 끓일 때 소금 약간 참깨 2스푼 넣기 갈아주고 두부마요네즈에 섞기\n3 연근, 참깨 두부마요네즈 소스를 섞어낸다.싱거우면 소금 약간 추가하기\n",
                    7
                )
            )

            add(
                RecipeDataVo(
                    "블루레몬 마들렌", R.drawable.madeleine,
                    "<액체재료>\n두유 1/3 컵\n레몬즙 3 티스푼\n설탕 1/3 컵\n오일 1/3 컵\n아가베시럽 1 스푼\n<가루재료>\n박력분 1/2 컵\n중력분 1/2 컵\n베이킹파우더 1티스푼\n소금 한꼬집\n",
                    "1. 먼저 두유에 레몬즙을 가볍게 섞어 잠시 그대로 둔다.\n2. 약간 엉긴 두유+레몬즙 에 설탕을 넣어 잘 섞어준다.\n3. 설탕이 섞인 액체류에 오일을 넣어주세요. \n4. 아가베 시럽을 섞어준다. 소량이어서 설탕만 넣을 때와 큰 차이가 없을 수도 있지만, 조금 더 묵직한 맛과 식감을 주기 위함.\n5. 모든 가루류(밀가루 두 종류, 베이킹파우더, 소금)를 체에 쳐서 액체류 위에 고르게 뿌려주고, 빠르게 섞어준다.\n6. 이 상태로 랩핑해서 냉장고에 10~20분 정도 휴지시켜준다.\n7. 반죽을 꺼낼 때쯤, 마들렌 틀을 오일로 코팅해준다. 오일은 얇게 아주 고르게 칠해준다. 이 때 오븐을 220도로 예열해준다.\n8. 고르게 코팅된 팬에 숟가락 두 개를 사용해 반죽을 각 구에 약 80~90% 사이로 채워준다. 보통 짤주머니를 사용해서 쭈욱 짜준다.\n220도로 예열된 오븐에 넣어 3분 구워주고, 그 상태에서 200도로 낮춰서 7분 더 구워준다. 팬을 오븐에서 꺼내지 말고, 그대로 온도만 낮춰준다\n",
                    6
                )
            )

            add(
                RecipeDataVo(
                    "감자 아욱된장국", R.drawable.mallow_soup,
                    "아욱 150g\n감자 큰거 1개 (250g)\n대파 1/2\n다진마늘 1큰술\n된장 3큰술\n연두 1큰술\n",
                    "1 아욱은 딱딱한 줄기는 제거하고 잎 부분 위주로 다듬는다. 물에 2-3번 흙없이 깨끗하게 씻어준다. 아욱 잎 크기가 작은 것들은 특히나 줄기가 얇고 딱딱해서 자른다.\n2 마늘은 잘게 다지고, 감자는 얇게 썬다. 대파는 어슷썰기\n3 물 1L에 된장 1스푼(30g정도) 넣고 끓인다. 다진마늘, 대파도 함께 넣어 끓인다.\n4 아욱을 넣고 뚜껑닫고 10분동안 푹 끓인다\n5 연두 1스푼(or 간장)을 넣고 10분 동안 약한 불에서 서서히 끓인다. 아욱이 부드럽게 푹 익으면 완성",
                    2
                )
            )

            add(
                RecipeDataVo(
                    "비건 식빵, 모닝빵", R.drawable.morning_bread,
                    "<액체재료>\n따뜻한 물(두유) 3/4 컵\n(끓인 물 1/4 컵 + 두유 2/4 컵)\n설탕 3스푼\n오일 2스푼\n<가루재료>\n드라이 이스트 2티스푼\n밀가루(강력 혹은 중력) 2컵\n소금 1티스푼\n",
                    "1. 뜨거운 물과 차가운 물(혹은 두유)를 1:2 정도로 섞어서 이스트가 발효되기 좋은 따뜻한 온도로 만들고, 설탕을 넣어 휙휙 저어준다.\n2. 그 위에 이스트를 휘리릭 뿌려주고 잘 섞어 5분~10분 정도 실온에 둘 거예요.\n3. 5분~10분 정도 지나면 이렇게 이스트가 활성화된 것을 볼 수 있어요.\n4. 오일을 넣어 휘리릭 섞어주세요.\n5. 마지막으로 밀가루를 먼저 넣고 그 위에 소금을 뿌려주세요! 소금은 잘못하면 이스트를 죽일 수 있으니 되도록 직접 닿지 않게 하는 게 좋답니다. 그리고는 아래 영상처럼 휙휙휙 포크로 저어 섞어주세요.\n6. 반죽에도 밀가루 솔솔 뿌리고 손에도 밀가루 묻혀서 5분 정도만 치대주세요. 손에 엄청 붙는 게 맞아요~\n8. 5분 치댄 후에 잠시 보울을 면으로 덮어 15분 휴지시켜 줄게요.\n9. 15분 휴지 후 다시 반죽을 치대줄 건데요, 이번에는 10분 정도 성실하게 해주셔야 해요. 밀가루를 조금씩 넣어가며 치대주세요.\n10. 10분 정도 열심히 치대주니 제법 반죽의 모양이 뭉쳐서 나온다\n11. 드디어 첫 번째 발효 들어갑니다~ 미온수에 적신 면으로 보울을 덮어 실온에 두거나 발효모드 오븐 안에서 1시간 정도 발효시켜준다. 온도에 따라 발효속도에 차이가 있을 수 있으니 반죽이 2배 정도 부풀때까지 잘 지켜봐준다!\n12. 이제 이 반죽을 도마나 깔끔하고 평평한 탁자 위에 부어 3등분 해준다.\n13. 3등분한 덩어리들을 잘 굴려서 이렇게 동글동글하게 만들어 준다.\n14. 이대로 면보를 다시 덮어서 15분간 휴지시켜줄게요. 이 다음 단계부터는 원하는 모양과 취향에 맞는 속을 채워주시면 되는데요\n15. 15분 휴지 후, 한 덩어리씩 이렇게 사각형 모양으로 롤러로 적당히 밀어주세요.\n16. 그리고는 이렇게 취향에 맞는 필링을 깔아주고 3등분으로 접어주세요. 플레인 식빵만들기는 여기에서 속을 생략하고 그냥 3등분해서 접어주시면 돼요!\n17. 3등분으로 접은 후, 돌돌 잘 눌러 말아서 끝부분을 꼭꼭 눌러 잘 마감해주세요. 그리고는 미리 오일칠을 해 둔 식빵 틀 한쪽에 잘 밀어 넣어주세요.\n18. 세 덩어리를 모두 완성해 틀에 넣은 후 부드럽게 눌러주고, 면보로 덮어 2차 발효 들어가겠습니다! 발효조건은 1차와 동일해요.\n19. 2차 발효까지 완료되었습니다! 반죽 윗면에 두유를 솔솔 발라주고 180도로 예열된 오븐에 넣어 40~45분간 구워주세요.\n",
                    6
                )
            )

            add(
                RecipeDataVo(
                    "버섯강정", R.drawable.mushroom_gangjeong,
                    "양송이버섯 100g\n새송이버섯 150g\n캐슈넛 30g\n밀가루 100g\n감자전분 50g\n소금 0.5 tsp\n베이킹소다 1tsp (생략가능)\n강황가루 1tsp (생략가능)\n두유 3T\n후추\n",
                    "1 버섯을 비슷한 크기로 숭덩숭덩 잘라준다. 두유 3스푼 넣어 잘 섞어준다. 튀김 반죽이 잘 묻게 하려고 뿌려주는 것임\n2 반죽 가루류(통밀가루, 전분, 강황)를 계량하여 절반은 넓은 접시에 따로 두고, 절반은 물을 넣어 섞어준다. 물은 가루와 비슷한 용량을 넣으면 된다. 대략 반컵~한컵 정도. 반컵 붓고 반죽이 되다 싶으면 조금씩 더 붓기\n3 두유 묻힌 버섯에 만들어둔 반죽을 넣어 버무려 준다. 이때 소금 반스푼, 후추도 같이 넣어 섞어준다. 하나씩 떠서 가루를 잘 묻혀준다. 가루 잘 털어줘야 함!! 기름에 튀기지 않아서 가루 뭉친 것은 나중에 하얗게 그대로 된다\n4 오일을 살짝 뿌려주고 에어프라이어 190도에서 15분 돌려준다. 10분 돌린 후에 상태를 보고 뒤집어 주고 추가로 5분 돌리면 땡!\n5 고추장1, 케찹3, 간장0.5, 고춧가루0.5, 물엿1, 물5, 깨를 잘 섞어서 팬에 넣고 바글바글 끓여서 거품이 나고 졸아들면 불을 끈다. 에어프라이에서 꺼낸 버섯 섞어주면 완성!\n",
                    2
                )
            )

            add(
                RecipeDataVo(
                    "초간단 양파덮밥", R.drawable.onion_with_rice,
                    "양파 1개 (200g)\n밥 1공기 (150g)\n김밥김 1장\n대파\n물 2T\n간장 1T \n들기름 1T\n참깨\n전분가루 조금 (없어도 됨)\n1T = 10ml, 1t=5ml 밥숱가락 기준\n",
                    "1 양파는 1-2cm 두깨로 썰어준다. 대파도 비슷한 두께로 썰어준다.\n2 김밥김은 잘게 가위로 자르고 프라이팬에 살짝 구워 습기를 날려준다. 조미김을 써도 되지만 맨김이 훨씬 맛있음\n3 팬에 물 두스푼 정도 넣고 양파를 익혀준다. 마지막에 들기름을 둘러줄 것이기 때문에 따로 식용유를 넣지는 않았다.\n4 물 2T, 간장 1T, 들기름 1T를 넣고 잘 섞어준 파와 함께 양파 익은 것에 넣고 볶아준다.\n5 전문물 1~2스푼 정도 농도를 봐가며 추가해준다. 후추, 참깨를 추가로 뿌려주었다. \n6 밥 위에 얹으면 완성",
                    7
                )
            )

            add(
                RecipeDataVo(
                    "시금치 우동", R.drawable.perilla_chwinamul_pasta,
                    "다시마 쯔유 재료\n건다시마 10조각\n표고버섯 1개\n물 500ml\n간장 60ml\n설탕 30g\n시금치 우동 재료\n시금치100g\n우동면\n다시마 쯔유 50ml\n유자청 1tsp\n",
                    "1  다시마, 표고, 물을 넣고 끓인다. 5분 정도 중불에 끓이고 불을 끈 상태로 1시간 이상 방치한다.\n2 건더기를 건져내고 간장50ml, 설탕 30g을 넣고 한번 더 끓인다. 식힌 후에 유리병에 넣고 냉장고에 보관한다. \n다시마 맛간장 만들어 두면 볶음, 덮밥, 조림, 우동, 소면 등등 다양한 요리에 활용 가능하다.\n3 면부터 삶아준다. 끓는 물에 우동면 넣고 잘 풀어준다. 2분 정도 삶고 꺼내서 찬물에 헹구고 얼음 물에 잠시 담궈둔다. 이 때 레몬조각이나 유자청을 조금 넣어주면 향이 면에 잘 베인다.\n4 시금치는 끓는 물에 30~60초 정도 삶아준다. 꺼내서 찬물에 헹구고 물기를 꽉 짜낸다. 3등분으로 썰어준다.\\n5 얼음 2-3조각 넣고 면 올리고, 시금치 올리고, 하귤올려주고, 다시마 쯔유 50ml 넣어주면 완성!!\n",
                    4
                )
            )

            add(
                RecipeDataVo(
                    "적양배추 라구 파스타", R.drawable.red_cabbage_ragu_pasta,
                    "\n적양배추 1/2통 (250g)\n감자 2개 (150g)\n마늘 6-7개 (25g)\n토마토퓨레 100ml\n레드와인 50ml\n물 200ml\n올리브오일 1스푼\n연두 1스푼 (or 간장 1스푼)\n소금 2꼬집\n설탕 1 티스푼(선택)\n향신료(로즈마리, 타임, 바질 등..)\n후추",
                    "1 적양배추 썰어주기. 나는 보통 심지까지 요리해서 같이 다진다.\n2 깊은 냄비에 올리브유 1스푼과 다진마늘을 볶아준다. 양파를 다져서 넣는다면 이 때 같이 볶아준다.\n3 마늘이 익은 냄새가 나면 적양배추, 감자, 토마토퓨레, 연두1스푼(혹은 간장1스푼), 레드와인, 물 100ml 넣고 끓여준다. 센 불에서 1-2분 정도 뒤적여 준다. 보글보글 끓으면 불을 줄이고 냄비 뚜껑을 닫고 15분 동안 푹 끓인다.\n4 15분 뒤. 뚜껑을 열어보니 수분이 많이 줄어들었고 보라색이 많이 빠지면서 전체적으로 어두워졌다. 수분이 없으면 눌러붙어 타게 되니까 물을 추가로 100ml 넣고 끓인다. 이 때 향신료(타임, 로즈마리, 바질, 월계수 잎 등..)를 넣어준다. 나는 타임과 스위트 바질을 넣음.\n5 10분 뒤, 간을 보고 소금으로 간을 맞춘다. 나는 소금 2꼬집, 설탕 1티스푼 넣었다. 싱거우면 소금을 조금씩 더 넣어가며 맛을 보며 추가하기! 불 끄고 후추 뿌려주면 적양배추 라구소스는 완성!\n6 소금 넣고 면을 삶아준다. 그리고 캐슈넛은 믹서기에 넣어 가루로 만든다. 캐슈넛, 아몬드를 섞었다.  견과류 가루를 뿌리면 고소하고 달달해진다.\n7 면 올리고 + 적양배추 라구 소스 한스푼 듬뿍 올리면 완성!\n",
                    5
                )
            )

            add(
                RecipeDataVo(
                    "초간단 로메인 샐러드", R.drawable.romaine_salad,
                    "로메인 1통 (150g)\n두부마요네즈 1스푼\n홀그레인 머스터드 1tsp\n레몬즙 1tsp\n다진마늘 1tsp\n[토핑]\n아몬드 가루 1스푼\n빵 조각\n",
                    "1 두부마요네즈 듬뿍 1스푼에 홀그레인 머스터드, 뉴트리셔널 이스트, 레몬즙, 다진마늘을 섞어 드레싱을 만든다.\n2 빵은 큐브 모양으로 자르고 팬에 살짝 구워준다.\n3 로메인 위에 드레싱, 빵 올리고 아몬드 가루를 뿌리면 완성! (초간단..)\n",
                    7
                )
            )

            add(
                RecipeDataVo(
                    "중국식 셀러리 물만두", R.drawable.salary_boiled_dumplings,
                    "셀러리 70g\n부추 70g\n두부 100g\n만두피 20개\n만두소 양념: 식용유 1스푼, 간장 1스푼, 소금 2꼬집, 참기름 1스푼\n간장 양념: 다진마늘 1스푼, 식초 1스푼, 간장 1스푼\n",
                    "1 두부는 물기를 제거하고 으깨어 준다.\n2 셀러리는 끓는 물에 30초 정도 넣었다가 뺀다. 잎부터 꺼내고 줄기를 나중에 꺼내면 적절하게 아삭아삭함. 잘게 다져주기\n3 부추는 잘게 송송 썰어주기\n4 두부에 식용유 1스푼, 부추, 셀러리를 넣고 젓가락으로 한방향으로 저어준다. 그리고 간장 1스푼, 소금 조금 넣고 다시 저어주기\n5 만두피에 한스푼씩 넣어 끝 부분을 물묻혀서 원하는 모양으로.\n6 끓는 물에 만두를 넣어주고 둥둥 떠오르면 찬물을 한컵 부어준다. 이렇게 2-3번 반복\n7 양념장 만들기! 다진마늘, 간장, 식초를 1:1:1비율로. 다진마늘 진짜 꼭 꼭 넣기\n",
                    3
                )
            )

            add(
                RecipeDataVo(
                    "새송이버섯 물회", R.drawable.sesong_mushroom_cold_raw_fish_soup,
                    "새송이버섯 1-2개\n생채소(상추, 깻잎, 양파, 양배추)\n꼬시래기(없으면 패쓰)\n동치미 냉면 육수\n무쌈\n소면\n",
                    "1 냉면육수는 미리 냉동실에 1시간 정도 얼려두기\n2 꼬시래기는 물에 씻어 30분 이상 담궈둔 뒤에 끓는 물에 살짝 데쳐준다.\n3 찜기를 올려 새송이버섯을 5분동안 익혀준다.\n4 찬물에 담궈서 식혀준다. 얼음물에 담구면 더 빨리 식고 표면이 탱글해진다.\n5 겉면이 식으면 손으로 결따라 찢어준다. 안에는 아직 뜨거울 수 있으니 주의해야 한다..\n6 채소들은 채썰어 준비. \n7 고추장, 식초, 설탕, 무쌈 양념을 1스푼씩 넣어 양념장을 만든다. 그리고 물냉면 육수에 섞어주기\n8냉면 그릇에 채소 - 버섯 - 꼬시래기 - 무 채 순서로 가득 담아준다. 그리고 섞어둔 육수 부어주기\n9 양념장 넣고 꺠 뿌리기\n",
                    2
                )
            )

            add(
                RecipeDataVo(
                    "표고버섯 피자", R.drawable.shiitake_mushroom_pizza,
                    "생 표고버섯 5개\n토마토소스 5스푼\n토마토 1/4\n양파 1/4\n파프리카 1/4\n올리브 2개\n루꼴라 약간\n",
                    "1 표고버섯은 기둥을 제거하고 흙과 먼지를 잘 털어 준비한다.\n2 야채는 다져서 준비. 올리브 추가하면 더 맛있어진다. 없으면 패쓰!\n3 표고버섯 움푹 파진 부분에 토마토소스 바르고 다진 야채를 올린다.\n4 오븐 5분 예열하고, 180도에서 15분동안 구워준다. 에어프라이어도 동일\n5 오븐에서 꺼내 먹는다. 남은 채소나 샐러드 곁들이기.\n",
                    5
                )
            )

            add(
                RecipeDataVo(
                    "무 표고버섯 솥밥", R.drawable.shiitake_mushroom_rice,
                    "찹쌀 200g\n표고버섯 20g\n새송이 버섯 한줌\n무 200g\n들기름\n<양념장>\n진간장 2스푼, 다진파 1스푼, 참기름 1스푼, 참깨 1스푼, 설탕 약간",
                    "1 찹쌀을 씻어두기. 그냥 쌀이면 15분 이상 불려두기\n2 표고버섯은 씻어서 물에 15분 이상 불리기. 표고버섯 우러난 물은 버리지 말고 밥할때 같이 넣어준다! \n3 무 길게 썬다.\n4쌀과 물의 양을 1:1로 넣고(나는 표고버섯 우린 물을 넣었다.) 표고버섯, 무를 올린다. 추가하고 싶은 버섯이 있다면 같이 넣어주기.\n5 이제 뚜껑 닫고 센불로 7분 - 불 줄이고 5분 - 불 끄고 5분동안 뜸들이기. \n6 진간장 2스푼, 다진파 1스푼, 참기름 1스푼, 참깨 갈은것 1스푼, 설탕 아주 약간.\n7 먹기 전에 들기름 한스푼 둘러주고 잘 섞어준다.\n",
                    2
                )
            )

            add(
                RecipeDataVo(
                    "시금치 카레", R.drawable.spinach_curry,
                    "시금치 160g\n아몬드 밀크 120ml (아몬드 30g)\n감자 120g\n당근 120g\n양파 1개\n카레 4스푼 (40g)\n소금\n",
                    "1 시금치는 뿌리 흙을 털고 깨끗하게 씼어 꼭! 뿌리 까지 섭취해야한다.\n2 당근, 감자, 양파를 썰어준다.\n3 기름을 두르고 양파부터 볶아준다. \n4 양파가 갈색부분이 생기면 당근, 감자를 넣어 볶다가 물 300ml를 넣고 푹 익혀준다.\n5 감자 당근 푹 익는 동안 시금치 후딱 데치기. 소금물에 데쳐야 영양소 파괴를 줄임\n6 시금치 찬물에 헹궈 물기 살짝만 짜고아몬드우유 120ml를 넣고 함께 갈아준다.\n7 당근 감자가 익으면 카레가루를 넣고 간을 보며 소금 추가하여 5분 정도 끓인다.\n8 시금치, 아몬드 우유 갈은 것을 넣고 2분 정도 약불에 끓여주면 완성\n",
                    4
                )
            )

            add(
                RecipeDataVo(
                    "상추볶음", R.drawable.stir_fried_lettuce,
                    "상추 250g\n[소스]\n고추기름 1T\n참기름 1T\n간장 1T\n연두 1T\n물엿이나 올리고당 1T\n물 1T\n다진양파 또는 다진마늘 0.5T\n",
                    "1 끓는 물에 대충 10초 정도 데친다. 체망에 놓고 물기를 제거한다. 헹구지 않는다!! 절대 오래 데치지 않는다!\n2 고추기름, 참기름, 간장, 연두, 올리고당, 다진양파 잘 섞어서 팬에 넣고 익힌다. 바글바글 끓으면 불을 끈다.\n3 상추를 접시에 담고 그 위에 양념을 끼얹어준다.\n",
                    7
                )
            )

            add(
                RecipeDataVo(
                    "중식 크림배추",
                    R.drawable.stir_fried_white_shiitake_mushrooms_and_perilla_seeds,
                    "알배추 1/4 (250g)\n양송이버섯 2개\n마늘\n양파\n캐슈넛밀크 200ml (캐슈넛 30g+물170ml 넣고 갈아줌)\n물 100ml\n야채스톡 큐브 1/2개\n소금, 후추\n식용유\n전분물(없으면 생략)",
                    "1. 배추를 썬다.배추는 길게 사선으로 썬다. 두꺼운 줄기부분과 잎 부분이 한 조각에 있도록 해줘야 함. 그래야 먹을때 맛있음가운데로 한번 썰고, 사선으로 썰면 된다\n2. 마늘을 식용유에 볶다가 마늘 향이 나기시작하면 배추를 넣고 볶아준다.3. 물 100ml와 야채스톡 큐브를 넣는다. 뚜껑 덮고 5분 정도 놔둔다. 스톡을 녹이지 않고 넣었어서 뚜껑 열고 야채스톡이 녹도록 뒤적뒤적 해줬다 핵심은 야채스톡이 배추에 스며들도록 하는 것\n4. 5분 뒤 뚜껑 열고 배추만 건진다. 최대한 물 없이 건지고 소스만들 때 쓸것이므로 남은 물은 그대로 둔다.\n5. 배추를 건지고 남은 물에 캐슈넛밀크를 넣어준다.양파, 버섯을 넣고 센불에 휘저어준다.끓은 다음에 전분물을 넣고 천천히 저어준다.\n6. 건져둔 배추를 넣고 약불에 뒤집어 가며 익혀준다. 식용유 살짝 뿌리고 소금, 후추로 간한다.\n",
                    3
                )
            )
            add(
                RecipeDataVo(
                    "두부유부초밥", R.drawable.tofu_fried_tofu_rice_balls,
                    "시판 조미유부초밥\n두부 200g\n밥 200g\n채소 약간(당근, 브로콜리)\n",
                    "1 두부는 으깨면서 물기를 제거한다. 나는 치즈틀을 사용해서 반나절동안 냉장고에서 수분을 제거했다.\n2 채소 다지기\n3 밥, 두부, 채소, 조미액, 후레이크를 넣고 섞어준다.\n4 유부를 꺼내 물기를 체에 받쳐준다. 양념된 밥을 유부에 조금씩 눌러 담는다. 너무 많이 넣으면 터지니까 주의!\n",
                    7
                )
            )

            add(
                RecipeDataVo(
                    "두부 마요네즈", R.drawable.tofu_mayonnaise,
                    "두부 1/4 쪽 (100g)\n올리브유 80ml\n설탕 0.5스푼\n소금 두꼬집\n레몬즙 \n",
                    "1. 두부를 1/4 등분 한다.\n2. 블렌더 에 두부를 넣고 올리브오일 80ml를 넣는다.\n3. 레몬즙, 설탕, 소금을 넣는다.\n4. 블렌더로 갈아줌. 금방 유화된다 \n5. 유리병이나 통에 담아서 보관한다.\n",
                    7
                )
            )

            add(
                RecipeDataVo(
                    "중국식 고수 두부면 무침", R.drawable.tofu_noodles,
                    "두부면 1팩 (100g)\n고수 30g\n양파 1/4\n당근 1/3\n간장 1스푼\n식초 1스푼\n설탕 0.5스푼\n고추기름 1스푼\n고춧가루 0.5스푼\n소금 2꼬집\n참깨\n",
                    "1 두부면은 바로 사용해도 되지만 나는 끓는 물에 30초 정도 데쳤다. 물기 빼서 준비\n2 고수는 손가락 한마디 크기로 잘라주고, 당근 양파는 채썰어 준비\n3 간장1, 식초1, 설탕0.5, 고추기름1, 소금 1꼬집, 고춧가루 넣고 양념장을 섞어준다. \n4 두부면, 고수, 양념장을 잘 섞어주고 싱거우면 소금 1꼬집 추가. 꺠뿌려서 완성\n",
                    3
                )
            )

            add(
                RecipeDataVo(
                    "채식 양장피", R.drawable.vegun_dired_sweet_potato_stratch_sheet,
                    "[생채 용] \n양장피채 100g\n빨간 파프리카 1/2개, 노란 파프리카 1/2개\n오이 1/2개\n새송이버섯 2개 \n적양배추 1~2장\n오이고추 2개\n목이버섯 약간\n[볶음 용]\n양배추 2~3장(100g)\n생표고버섯 3개 (150g)\n부추 약간 (30g)\n목이버섯 한줌 (70g) \n양파 1/2개 (100g)\n오일1, 간장2, 연두1, 물 반컵\n[소스 용]\n겨자소스1스푼, 참기름1, 땅콩버터1, 연두1, 설탕1, 물1\n",
                    "1 겨자분 소스부터 만들기. 연겨자 일 경우에는 바로 사용하지만 겨자분일 경우 미지근한 물과 섞어 미리 불려놓는다.\n겨자소스 2: 물 3의 비율로 섞는다.\n2 새송이버섯은 끓는 물에 7분동안 쪄준다. 얼음 띄운 찬물에 식힌 후 결따라 찢어준다.\n3 노란색 파프리카부터 시작했다. 채를 썰어서 12시 방향, 6시 방향으로 놓아준다. 채썬 채소들을 오른쪽으로 2군데 씩 놓아주면 된다.\n4 양장피채를 끓는 물에 5분 삶는다. 중국집에서 먹는 흐물흐물한 양장피 채를 생각했는데 넙적 당면과 똑같다. \n5 당면은 찬물에 헹군다. 가만히 냅두면 뭉쳐서 덩어리가 될 것이므로 양념을 해준다.  겨자소스 1티스푼, 참기름 1티스푼, 설탕 1티스푼, 연두 1티스푼을 넣어 무쳐준다. \n 채소 중앙에 고이 모셔둔다.\n6 볶음 재료를 모두 넣어 볶아준다.순서는 기름에 양파부터 볶고, 그 다음에 버섯, 양배추, 목이버섯을 넣고 간장 2스푼, 연두1스푼, 물 반컵을 넣어 볶는다. 맨 마지막에 볶기가 끝났을 때 부추를 넣는다. 물기가 부족하면 물을 추가한다.\n7 양장피채 위에 올려주면 완성!\n*소스는 물에 개어 만들어둔 겨자소스1스푼, 참기름1, 땅콩버터1, 연두1, 설탕1, 물1를 넣어 섞어준다. 너무 맵다면 물을 추가하면 된다\n",
                    3
                )
            )

            add(
                RecipeDataVo(
                    "통밀 바나나 브레드", R.drawable.wholemeal_banna_bread,
                    "아주 잘익은 바나나 3개 (300g)\n통밀가루 240g\n두유 유청 90ml\n코코넛 슈가 30g \n식물성오일 25g\n두유 90ml (다른 식물성 밀크로 대체 가능)\n레몬쥬스 1 티스푼\n소금 1/3 티스푼\n베이킹 파우더 1 x티스푼\n시나몬가루 1티스푼\n",
                    "1 두유유청 휘저어서 거품 만들어 주기\n2 코코넛 슈가 30g + 오일 25ml 넣어 잘 섞어준다. 코코넛 슈가는 더 많이 넣어줘도 될 듯 하다. 40~60g 정도까지..너무 달지않고 담백했음 ㅎㅎ 두유 유청에 넣어 잘 섞어준다.\n3 바나나는 껍질을 벗기고 잘 으깨준다. 원하는 크기로 으깬 후 액체류에 잘 섞어준다.\n4 통밀가루 240g 체에 쳐서 넣고, 베이킹 소다 1스푼, 시나몬 가루 1스푼, 가루류를 넣고 잘 섞는다. 이때 반드시 많이 섞지 말것!! 많이 섞으면 떡같은 질감이 된다.\n2분 동안 섞었는데도 떡이 되어버렸으니 1분 이내로 날가루만 안보일정도로 대충 섞어주길 추천\n5 팬에 넣고 원하는 토핑을 올린다. 바나나 슬라이스를 올려도 되고, 나는 오트밀과 시나문가루, 코코넛 슈가를 뿌려주었다.\n6 180도에서 50분 동안 익혀준다. \n",
                    6
                )
            )
/*            Adapter.dataList = datas
            Adapter.notifyDataSetChanged()*/
        }

        while (randnum.size < 4) {
            randnum.add((1..datas.size).random())
        }
        for (i in 0..3) {
            timedatas.add(i, datas[randnum.sorted()[i]])
            Log.d("time", "랜덤 숫자 : " + randnum)
            Log.d("time", randnum.toString() + timedatas.toString())
            Log.d("time", "")
        }

        var curTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("hh:mm:ss")
        val formatted = curTime.format(formatter)
        Log.d("hour:min:sec = ", formatted)

        if (formatted == "23:59:10") {
            randnum.clear()
            while (randnum.size < 4) {
                randnum.add((1..datas.size).random())
            }
            for (i in 0..3) {
                timedatas.add(i, datas[randnum.sorted()[i]])
                Log.d("time", "랜덤 숫자 : " + randnum)
                Log.d("time", randnum.toString() + timedatas.toString())
                Log.d("time", "")
            }
        }

        Adapter.dataList = timedatas
        Adapter.notifyDataSetChanged()

        Adapter.setOnItemClickListener(
            object : RecipeAdapter.OnItemClickListener {
                override fun onItemClick(v: View, data: RecipeDataVo, pos: Int) {
                    val bundle = Bundle()
                    bundle.putParcelable("item", data)
                    view?.findNavController()
                        ?.navigate(R.id.action_homeFragment_to_recipeNextFragment, bundle)
                        .run {
                        }
                }
            })
    }

    companion object {
        const val RawCount = 2
    }
}
