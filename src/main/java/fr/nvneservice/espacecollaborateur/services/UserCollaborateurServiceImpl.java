package fr.nvneservice.espacecollaborateur.services;

import fr.nvneservice.espacecollaborateur.dao.UserCollaborateurRepository;
import fr.nvneservice.espacecollaborateur.entities.UserCollaborateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCollaborateurServiceImpl implements UserCollaborateurService {

	@Autowired
	private UserCollaborateurRepository userCollaborateurRepository;
	
	@Override
	public UserCollaborateur registerEntreprise(UserCollaborateur userCollaborateur) {
		return this.userCollaborateurRepository.save(userCollaborateur);
	}

	@Override
	public UserCollaborateur findByEmail(String email) {
		return userCollaborateurRepository.findByEmail(email);
	}
}
