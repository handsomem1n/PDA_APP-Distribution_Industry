package com.example.pda_project.ui.workers.inbound

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pda_project.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class InboundAcceptActivity : AppCompatActivity() {

    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private lateinit var listView: ListView
    private lateinit var documentTextView: TextView
    private lateinit var acceptButton: Button
    private var selectedDocumentId: String? = null
    private var selectedDocumentData: Map<String, Any>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbound_accept)

        listView = findViewById(R.id.listView)
        documentTextView = findViewById(R.id.documentTextView)
        acceptButton = findViewById(R.id.acceptButton)

        fetchDocuments()

        listView.setOnItemClickListener { parent, _, position, _ ->
            selectedDocumentId = parent.getItemAtPosition(position) as String
            fetchDocumentData(selectedDocumentId!!)
        }

        acceptButton.setOnClickListener {
            selectedDocumentId?.let { documentId ->
                selectedDocumentData?.let { documentData ->
                    acceptInbound(documentId, documentData)
                }
            } ?: Toast.makeText(this, "Please select a document to accept.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchDocuments() {
        firestore.collection("warehouseTemp").get()
            .addOnSuccessListener { documents ->
                val documentIds = documents.map { it.id }
                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, documentIds)
                listView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error fetching documents: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchDocumentData(documentId: String) {
        firestore.collection("warehouseTemp").document(documentId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    selectedDocumentData = document.data
                    displayDocumentData(document.data)
                } else {
                    Toast.makeText(this, "Document not found.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error fetching document data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun displayDocumentData(data: Map<String, Any>?) {
        data?.let {
            val displayText = it.map { (key, value) ->
                val itemDetails = value as Map<String, Any>
                val itemName = itemDetails["item"] as String
                val amount = itemDetails["amount"] as Long
                "Item: $itemName, Amount: $amount"
            }.joinToString("\n")
            documentTextView.text = displayText
        }
    }

    private fun acceptInbound(documentId: String, documentData: Map<String, Any>) {
        val warehouseNumber = documentId.split("-")[0]

        firestore.runTransaction { transaction ->
            val warehouseRef = firestore.collection("warehouse").document(warehouseNumber)
            val warehouseSnapshot = transaction.get(warehouseRef)

            documentData.forEach { (productId, itemDetails) ->
                val itemMap = itemDetails as Map<String, Any>
                val amountToAdd = itemMap["amount"] as Long
                val itemName = itemMap["item"] as String

                if (warehouseSnapshot.exists()) {
                    val currentAmount = warehouseSnapshot.getLong("$productId.amount") ?: 0L
                    transaction.update(warehouseRef, "$productId.amount", currentAmount + amountToAdd)
                    transaction.update(warehouseRef, "$productId.item", itemName)
                } else {
                    val newItemMap = mapOf(
                        "item" to itemName,
                        "amount" to amountToAdd
                    )
                    transaction.set(warehouseRef, mapOf(productId to newItemMap), SetOptions.merge())
                }
            }

            // warehouseTemp 문서 삭제
            transaction.delete(firestore.collection("warehouseTemp").document(documentId))
        }.addOnSuccessListener {
            Toast.makeText(this, "입고 승인이 완료되었습니다.", Toast.LENGTH_SHORT).show()
            fetchDocuments() // 문서 목록 새로고침
            documentTextView.text = ""
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "입고 승인 오류: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
