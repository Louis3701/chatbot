import org.ldv.chatbot.model.dao.ConversationDAO
import org.ldv.chatbot.model.dao.FourniseurIADAO
import org.ldv.chatbot.model.dao.ModeleIADAO
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/admin/modeles-ia")
class AdminModeleIAController(
    private val modeleIADAO: ModeleIADAO,
    private val fourniseurIADAO: FourniseurIADAO,
    private val conversationDAO: ConversationDAO
) {
    @GetMapping
    fun index(model: Model): String {
        model.addAttribute("modelesIA", modeleIADAO.findAll())
        return "admin/modeleIA/index"
    }
}