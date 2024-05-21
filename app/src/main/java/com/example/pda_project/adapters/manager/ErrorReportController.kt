package com.example.pda_project.adapters.manager

import android.annotation.SuppressLint
import android.util.Log
import com.example.pda_project.models.manager.ErrorReport
import com.google.firebase.firestore.FirebaseFirestore

object ErrorReportController {
    @SuppressLint("StaticFieldLeak")
    private val db = FirebaseFirestore.getInstance()

    fun fetchErrorReports(callback: (List<ErrorReport>) -> Unit) {
        db.collection("errorReport")
            .get()
            .addOnSuccessListener { result ->
                val errorReports = result.map { document ->
                    document.toObject(ErrorReport::class.java).apply { id = document.id }
                }
                callback(errorReports)
            }
            .addOnFailureListener { exception ->
                Log.e("ErrorReportManager", "Error fetching error reports", exception)
            }
    }
}
