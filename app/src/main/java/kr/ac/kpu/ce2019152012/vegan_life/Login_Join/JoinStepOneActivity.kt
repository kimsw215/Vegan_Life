package kr.ac.kpu.ce2019152012.vegan_life.Login_Join

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import kr.ac.kpu.ce2019152012.vegan_life.DataVo.JoinDataVo
import kr.ac.kpu.ce2019152012.vegan_life.databinding.ActivityJoinOneBinding
import org.w3c.dom.Text

class JoinStepOneActivity : AppCompatActivity() {

    private lateinit var binding : ActivityJoinOneBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinOneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.joinInsertImage.paintFlags = Paint.UNDERLINE_TEXT_FLAG

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
                    Toast.makeText(this,"권한을 거부하셨습니다.",Toast.LENGTH_SHORT)
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
}