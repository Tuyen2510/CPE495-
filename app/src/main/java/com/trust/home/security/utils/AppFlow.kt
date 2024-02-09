package com.trust.home.security.utils

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.trust.home.security.HomeSecurityApplication.getRef
import com.trust.home.security.database.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

object AppFlow {
    val userFlow = MutableStateFlow<User?>(null)
    var currentUser: User? = null

    fun initialize() {
        val userName = AppPrefsManager.getInstance().user.userName
        getRef(Constance.Reference.USERS)
            .child(userName ?: "")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()) {
                        CoroutineScope(Dispatchers.IO).launch {
                            currentUser = snapshot.getValue(User::class.java)
                            userFlow.emit(currentUser)
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }
}