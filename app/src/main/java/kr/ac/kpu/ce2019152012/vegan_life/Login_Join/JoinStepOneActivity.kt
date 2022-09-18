package kr.ac.kpu.ce2019152012.vegan_life.Login_Join

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kr.ac.kpu.ce2019152012.vegan_life.DataVo.JoinDataVo
import kr.ac.kpu.ce2019152012.vegan_life.R
import kr.ac.kpu.ce2019152012.vegan_life.databinding.ActivityJoinOneBinding
import java.util.regex.Pattern
import kotlin.collections.ArrayList

class JoinStepOneActivity : AppCompatActivity() {
    private lateinit var binding : ActivityJoinOneBinding

    private lateinit var JoinData: ArrayList<JoinDataVo>

    // Uri 받아오기 위한 전역 함수
    var ImgUri : Uri?= null
    var ImageCheck = 0

    private var firebaseStore : FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    // 닉네임 참인지 체크
    private var NickNameCheck : Boolean = false

    // 이메일 참인지 체크
    private var EmailCheck : Boolean = false

    // 비밀번호 참인지 체크
    private var PasswordCheck  : Boolean = false

    // 비밀번호 재입력이 비밀번호와 같은지 체크
    private var Password2Check: Boolean = false

    // 이메일 검사 정규식
    val emailValidation = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

    //비밀번호 조합 정규식
    val passwordValidation = "^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{8,15}\$"

     /*
    숫자,문자,특수문자 중 2가지 이상 조합
    ^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#$%^&*])(?=.*[0-9!@#$%^&*]).{8,15}$
    */
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinOneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.joinInsertImage.paintFlags = Paint.UNDERLINE_TEXT_FLAG

         firebaseStore = FirebaseStorage.getInstance()
         storageReference = FirebaseStorage.getInstance().reference

        // 이미지 올리기 버튼을 눌렀을 때 프로필 변경하기
        binding.joinInsertImage.setOnClickListener {
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
        binding.joinNickname.doAfterTextChanged {
            if(it!!.length < 2 || it!!.length > 9){
                NickNameCheck = false
                Toast.makeText(this,"닉네임 사이즈를 맞춰주세요.",Toast.LENGTH_SHORT).show()
            } else {
                NickNameCheck = true
            }
        }

        // 이메일
        binding.joinEmail.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // text가 변경된 후 호출
                // s에는 변경 후의 문자열이 담겨 있다.
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // text가 변경되기 전 호출
                // s에는 변경 전 문자열이 담겨 있다.
            }

            override fun afterTextChanged(p0: Editable?) {
                // text가 바뀔 때마다 호출된다.
                checkEmail()
            }
        })

        // 비밀번호
        binding.joinPw.doAfterTextChanged {
            checkPassword()
        }
        // 비밀번호 재입력 확인
        binding.joinPw2.doAfterTextChanged {
            if(binding.joinPw.text.toString().trim() == binding.joinPw2.text.toString().trim()){
                binding.joinPw2.setBackgroundResource(R.drawable.button_background_stroke)
                Password2Check = true
            } else {
                binding.joinPw2.setBackgroundResource(R.drawable.edit_fail_background_stroke)
                Password2Check = false
                Toast.makeText(this,"비밀번호를 확인해주세요.",Toast.LENGTH_SHORT).show()
            }
        }

        // 다음 버튼
        binding.nextBtn.setOnClickListener {
            if(NickNameCheck && EmailCheck && PasswordCheck && Password2Check){

                // 정보 넘겨 줄 때 사진이 있는 지 없는 지 체크 후 있으면 uri 그대로 보내고 아니면 0 보내기기

                if(ImgUri != null){
                    val Data = JoinDataVo(ImgUri,binding.joinNickname.text.toString().trim(),
                        binding.joinEmail.text.toString().trim(),binding.joinPw.text.toString().trim())


                    val intent = Intent(this,JoinStepTwoActivity::class.java)
                    intent.putExtra("Join",Data)
                    startActivity(intent)
                } else {
                    val Data = JoinDataVo(null,binding.joinNickname.text.toString().trim(),
                        binding.joinEmail.text.toString().trim(),binding.joinPw.text.toString().trim())

                    val intent = Intent(this,JoinStepTwoActivity::class.java)
                    intent.putExtra("Join",Data)
                    startActivity(intent)
                }


            } else{
                Toast.makeText(this,"필수정보가 미기입되어있습니다.",Toast.LENGTH_SHORT).show()
            }
        }
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
                    Toast.makeText(this,"권한을 거부하셨습니다.",Toast.LENGTH_SHORT).show()
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
                    binding.joinImage.setImageURI(selectedImageUri)
                    ImgUri = selectedImageUri
//                    Glide.with(this).load(selectedImageUri).circleCrop().into(binding.joinImage)
                } else {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
       }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showPermissionContetxtPopup(){
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("프로필 이미지를 바꾸기 위해서는 갤러리 접근 권한이 필요합니다.")
            .setPositiveButton("동의"){_, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1000)
            }
            .setNegativeButton("취소"){_, _ ->}
            .create()
            .show()
    }

    private fun UploadImage(){
        if(ImgUri != null){
            var imgFileName = binding.joinEmail.text
            val ref = storageReference?.child("${binding.joinEmail.toString()}/" +
                    "ProfileImage")
            val uploadTask = ref?.putFile(ImgUri!!)
            ImageCheck = 1
        } else {
            Toast.makeText(this,"사진을 등록안했습니다.",Toast.LENGTH_SHORT).show()
        }
    }

    // 이메일 형식 체크
    fun checkEmail():Boolean{
        val p = Pattern.matches(emailValidation, binding.joinEmail.text.toString().trim()) // 서로 패턴이 맞는지 체크
        if (p) {
            //이메일 형태가 정상일 경우
            binding.joinEmail.setBackgroundResource(R.drawable.button_background_stroke)
            EmailCheck = true
            return true
        } else {
            binding.joinEmail.setBackgroundResource(R.drawable.edit_fail_background_stroke)
            EmailCheck = false
            Toast.makeText(this,"이메일 형식이 아닙니다.",Toast.LENGTH_SHORT)
            //또는 binding.joinEmail.setTextColor(R.color.red.toInt())
            return false
        }
    }

    // 비밀번호 형식 체크
    fun checkPassword():Boolean{
        val p = Pattern.matches(passwordValidation, binding.joinPw.text.toString().trim()) // 서로 패턴이 맞닝?
        if (p) {
            //비밀번호 형태가 정상일 경우
            binding.joinPw.setBackgroundResource(R.drawable.button_background_stroke)
            PasswordCheck = true
            return true
        } else {
            binding.joinPw.setBackgroundResource(R.drawable.edit_fail_background_stroke)
            PasswordCheck = false
            Toast.makeText(this,"패스워드 형식이 아닙니다.",Toast.LENGTH_SHORT)
            return false
        }
    }
}