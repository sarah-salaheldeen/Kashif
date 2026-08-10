package com.example.kashifapp.place.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreSavedPlacesDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val userId get() = auth.currentUser?.uid

    suspend fun savePlaceRemote(placeId: String) {
        val uid = userId ?: return
        firestore.collection("users")
            .document(uid)
            .collection("saved_places")
            .document(placeId)
            .set(mapOf("savedAt" to FieldValue.serverTimestamp()))
            .await()
    }

    suspend fun removeSavedPlaceRemote(placeId: String) {
        val uid = userId ?: return
        firestore.collection("users")
            .document(uid)
            .collection("saved_places")
            .document(placeId)
            .delete()
            .await()
    }

    suspend fun fetchSavedPlaceIds(): List<String> {
        val uid = userId ?: return emptyList()
        return firestore.collection("users")
            .document(uid)
            .collection("saved_places")
            .get()
            .await()
            .documents
            .map { it.id }
    }
}