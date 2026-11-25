package org.ldv.chatbot.model.dao

import org.ldv.chatbot.model.entity.Utilisateur
import org.springframework.data.jpa.repository.JpaRepository

interface UtilisateurDAO : JpaRepository<Utilisateur, Long> {

    // Retrouve un utilisateur à partir de son e-mail, sans tenir compte des majuscules
    fun findByEmailIgnoreCase(email: String): Utilisateur?
}