package com.example.kashifapp.place.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FirestoreSavedPlacesDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val userId get() = auth.currentUser?.uid

    suspend fun savePlaceRemote(placeId: String) {
        val uid = userId ?: return
        firestore.collection("users")
    }
}