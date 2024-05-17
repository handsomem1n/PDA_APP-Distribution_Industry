package com.example.pda_project.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pda_project.MainActivity
import com.example.pda_project.ui.workers.ModeSelectActivity
import com.example.pda_project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)
        val registerButton: Button = findViewById(R.id.signupButton)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
                registerUser(email, password)
            }
        }
    }

    private fun registerUser(email: String, password: String) {
        val hashedEmail = hashString(email)

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userMap = hashMapOf(
                        "email" to email,
                        "role" to "wait"
                    )

                    db.collection("users").document(hashedEmail).set(userMap)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Signup 성공! 관리자 승인 이후 로그인 가능합니다.", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error registering user: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Error registering user: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun loginUser(email: String, password: String) {
        val hashedEmail = hashString(email)

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    db.collection("users").document(hashedEmail).get()
                        .addOnSuccessListener { document ->
                            if (document != null && document.exists()) {
                                val userRole = document.getString("role")
                                userRole?.let { role ->
                                    // 기능 부여
                                    provideFeaturesBasedOnRole(role)
                                }
                            } else {
                                Toast.makeText(this, "해당 사용자를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error getting user document: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "로그인 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun hashString(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.fold("", { str, it -> str + "%02x".format(it) })
    }

    private fun provideFeaturesBasedOnRole(role: String) {
        when (role) {
            "manager" -> {
                // 관리자 기능 활성화
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            "workers" -> {
                // 일반 사용자 기능 활성화
                startActivity(Intent(this, ModeSelectActivity::class.java))
                finish()
            }
            else -> {
                Toast.makeText(this, "Unknown role: $role", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
