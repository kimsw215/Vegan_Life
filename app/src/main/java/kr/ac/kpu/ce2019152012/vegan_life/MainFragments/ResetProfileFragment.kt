package kr.ac.kpu.ce2019152012.vegan_life.MainFragments

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kr.ac.kpu.ce2019152012.vegan_life.DataVo.JoinDataVo
import kr.ac.kpu.ce2019152012.vegan_life.Login_Join.JoinStepTwoActivity
import kr.ac.kpu.ce2019152012.vegan_life.R
import kr.ac.kpu.ce2019152012.vegan_life.databinding.FragmentResetprofileBinding
import kr.ac.kpu.ce2019152012.vegan_life.databinding.FragmentHomeBinding

class ResetProfileFragment : Fragment() {
    private lateinit var binding : FragmentResetprofileBinding
    private lateinit var JoinData: ArrayList<JoinDataVo>

    private var auth: FirebaseAuth? = null
    private lateinit var db: FirebaseFirestore

    // Uri 받아오기 위한 전역 함수
    var ImgUri : Uri?= null
    // 닉네임 참인지 체크
    private var NickNameCheck : Boolean = false

    // 이메일 참인지 체크
    private var EmailCheck : Boolean = false

    // 비밀번호 참인지 체크
    private var PasswordCheck  : Boolean = false

    // 비건 타입
    var VeganType: Int = 0
    /*
    vegan = 1 , lacto = 2 , obo = 3 , lactoObo = 4 , fesco = 5
    */
    var SexType: Int = 0
    /*
    female = 2 , male = 1
    */
    var basicCal: Double = 0.0


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentResetprofileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.resetprofileInsertImage.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        // 이미지 올리기 버튼을 눌렀을 때 프로필 변경하기
        binding.resetprofileInsertImage.setOnClickListener {
            when {
                // 갤러리 접근 권한이 있는 경우
                ContextCompat.checkSelfPermission(
                    this,
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

        // 닉네임
        // 추 후에 중복 체크 할 예정
        binding.resetprofileNickname.doAfterTextChanged {
            if(it!!.length < 2 || it!!.length > 9){
                NickNameCheck = false
                Toast.makeText(this,"닉네임 사이즈를 맞춰주세요.", Toast.LENGTH_SHORT).show()
            } else {
                NickNameCheck = true
            }
        }

        // 비밀번호
        binding.resetprofilePw.doAfterTextChanged {
            checkPassword()
        }
        // 비건 단계설정
        val myData = intent?.getParcelableExtra<JoinDataVo>("Join")
        val text =
            "JoinData{${myData?.profileImage}, ${myData?.nickname}, ${myData?.email}, ${myData?.password}}"

        binding.vegan.setOnClickListener {
            binding.vegan.isSelected = binding.vegan.isSelected != true
            binding.lacto.isSelected = false
            binding.obo.isSelected = false
            binding.lactoObo.isSelected = false
            binding.fesco.isSelected = false

            if(binding.vegan.isSelected){
                VeganType = 1
            } else {
                VeganType = 0
            }
        }

        binding.lacto.setOnClickListener {
            binding.lacto.isSelected = binding.lacto.isSelected != true
            binding.vegan.isSelected = false
            binding.obo.isSelected = false
            binding.lactoObo.isSelected = false
            binding.fesco.isSelected = false

            if(binding.lacto.isSelected){
                VeganType = 2
            } else {
                VeganType = 0
            }
        }
        binding.obo.setOnClickListener {
            binding.obo.isSelected = binding.obo.isSelected != true
            binding.vegan.isSelected = false
            binding.lacto.isSelected = false
            binding.lactoObo.isSelected = false
            binding.fesco.isSelected = false

            if(binding.obo.isSelected){
                VeganType = 3
            } else {
                VeganType = 0
            }
        }
        binding.lactoObo.setOnClickListener {
            binding.lactoObo.isSelected = binding.lactoObo.isSelected != true
            binding.vegan.isSelected = false
            binding.lacto.isSelected = false
            binding.obo.isSelected = false
            binding.fesco.isSelected = false

            if(binding.lactoObo.isSelected){
                VeganType = 4
            } else {
                VeganType = 0
            }
        }
        binding.fesco.setOnClickListener {
            binding.fesco.isSelected = binding.fesco.isSelected != true
            binding.vegan.isSelected = false
            binding.lacto.isSelected = false
            binding.obo.isSelected = false
            binding.lactoObo.isSelected = false

            if(binding.fesco.isSelected){
                VeganType = 5
            } else {
                VeganType = 0
            }
        }


    // 밑에 지역함수 수정필요하다고 나와있는데 방법을몰라 일단 깃 하겠습니다
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResetprofileBinding.inflate(inflater,container,false)
        val view = binding.root

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