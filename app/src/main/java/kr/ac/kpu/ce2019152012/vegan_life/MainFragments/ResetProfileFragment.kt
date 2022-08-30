package kr.ac.kpu.ce2019152012.vegan_life.MainFragments

import android.app.Activity
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
import androidx.appcompat.app.AlertDialog
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
import java.util.regex.Pattern

class ResetProfileFragment : Fragment() {
    private var _binding: FragmentResetprofileBinding? = null
    private val binding get() = _binding!!
    private lateinit var JoinData: ArrayList<JoinDataVo>

    private var auth: FirebaseAuth? = null
    private lateinit var db: FirebaseFirestore

    private var itemCollectionRef = db.collection(auth?.currentUser?.email.toString()).document("Info")

    // Uri 받아오기 위한 전역 함수
    var ImgUri: Uri? = null

    // 닉네임 참인지 체크
    private var NickNameCheck: Boolean = false

    // 이메일 참인지 체크
    private var EmailCheck: Boolean = false

    // 비밀번호 참인지 체크
    private var PasswordCheck: Boolean = false
    //비밀번호 조합 정규식
    val passwordValidation = "^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{8,15}\$"

    // 비건 타입
    var VeganType: Int = 0
    /*
    vegan = 1 , lacto = 2 , obo = 3 , lactoObo = 4 , fesco = 5
    */

    var basicCal: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentResetprofileBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.resetprofileInsertImage.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        return view
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 이미지 올리기 버튼을 눌렀을 때 프로필 변경하기
        binding.resetprofileInsertImage.setOnClickListener {
            when {
                // 갤러리 접근 권한이 있는 경우
                ContextCompat.checkSelfPermission(
                    requireActivity(),
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
            if (it!!.length < 2 || it!!.length > 9) {
                NickNameCheck = false
                Toast.makeText(requireActivity(), "닉네임 사이즈를 맞춰주세요.", Toast.LENGTH_SHORT).show()
            } else {
                NickNameCheck = true
            }
        }

        // 비밀번호
        binding.resetprofilePw.doAfterTextChanged {
            checkPassword()
        }

        binding.vegan.setOnClickListener {
            binding.vegan.isSelected = binding.vegan.isSelected != true
            binding.lacto.isSelected = false
            binding.obo.isSelected = false
            binding.lactoObo.isSelected = false
            binding.fesco.isSelected = false

            if (binding.vegan.isSelected) {
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

            if (binding.lacto.isSelected) {
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

            if (binding.obo.isSelected) {
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

            if (binding.lactoObo.isSelected) {
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

            if (binding.fesco.isSelected) {
                VeganType = 5
            } else {
                VeganType = 0
            }
        }

        binding.nextBtn.setOnClickListener {
            // 프로필 사진 변경
            itemCollectionRef.update(mapOf(
                "image" to ImgUri,
                "nickname" to binding.resetprofileNickname.toString().trim(),
                "passwd" to binding.resetprofilePw.toString().trim(),
                "vegantype" to VeganType,
                "hieght" to binding.resetprofileHeight,
                "weight" to binding.resetprofileWeight,
                "age" to binding.joinAge
            ))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUI(user: FirebaseUser?) {
    }

    private fun reload() {
    }

    // 권한 요청 승인 이후 실행되는 함수
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            1000 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    navigateGallery()
                else
                    Toast.makeText(requireActivity(),"권한을 거부하셨습니다.",Toast.LENGTH_SHORT)
            }
            else ->{
                //
            }
        }
    }

    private fun navigateGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        // 가져올 콘턴츠들 중에서 Image 만을 가져온다
        intent.type = "image/*"
        // 갤러리에서 이미지를 선택한 후, 프로필 이미지 뷰를 수정하기 위해 갤러리에서 수행한 값을 받아오는 startActivityForeResult를 사용한다.
        startActivityForResult(intent,2000)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 예외처리
        if (resultCode != Activity.RESULT_OK)
            return

        when(requestCode){
            // 2000: 이미지 컨텐츠를 가져오는 액티비티를 수행한 후 실행되는 Activity 일 때만 수행하기 위해서
            2000 -> {
                val selectedImageUri: Uri? = data?.data
                if (selectedImageUri != null){
                    binding.resetprofileImage.setImageURI(selectedImageUri)
                    ImgUri = selectedImageUri
//                    Glide.with(this).load(selectedImageUri).circleCrop().into(binding.joinImage)
                } else {
                    Toast.makeText(requireActivity(), "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(requireActivity(), "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showPermissionContetxtPopup(){
        AlertDialog.Builder(requireActivity())
            .setTitle("권한이 필요합니다.")
            .setMessage("프로필 이미지를 바꾸기 위해서는 갤러리 접근 권한이 필요합니다.")
            .setPositiveButton("동의"){_, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1000)
            }
            .setNegativeButton("취소"){_, _ ->}
            .create()
            .show()
    }

    // 비밀번호 형식 체크
    fun checkPassword():Boolean{
        val p = Pattern.matches(passwordValidation, binding.resetprofilePw.text.toString().trim()) // 서로 패턴이 맞닝?
        if (p) {
            //비밀번호 형태가 정상일 경우
            binding.resetprofilePw.setBackgroundResource(R.drawable.button_background_stroke)
            PasswordCheck = true
            return true
        } else {
            binding.resetprofilePw.setBackgroundResource(R.drawable.edit_fail_background_stroke)
            PasswordCheck = false
            Toast.makeText(requireActivity(),"패스워드 형식이 아닙니다.",Toast.LENGTH_SHORT)
            return false
        }
    }

    fun setup() {
        db = Firebase.firestore

        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        db.firestoreSettings = settings

    }
}
