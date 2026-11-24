
package org.ldv.chatbot.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MainController {

    // Affichage de la page de connexion
    @GetMapping("/login")
    fun login(model: Model): String {
        model.addAttribute("pageConnexion", true)
        return "auth/login"
    }
}
