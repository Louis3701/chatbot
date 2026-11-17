package org.ldv.chatbot.model.entity

import jakarta.persistence.*

@Entity
class ModeleIA(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long? = null,
    @Column(nullable = false)
    var nom: String,
    var urlAPI: String,
    var description: String,
    var cheminLogo: String,

    // Association avec FourniseurIA (ModeleIA est le maître de l'association)
    @ManyToOne
    @JoinColumn(name = "fourniseur_ia_id", nullable = false)
    var fourniseurIA: FourniseurIA,

    // Association avec Conversation
    @OneToMany(mappedBy = "modele")
    var conversations: MutableList<Conversation> = mutableListOf()
) {
}