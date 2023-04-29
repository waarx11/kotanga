package com.example.kotanga

import com.google.firebase.database.*

class User {
    var id: String? = null
    var name: String? = null
    var groupIds: MutableList<Groupe>? = null
    // Ajouter d'autres champs ici si nécessaire
}

class Depense {
    var id: String? = null
    var name: String? = null
    var type: String? = null
    var date: String? = null
    var price: Float? = null
    var priceType: String? = null
    var payBy: User? = null
    var payFor: User? = null
}

class Groupe {
    var id: String? = null
    var name: String? = null
    var usersIds: MutableList<User>? = null
    var depenses: MutableList<Depense>? = null
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

    // Ajoute un groupe à la liste des groupes d'un utilisateur
// La méthode prend l'ID de l'utilisateur et l'objet groupe en entrée ainsi qu'une closure completion.
// La closure completion est appelée à la fin de l'opération pour renvoyer un booléen indiquant si l'opération s'est bien déroulée
    fun addGroupeToUserGroups(userId: String, groupe: Groupe, completion: (Boolean) -> Unit) {
        getUserById(userId) { user ->
            if (user != null) {
                if (user.groupIds == null) {
                    user.groupIds = mutableListOf(groupe)
                } else {
                    user.groupIds?.add(groupe)
                }
                usersRef.child(userId).child("groupIds").setValue(user.groupIds).addOnCompleteListener { task ->
                    completion(task.isSuccessful)
                }
            } else {
                completion(false)
            }
        }
    }

    // Ajoute un utilisateur à la liste des utilisateurs d'un groupe
// La méthode prend l'ID du groupe et l'objet utilisateur en entrée ainsi qu'une closure completion.
// La closure completion est appelée à la fin de l'opération pour renvoyer un booléen indiquant si l'opération s'est bien déroulée
    fun addUserToGroupeUsers(groupeId: String, user: User, completion: (Boolean) -> Unit) {
        getGroupeById(groupeId) { groupe ->
            if (groupe != null) {
                if (groupe.usersIds == null) {
                    groupe.usersIds = mutableListOf(user)
                } else {
                    groupe.usersIds?.add(user)
                }
                groupesRef.child(groupeId).child("usersIds").setValue(groupe.usersIds).addOnCompleteListener { task ->
                    completion(task.isSuccessful)
                }
            } else {
                completion(false)
            }
        }
    }

    fun addDepenseToGroupeDepenses(groupeId: String, depense: Depense, completion: (Boolean) -> Unit) {
        getGroupeById(groupeId) { groupe ->
            if (groupe != null) {
                if (groupe.depenses == null) {
                    groupe.depenses = mutableListOf(depense)
                } else {
                    groupe.depenses?.add(depense)
                }
                groupesRef.child(groupeId).child("usersIds").setValue(groupe.depenses).addOnCompleteListener { task ->
                    completion(task.isSuccessful)
                }
            } else {
                completion(false)
            }
        }
    }
}


