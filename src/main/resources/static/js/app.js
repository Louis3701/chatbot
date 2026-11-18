document.addEventListener('DOMContentLoaded', function () {
  'use strict';

  // ----- Barre latérale (petits écrans) -----
  var barre = document.getElementById('barre');
  var voile = document.getElementById('voile');
  var menu = document.getElementById('btn-menu');

  function basculerBarre(ouvrir) {
    if (!barre) { return; }
    barre.classList.toggle('ouverte', ouvrir);
    if (voile) { voile.classList.toggle('visible', ouvrir); }
    if (menu) { menu.setAttribute('aria-expanded', String(ouvrir)); }
  }
  if (menu) { menu.addEventListener('click', function () { basculerBarre(!barre.classList.contains('ouverte')); }); }
  if (voile) { voile.addEventListener('click', function () { basculerBarre(false); }); }
  document.addEventListener('keydown', function (e) { if (e.key === 'Escape') { basculerBarre(false); } });

  // ----- Zone de saisie (le formulaire est envoyé au serveur Spring) -----
  var form = document.getElementById('form-chat');
  if (form) {
    var saisie = document.getElementById('saisie');
    var envoyer = document.getElementById('btn-envoyer');
    var fil = document.getElementById('fil');
    var attente = document.getElementById('attente');

    function ajuster() {
      saisie.style.height = 'auto';
      saisie.style.height = Math.min(saisie.scrollHeight, 200) + 'px';
      envoyer.disabled = saisie.value.trim() === '';
    }

    saisie.addEventListener('input', ajuster);
    saisie.addEventListener('keydown', function (e) {
      if (e.key === 'Enter' && !e.shiftKey && !e.isComposing) {
        e.preventDefault();
        form.requestSubmit(); // vérifie aussi que le modèle est choisi
      }
    });

    // Pendant l'appel à l'API : on bloque le bouton et on affiche « Le modèle réfléchit… »
    form.addEventListener('submit', function () {
      envoyer.disabled = true;
      if (attente) {
        attente.hidden = false;
        fil.scrollTop = fil.scrollHeight;
      }
    });

    // Retour arrière du navigateur : on remet l'état initial
    window.addEventListener('pageshow', function () {
      if (attente) { attente.hidden = true; }
      ajuster();
    });

    document.querySelectorAll('.suggestion').forEach(function (bouton) {
      bouton.addEventListener('click', function () {
        saisie.value = bouton.getAttribute('data-texte');
        ajuster();
        saisie.focus();
      });
    });

    ajuster();
    fil.scrollTop = fil.scrollHeight; // on affiche le bas de la conversation
  }

  // ----- Formulaire de connexion : pas encore fonctionnel (Sprint 2) -----
  var formConnexion = document.getElementById('form-connexion');
  if (formConnexion) {
    formConnexion.addEventListener('submit', function (e) {
      e.preventDefault();
      document.getElementById('message-connexion').classList.remove('d-none');
    });
  }
});
