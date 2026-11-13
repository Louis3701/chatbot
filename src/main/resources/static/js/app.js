// Formulaire de connexion : pas encore fonctionnel (Sprint 2, Spring Security)
document.addEventListener('DOMContentLoaded', function () {
  var form = document.getElementById('form-connexion');
  if (!form) { return; }
  form.addEventListener('submit', function (e) {
    e.preventDefault();
    document.getElementById('message-connexion').classList.remove('d-none');
  });
});
