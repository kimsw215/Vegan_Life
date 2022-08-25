package kr.ac.kpu.ce2019152012.vegan_life.Login_Join

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kr.ac.kpu.ce2019152012.vegan_life.MainActivity
import kr.ac.kpu.ce2019152012.vegan_life.R
import kr.ac.kpu.ce2019152012.vegan_life.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var auth: FirebaseAuth? = null
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseApp.initializeApp(this)

        auth = FirebaseAuth.getInstance()
        setup()

        var UserList = arrayListOf<String>()

/*        db.collection(auth?.currentUser?.email.toString().trim())
            .document("Info")
            .get()
            .addOnSuccessListener {
                UserList.add(it["email"].toString().trim())
                UserList.add(it["passwd"].toString().trim())
                UserList.add(it["age"].toString().trim())
                UserList.add(it["height"].toString().trim())
                UserList.add(it["weight"].toString().trim())
                UserList.add(it["nickname"].toString().trim())
                UserList.add(it["basiccal"].toString().trim())
                Log.d("userlist", UserList.toString().trim())
            }.addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }*/

        // 이메일로 로그인
        binding.loginBtn.setOnClickListener()
        {
            auth?.signInWithEmailAndPassword(
                binding.editId.text.toString().trim(),
                binding.editPw.text.toString().trim()
            )
                ?.addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                        val user = auth?.currentUser
                        updateUI(user)
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("userId", binding.editId.text.toString())
                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        finish()
     /*                   if (binding.editId.text.toString().trim() in UserList) {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("userId", binding.editId.text.toString())
                            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                            finish()
                        } else {
                            Log.w(TAG, "Error getting documents ")
                        }*/
                    } else {
                        Toast.makeText(this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }
        }

        binding.joinBtn.setOnClickListener()
        {
            val intent = Intent(this, JoinStepOneActivity::class.java)
            startActivity(intent)
        }

        binding.editPw.addTextChangedListener()
        {
            if (it!!.length >= 1) {
                binding.pwtextField.isHintEnabled = false
                binding.loginBtn.isClickable = true
                binding.loginBtn.isEnabled = true
            } else {
                binding.pwtextField.isHintEnabled = false
                binding.loginBtn.isClickable = false
                binding.loginBtn.isEnabled = false
            }
        }


    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accroingly.
        val currenUser = auth?.currentUser
        if (currenUser != null) {
            updateUI(currenUser)
        }
    }

    private fun sendEmailVerification() {
        // [START send_email_verification]
        val user = auth?.currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                // Email Verification sent
            }
        // [END send_email_verification]
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

    fun setup2() {
        db = FirebaseFirestore.getInstance()
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        db.firestoreSettings = settings
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}