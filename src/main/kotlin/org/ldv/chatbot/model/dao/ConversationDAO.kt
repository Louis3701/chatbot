package org.ldv.chatbot.model.dao

import org.ldv.chatbot.model.entity.Conversation
import org.ldv.chatbot.model.entity.ModeleIA
import org.ldv.chatbot.model.entity.Utilisateur
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ConversationDAO : JpaRepository<Conversation, Long> {

    // Conversations d'un utilisateur, de la plus récemment modifiée à la plus ancienne
    fun findAllByUtilisateurOrderByDateDerniereModificationDesc(utilisateur: Utilisateur): List<Conversation>

    // Retrouve une conversation seulement si elle appartient à cet utilisateur
    fun findByIdAndUtilisateur(id: Long, utilisateur: Utilisateur): Conversation?

    // Indique si au moins une conversation utilise ce modèle (utile avant de le supprimer).
    // La requête est écrite à la main parce que la propriété de Conversation s'appelle "modele"
    @Query("select case when count(c) > 0 then true else false end from Conversation c where c.modele = :modeleIA")
    fun existsByModeleIA(@Param("modeleIA") modeleIA: ModeleIA): Boolean
}