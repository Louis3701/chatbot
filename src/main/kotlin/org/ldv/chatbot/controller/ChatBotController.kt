package org.ldv.chatbot.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ChatBotController {

    @GetMapping("/")
    fun accueil(): String = "accueil"

    // Formulaire affiché seulement : la connexion sera réalisée au Sprint 2
    @GetMapping("/connexion")
    fun connexion(): String = "auth/connexion"
}