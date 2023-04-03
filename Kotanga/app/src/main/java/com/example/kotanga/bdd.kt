package com.example.kotanga

import com.google.firebase.database.*
import java.util.Date

class User {
    var id: String? = null
    var name: String? = null
    var groupIds: List<Groupe>? = null
    // Ajouter d'autres champs ici si nécessaire
}

class Message {
    var date = Date()
    var content: String? = null
    var userIdSender: String? = null
}

class Groupe {
    var id: String? = null
    var name: String? = null
    var usersIds: List<User>? = null
    var messages: List<Message>? = null
    // Ajouter d'autres champs ici si nécessaire
}

class FirebaseManager {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val usersRef: DatabaseReference = database.getReference("users")
    private val groupesRef: DatabaseReference = database.getReference("groupes")

    // Ajoute un utilisateur à la base de données.
    // La méthode prend un objet User en entrée ainsi qu'une closure completion.
    // La closure completion est appelée à la fin de l'opération pour renvoyer un booléen indiquant si l'opération s'est bien déroulée
    // et une chaîne de caractères représentant l'ID de l'utilisateur nouvellement ajouté ou une erreur.
    fun addUser(user: User, completion: (Boolean, String?) -> Unit) {
        val userRef: DatabaseReference = usersRef.push()
        user.id = userRef.key // Attribue une ID unique à l'utilisateur
        userRef.setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                completion(true, user.id)
            } else {
                completion(false, task.exception?.message)
            }
        }
    }

    // Ajoute un groupe à la base de données.
    // La méthode prend un objet Groupe en entrée ainsi qu'une closure completion.
    // La closure completion est appelée à la fin de l'opération pour renvoyer un booléen indiquant si l'opération s'est bien déroulée
    // et une chaîne de caractères représentant l'ID du groupe nouvellement ajouté ou une erreur.
    fun addGroupe(groupe: Groupe, completion: (Boolean, String?) -> Unit) {
        val groupeRef: DatabaseReference = groupesRef.push()
        groupe.id = groupeRef.key // Attribue une ID unique au groupe
        groupeRef.setValue(groupe).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                completion(true, groupe.id)
            } else {
                completion(false, task.exception?.message)
            }
        }
    }

    // Récupère un utilisateur à partir de son ID dans la base de données.
    // La méthode prend l'ID de l'utilisateur en entrée ainsi qu'une closure completion.
    // La closure completion est appelée à la fin de l'opération pour renvoyer l'objet User correspondant ou null.
    fun getUserById(userId: String, completion: (User?) -> Unit) {
        usersRef.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user: User? = snapshot.getValue(User::class.java)
                completion(user)
            }

            override fun onCancelled(error: DatabaseError) {
                completion(null)
            }
        })
    }

    // Récupère un groupe à partir de son ID dans la base de données.
    // La méthode prend l'ID du groupe en entrée ainsi qu'une closure completion.
    // La closure completion est appelée à la fin de l'opération pour renvoyer l'objet

    fun getGroupeById(groupeId: String, completion: (Groupe?) -> Unit) {
        groupesRef.child(groupeId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val groupe: Groupe? = snapshot.getValue(Groupe::class.java)
                completion(groupe)
            }

            override fun onCancelled(error: DatabaseError) {
                completion(null)
            }
        })
    }

    fun updateUserName(userId: String, newName: String, completion: (Boolean, String) -> Unit) {
        usersRef.child(userId).child("name").setValue(newName).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                completion(true, "Le nom de l'utilisateur a été mis à jour avec succès.")
            } else {
                completion(false, "Une erreur est survenue : ${task.exception?.message}")
            }
        }
    }

    fun addMessage(message: Message, groupId: String) {

    }
}


