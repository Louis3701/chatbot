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
    var dateDerniereModification: LocalDateTime,

    // Association avec ModeleIA (Conversation est le maître de l'association)
    @ManyToOne
    @JoinColumn(name = "modele_id", nullable = false)
    var modele: ModeleIA,

    // Association avec Message
    @OneToMany(mappedBy = "conversation", cascade = [CascadeType.ALL], orphanRemoval = true)
    var messages: MutableList<Message> = mutableListOf()
) {

}