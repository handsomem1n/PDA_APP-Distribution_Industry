package com.example.pda_project.ui.workers

import androidx.fragment.app.FragmentActivity
import com.example.pda_project.R

class Scanner(private val activity: FragmentActivity) {
    fun openScanner() {
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainScreen, BarcodeScannerFragment(), "BarcodeScannerFragment")
        transaction.addToBackStack(null)
        transaction.commit() // commitAllowingStateLoss 대신 commit 사용
    }
}