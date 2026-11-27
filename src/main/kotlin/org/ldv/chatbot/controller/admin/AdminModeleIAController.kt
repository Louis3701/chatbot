package org.ldv.chatbot.controller.admin

import org.ldv.chatbot.model.dao.ConversationDAO
import org.ldv.chatbot.model.dao.FourniseurIADAO
import org.ldv.chatbot.model.dao.ModeleIADAO
import org.ldv.chatbot.model.entity.ModeleIA
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
// Préfixe commun : toutes les routes de la classe commencent par /admin/modeles ia
@RequestMapping("/admin/modeles-ia")
class AdminModeleIAController(
    // Spring fournit ces DAO tout seul (injection de dépendances)
    private val modeleIADAO: ModeleIADAO,
    private val fourniseurIADAO: FourniseurIADAO,
    private val conversationDAO: ConversationDAO
) {

    // Liste des modèles : GET /admin/modeles-ia
    @GetMapping
    fun index(model: Model): String {
        // Envoie la liste au template sous le nom "modelesIA"
        model.addAttribute("modelesIA", modeleIADAO.findAll())
        // Fichier utilisé : templates/admin/modeleIA/index.html
        return "admin/modeleIA/index"
    }

    // Formulaire de création : GET /admin/modeles-ia/create
    @GetMapping("/create")
    fun create(model: Model): String {
        model.addAttribute("fourniseursIA", fourniseurIADAO.findAll())
        return "admin/modeleIA/create"
    }

    // Enregistrement d'un modèle IA : POST /admin/modeles-ia
    @PostMapping
    fun store(
        // @RequestParam lit le champ du formulaire qui a le même name="..."
        @RequestParam nom: String,
        @RequestParam urlAPI: String,
        @RequestParam description: String,
        @RequestParam cheminLogo: String,
        @RequestParam fourniseurIAId: Long,
        redirectAttributes: RedirectAttributes
    ): String {

        // Contrôle côté serveur : le "required" du formulaire peut être contourné
        if (nom.isBlank() || urlAPI.isBlank()) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Le nom et l'URL sont obligatoires"
            )
        }

        // Le formulaire n'envoie que l'identifiant : on retrouve l'objet fournisseur
        val fourniseurIA = fourniseurIADAO.findById(fourniseurIAId)
            .orElseThrow {
                ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Fournisseur IA introuvable"
                )
            }

        val modeleIA = ModeleIA(
            nom = nom,
            urlAPI = urlAPI,
            description = description,
            cheminLogo = cheminLogo,
            fourniseurIA = fourniseurIA
        )

        // Pas d'identifiant : save() fait un INSERT
        modeleIADAO.save(modeleIA)

        // Message affiché une seule fois sur la page suivante
        redirectAttributes.addFlashAttribute(
            "succes", "Le modèle IA a été créé avec succès."
        )

        // On redirige vers la liste : actualiser la page ne renvoie pas le formulaire
        return "redirect:/admin/modeles-ia"
    }

    // Détails d'un modèle IA : GET /admin/modeles-ia/{id}
    @GetMapping("/{id}")
    // @PathVariable lit le {id} de l'adresse (ex. /admin/modeles-ia/3 donne id = 3)
    fun show(@PathVariable id: Long, model: Model): String {
        // Si le modèle n'existe pas : erreur 404
        val modeleIA = modeleIADAO.findById(id).orElseThrow {
            ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Modèle IA introuvable"
            )
        }
        model.addAttribute("modeleIA", modeleIA)
        return "admin/modeleIA/show"
    }

    // Formulaire de modification : GET /admin/modeles-ia/{id}/edit
    @GetMapping("/{id}/edit")
    fun edit(@PathVariable id: Long, model: Model): String {
        val modeleIA = modeleIADAO.findById(id).orElseThrow {
            ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Modèle IA introuvable"
            )
        }
        model.addAttribute("modeleIA", modeleIA)
        model.addAttribute("fourniseursIA", fourniseurIADAO.findAll())
        return "admin/modeleIA/edit"
    }

    // Modification : PUT /admin/modeles-ia/{id}
    // (le navigateur envoie un POST avec un champ caché _method=PUT)
    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestParam nom: String,
        @RequestParam urlAPI: String,
        @RequestParam description: String,
        @RequestParam cheminLogo: String,
        @RequestParam fourniseurIAId: Long,
        redirectAttributes: RedirectAttributes
    ): String {
        if (nom.isBlank() || urlAPI.isBlank()) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Le nom et l'URL sont obligatoires"
            )
        }
        val modeleIA = modeleIADAO.findById(id).orElseThrow {
            ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Modèle IA introuvable"
            )
        }
        val fourniseurIA = fourniseurIADAO.findById(fourniseurIAId)
            .orElseThrow {
                ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Fournisseur IA introuvable"
                )
            }

        // On modifie l'objet existant : il garde son identifiant
        modeleIA.nom = nom
        modeleIA.urlAPI = urlAPI
        modeleIA.description = description
        modeleIA.cheminLogo = cheminLogo
        modeleIA.fourniseurIA = fourniseurIA

        // Avec un identifiant, save() fait un UPDATE (pas de nouvelle ligne)
        modeleIADAO.save(modeleIA)

        redirectAttributes.addFlashAttribute(
            "succes", "Le modèle IA a été modifié avec succès."
        )
        return "redirect:/admin/modeles-ia"
    }
    @DeleteMapping("/{id}")
    fun destroy(
        @PathVariable id: Long,
        redirectAttributes: RedirectAttributes
    ): String {
        val modeleIA = modeleIADAO.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND,
                "Modèle IA introuvable")
        }
        if (conversationDAO.existsByModeleIA(modeleIA)) {
            redirectAttributes.addFlashAttribute("erreur",
                "Ce modèle IA est utilisé dans une conversation.")
        } else {
            try {
                modeleIADAO.delete(modeleIA)
                redirectAttributes.addFlashAttribute("succes",
                    "Le modèle IA a été supprimé avec succès.")
            } catch (exception: DataIntegrityViolationException) {
                redirectAttributes.addFlashAttribute("erreur",
                    "Ce modèle IA ne peut pas être supprimé.")
            }
        }
        return "redirect:/admin/modeles-ia"
    }
}