package com.example.pda_project.ui.workers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pda_project.R
import com.google.firebase.firestore.FirebaseFirestore

class ReportErrorFragment : Fragment() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_report_error, container, false)

        firestore = FirebaseFirestore.getInstance()

        val closeButton = view.findViewById<Button>(R.id.buttonClose)
        val sendButton = view.findViewById<Button>(R.id.buttonSend)
        val titleEditText = view.findViewById<EditText>(R.id.editTextTitle)
        val contentEditText = view.findViewById<EditText>(R.id.editTextContent)

        closeButton.setOnClickListener {
            closeFragment()
        }

        sendButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()
            if (title.isNotEmpty() && content.isNotEmpty()) {
                val report = hashMapOf(
                    "title" to title,
                    "content" to content
                )
                firestore.collection("errorReport").add(report)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(context, "보고가 성공적으로 전송되었습니다.", Toast.LENGTH_SHORT).show()
                            closeFragment()
                        } else {
                            Toast.makeText(context, "전송 실패: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(context, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun closeFragment() {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(0, R.anim.slide_out_down) // 닫기 애니메이션 설정
            .remove(this)
            .commit()
    }
}
