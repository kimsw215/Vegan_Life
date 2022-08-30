package kr.ac.kpu.ce2019152012.vegan_life.MainFragments

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import kr.ac.kpu.ce2019152012.vegan_life.DataVo.JoinDataVo
import kr.ac.kpu.ce2019152012.vegan_life.R
import kr.ac.kpu.ce2019152012.vegan_life.databinding.FragmentResetprofileBinding
import java.util.regex.Pattern

class ResetProfileFragment : Fragment() {
    private var _binding: FragmentResetprofileBinding? = null
    private val binding get() = _binding!!

    private var auth: FirebaseAuth? = null
    private lateinit var db: FirebaseFirestore

    // Uri 받아오기 위한 전역 함수
    var ImgUri: Uri? = null

    // 닉네임 참인지 체크
    private var NickNameCheck: Boolean = false

    // 비밀번호 참인지 체크
    private var PasswordCheck: Boolean = false

    //비밀번호 조합 정규식
    val passwordValidation =
        "^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{8,15}\$"

    var nickname: String ?= null
    var height: Int = 0
    var weight : Int = 0
    var age : Int = 0

    // 비건 타입
    var VeganType: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResetprofileBinding.inflate(inflater, container, false)
        val view = binding.root

        FirebaseApp.initializeApp(requireActivity())

        auth = FirebaseAuth.getInstance()

        setup()

        binding.resetprofileInsertImage.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        db.collection(auth?.currentUser?.email.toString())
            .document("Info")
            .get().addOnSuccessListener {

                nickname = it["nickname"].toString()
                VeganType = it["vegantype"].toString().toInt()
                height = it["hieght"].toString().toInt()
                weight = it["weight"].toString().toInt()
                age = it["age"].toString().toInt()

                binding.resetprofileNickname.setText(nickname)
                when(VeganType){
                    1 -> {
                        binding.vegan.isSelected = true
                        binding.lacto.isSelected = false
                        binding.obo.isSelected = false
                        binding.lactoObo.isSelected = false
                        binding.fesco.isSelected = false
                    }
                    2 -> {
                        binding.vegan.isSelected = false
                        binding.lacto.isSelected = true
                        binding.obo.isSelected = false
                        binding.lactoObo.isSelected = false
                        binding.fesco.isSelected = false
                    }
                    3 -> {
                        binding.vegan.isSelected = false
                        binding.lacto.isSelected = false
                        binding.obo.isSelected = true
                        binding.lactoObo.isSelected = false
                        binding.fesco.isSelected = false
                    }
                    4 -> {
                        binding.vegan.isSelected = false
                        binding.lacto.isSelected = false
                        binding.obo.isSelected = false
                        binding.lactoObo.isSelected = true
                        binding.fesco.isSelected = false
                    }
                    5 -> {
                        binding.vegan.isSelected = false
                        binding.lacto.isSelected = false
                        binding.obo.isSelected = false
                        binding.lactoObo.isSelected = false
                        binding.fesco.isSelected = true
                    }
                }
                binding.resetprofileHeight.setText(height.toString())
                binding.resetprofileWeight.setText(weight.toString())
                binding.resetprofileAge.setText(age.toString())
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 닉네임
        // 추 후에 중복 체크 할 예정
        binding.resetprofileNickname.doAfterTextChanged {
            if (it!!.length < 2 || it!!.length > 9) {
                NickNameCheck = false
                Toast.makeText(requireContext(), "닉네임 사이즈를 맞춰주세요.", Toast.LENGTH_SHORT).show()
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
            db.collection(auth?.currentUser.toString()).document("Info").update(
                mapOf(
                    "image" to ImgUri,
                    "nickname" to binding.resetprofileNickname.toString().trim(),
                    "passwd" to binding.resetprofilePw.toString().trim(),
                    "vegantype" to VeganType,
                    "hieght" to binding.resetprofileHeight,
                    "weight" to binding.resetprofileWeight,
                    "age" to binding.resetprofileAge
                )
            )
            Toast.makeText(requireContext(),"프로필이 변경되었습니다.",Toast.LENGTH_SHORT).show()
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

    // 비밀번호 형식 체크
    fun checkPassword(): Boolean {
        val p = Pattern.matches(
            passwordValidation,
            binding.resetprofilePw.text.toString().trim()
        ) // 서로 패턴이 맞는지
        if (p) {
            //비밀번호 형태가 정상일 경우
            binding.resetprofilePw.setBackgroundResource(R.drawable.button_background_stroke)
            PasswordCheck = true
            return true
        } else {
            binding.resetprofilePw.setBackgroundResource(R.drawable.edit_fail_background_stroke)
            PasswordCheck = false
            Toast.makeText(requireContext(), "패스워드 형식이 아닙니다.", Toast.LENGTH_SHORT).show()
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
