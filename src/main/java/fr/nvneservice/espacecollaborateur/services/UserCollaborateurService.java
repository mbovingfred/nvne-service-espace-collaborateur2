package fr.nvneservice.espacecollaborateur.services;

import fr.nvneservice.espacecollaborateur.entities.UserCollaborateur;

public interface UserCollaborateurService {
	public UserCollaborateur findByEmail(String email);
	public UserCollaborateur registerEntreprise(UserCollaborateur entreprise);
}
