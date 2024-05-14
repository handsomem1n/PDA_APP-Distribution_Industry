package com.example.pda_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.FrameLayout

class PDAExportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pda_export)

        findViewById<Button>(R.id.buttonOpenScanner).setOnClickListener {
            openScanner()
        }
        val buttonErrorReport = findViewById<Button>(R.id.buttonReportError)
        buttonErrorReport.setOnClickListener {
            val fragment = ReportErrorFragment()
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down)
                .replace(R.id.mainScreen, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun openScanner() {
        // FrameLayout의 레이아웃 파라미터 설정
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )

        // BarcodeScannerFragment 생성
        val scannerFragment = BarcodeScannerFragment()
        scannerFragment.view?.layoutParams = layoutParams

        // 프래그먼트를 `fragment_container`에 추가합니다.
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, scannerFragment)
            .commit()
    }

}