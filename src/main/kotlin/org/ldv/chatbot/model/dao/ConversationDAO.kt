package org.ldv.chatbot.model.dao

import org.ldv.chatbot.model.entity.Conversation
import org.ldv.chatbot.model.entity.Utilisateur
import org.springframework.data.jpa.repository.JpaRepository

interface ConversationDAO : JpaRepository<Conversation, Long> {

    // Conversations d'un utilisateur, de la plus récemment modifiée à la plus ancienne
    fun findAllByUtilisateurOrderByDateDerniereModificationDesc(utilisateur: Utilisateur): List<Conversation>

    // Retrouve une conversation seulement si elle appartient à cet utilisateur
    fun findByIdAndUtilisateur(id: Long, utilisateur: Utilisateur): Conversation?
}