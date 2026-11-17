package org.ldv.chatbot.model.entity

import jakarta.persistence.*

@Entity
class FourniseurIA(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long? = null,
    @Column(nullable = false)
    var nom: String,
    var description: String,
    var cheminLogo: String,

    // Association avec ModeleIA
    @OneToMany(mappedBy = "fourniseurIA")
    var modeles: MutableList<ModeleIA> = mutableListOf()
) {
}