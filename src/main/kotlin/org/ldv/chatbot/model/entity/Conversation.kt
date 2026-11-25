package org.ldv.chatbot.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Conversation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long? = null,
    @Column(nullable = false)
    var titre: String,
    var dateCreation: LocalDateTime,
    var dateDerniereModification: LocalDateTime = LocalDateTime.now(),

    // Association avec ModeleIA (Conversation est le maître de l'association)
    @ManyToOne
    @JoinColumn(name = "modele_id", nullable = false)
    var modele: ModeleIA,

    // Association avec Utilisateur : une conversation appartient obligatoirement à un seul utilisateur
    @ManyToOne(optional = false)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    var utilisateur: Utilisateur,

    // Association avec Message
    @OneToMany(mappedBy = "conversation", cascade = [CascadeType.ALL], orphanRemoval = true)
    var messages: MutableList<Message> = mutableListOf()
) {
}