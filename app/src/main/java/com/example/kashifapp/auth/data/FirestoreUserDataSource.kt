package com.example.kashifapp.auth.data

import com.example.kashifapp.auth.domain.model.User
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreUserDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun createUserDocument(user: User) {
        firestore.collection("users")
            .document(user.id)
            .set(mapOf(
                "email" to user.email,
                "displayName" to user.displayName,
                //"photoUrl" to user.photoUrl,
                "createdAt" to FieldValue.serverTimestamp()
            ))
            .await()
    }
}