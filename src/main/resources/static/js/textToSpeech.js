document.addEventListener("DOMContentLoaded", function () {

    // Récupération de la page du chatbot
    const pageChat = document.querySelector(".chat-page");
    selectVoix.addEventListener("change", function () {

        localStorage.setItem(
            "voixChatbot",
            selectVoix.value
        );

    });
    // Vérification de la demande de lecture automatique
    if (pageChat?.dataset.lectureAuto !== "true") {
        return;
    }

    // Vérification de la disponibilité de la synthèse vocale
    if (!("speechSynthesis" in window)) {
        return;
    }

    // Récupération des messages de l'assistant
    const messages = document.querySelectorAll(
        ".message-assistant"
    );

    if (messages.length === 0) {
        return;
    }

    // Sélection du dernier message
    const dernierMessage = messages[messages.length - 1];

    // Création de la lecture vocale
    const lecture = new SpeechSynthesisUtterance(
        dernierMessage.innerText
    );

    lecture.lang = "fr-FR";
    lecture.rate = 1;

    // Arrêt d'une éventuelle lecture précédente
    window.speechSynthesis.cancel();

    // Lancement de la lecture
    window.speechSynthesis.speak(lecture);

});
const selectVoix = document.getElementById("choixVoix");
const synthese = window.speechSynthesis;

function chargerVoix() {

    // Pages sans liste de voix (connexion) ou navigateur sans synthèse vocale
    if (!selectVoix || !synthese) {
        return;
    }

    // On garde la voix déjà choisie si la liste est rechargée
    const valeurActuelle = selectVoix.value;

    // Récupération des voix disponibles
    const voix = synthese.getVoices();

    // Réinitialisation de la liste
    selectVoix.innerHTML =
        '<option value="">Voix par défaut</option>';

    // Sélection des voix françaises
    voix.filter(voix => voix.lang.startsWith("fr"))
        .forEach(voix => {

            // Création d'une option HTML
            const option = document.createElement("option");

            option.value = voix.voiceURI;
            option.textContent = voix.name;

            // Ajout de l'option à la liste
            selectVoix.appendChild(option);
        });

    selectVoix.value = valeurActuelle;
}

// Chargement des voix : certains navigateurs les fournissent après le chargement de la page
if (synthese) {
    chargerVoix();
    synthese.addEventListener("voiceschanged", chargerVoix);
}

document.addEventListener("DOMContentLoaded", function () {

    // Récupération de la page du chatbot
    const pageChat = document.querySelector(".chat-page");

    // Vérification de la demande de lecture automatique
    if (pageChat?.dataset.lectureAuto !== "true") {
        return;
    }

    // Vérification de la disponibilité de la synthèse vocale
    if (!("speechSynthesis" in window)) {
        return;
    }

    // Récupération des messages de l'assistant
    const messages = document.querySelectorAll(".message-assistant");

    if (messages.length === 0) {
        return;
    }

    // Sélection du dernier message
    const dernierMessage = messages[messages.length - 1];

    // Création de la lecture vocale
    const lecture = new SpeechSynthesisUtterance(dernierMessage.innerText);

    lecture.lang = "fr-FR";
    lecture.rate = 1;

    // Ajout de ma part : utiliser la voix choisie dans la liste, s'il y en a une
    const voixChoisie = synthese.getVoices()
        .find(v => v.voiceURI === selectVoix?.value);

    if (voixChoisie) {
        lecture.voice = voixChoisie;
    }


    // Arrêt d'une éventuelle lecture précédente
    window.speechSynthesis.cancel();

    // Lancement de la lecture
    window.speechSynthesis.speak(lecture);

});