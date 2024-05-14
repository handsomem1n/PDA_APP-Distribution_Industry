package com.example.pda_project

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class BarcodeScannerFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 바코드 스캐너 초기화
        IntentIntegrator.forSupportFragment(this).initiateScan()
    }

    // 스캔 결과 처리
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                // 바코드 스캔 성공
                val scannedValue = result.contents
                // Toast로 스캔된 값을 화면에 출력
                Toast.makeText(requireContext(), "스캔된 바코드: $scannedValue", Toast.LENGTH_SHORT).show()
            } else {
                // 스캔 실패 또는 취소됨
                Toast.makeText(requireContext(), "바코드 스캔 실패 또는 취소됨", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
