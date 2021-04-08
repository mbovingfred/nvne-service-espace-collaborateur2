package fr.nvneservice.espacecollaborateur.web;

import fr.nvneservice.espacecollaborateur.dao.UserCollaborateurRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

//import io.jsonwebtoken.Jwts;
import org.springframework.web.bind.annotation.RequestHeader;
import fr.nvneservice.espacecollaborateur.entities.UserCollaborateur;
import fr.nvneservice.espacecollaborateur.services.UserCollaborateurService;
@RestController
@CrossOrigin("*")
public class CollaborateurRestController {
	
	@Autowired
	private UserCollaborateurService entrepriseService;
	
	@Autowired
	private UserCollaborateurRepository userCollaborateurRepository;


    @RequestMapping("/api/entreprises/downloadCV/{fileName:.+}")
    public void downloadCV( HttpServletRequest request, 
                                     HttpServletResponse response, 
                                     @PathVariable("fileName") String fileName) 
    {
        //If user is not authorized - he should be thrown out from here itself
         
        //Authorized user will download the file
        // String dataDirectory = request.getServletContext().getRealPath("/WEB-INF/downloads/pdf/");
        Path file = Paths.get(System.getProperty("user.home")+"/upload/cv/"+fileName);
        if (Files.exists(file)) 
        {
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename="+fileName);
            try
            {
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
            } 
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
//    @PostMapping(path="/api/entreprises/uploadCV", consumes = "multipart/form-data")
//    @ResponseBody
//    public ResponseEntity<String> uploadCV(@RequestPart("cv") MultipartFile file, @RequestPart("entreprise") UserCollaborateur entreprise ) throws IOException {
//        String filename = file.getOriginalFilename()+" "+System.currentTimeMillis()+".pdf";
//        Path path = Paths.get(System.getProperty("user.home")+"/upload/cv/"+filename);
//        Files.write(path, file.getBytes());
////        entreprise.setCv(path.getFileName().toString());
////        entrepriseRepository.save(entreprise);
//        return ResponseEntity.ok().build();
//    }

    @RequestMapping(value="/api/espacecollaborateur/register/collaborateur",method = RequestMethod.POST)
    public UserCollaborateur profilCollaborateur(
                @RequestPart("cv") MultipartFile cv, 
                @RequestPart("pceId") MultipartFile pceId, 
                @RequestPart("rib") MultipartFile rib, 
                @RequestPart("userCollaborateur") UserCollaborateur e,
                @RequestHeader("Authorization") String token) throws Exception {
        String filename;
        UserCollaborateur ue = userCollaborateurRepository.findByEmail(e.getEmail());
        // si l'utilisateur n'existe pas dans la base de données on utilise l'entité contenue dans la requête
        // l'entité sera par la suite créée plus tard
        ue = ue == null ? e : ue;
        Path path;
        // Ecriture du fichier cv
        if (!cv.isEmpty()){
            // Si l'utilisateur a envoye un nouveau fichier, on supprime l'ancien et on installe le nouveau
            Files.deleteIfExists(Paths.get(System.getProperty("user.home")+"/upload/cv/"+ue.getCv()));
            filename = cv.getOriginalFilename()+" "+System.currentTimeMillis()+".pdf";
            path = Paths.get(System.getProperty("user.home")+"/upload/cv/"+filename);
            Files.write(path, cv.getBytes());
            e.setCv(filename);
        }
        
        // Ecriture du fichier pceId
        if (!pceId.isEmpty()){
            Files.deleteIfExists(Paths.get(System.getProperty("user.home")+"/upload/pceId/"+ue.getPceId()));
            filename = pceId.getOriginalFilename()+" "+System.currentTimeMillis()+".pdf";
            path = Paths.get(System.getProperty("user.home")+"/upload/pceId/"+filename);
            Files.write(path, pceId.getBytes());
            e.setPceId(filename);
        }
        
        // Ecriture du fichier rib
        if (!rib.isEmpty()){
            Files.deleteIfExists(Paths.get(System.getProperty("user.home")+"/upload/rib/"+ue.getRib()));
            filename = rib.getOriginalFilename()+" "+System.currentTimeMillis()+".pdf";
            path = Paths.get(System.getProperty("user.home")+"/upload/rib/"+filename);
            Files.write(path, rib.getBytes());
            e.setRib(filename);
        }
        
//        System.out.println("dateFromMillis: "+new Date(Long.parseLong(e.getBirthdateForm())));
//    	System.out.println("heure: "+Integer.parseInt(e.getHeureFormContact().substring(0, 2))+Integer.parseInt(e.getHeureFormContact().substring(3)));
//    	System.out.println("date: "+e.getBirthdate());
//        UserCollaborateur entreprise = entrepriseRepository.findById(e.getId()).orElseThrow(() -> new Exception("Could not find Resource"));
//    	e.setBirthdate(new Date(Long.parseLong(e.getBirthdateForm())));
//    	entreprise.getDateContact().setHours(Integer.parseInt(e.getHeureFormContact().substring(0, 2)));
//    	entreprise.getDateContact().setMinutes(Integer.parseInt(e.getHeureFormContact().substring(3)));
//    	e.setId(entrepriseRepository.findByEmail(e.getEmail()).getId());
        System.out.println(e);
        if (ue != null) e.setId(ue.getId());
        return userCollaborateurRepository.save(e);
    }
    
    @RequestMapping(value="/api/espacecollaborateur/findByEmail",method = RequestMethod.POST)
    public UserCollaborateur findByEmail( 
                @RequestPart("email") String email,
                @RequestHeader("Authorization") String token) throws Exception {
//        Jwts.builder().
//        System.out.println(Jwts.parser().parse(token.substring(7)));
        // Ecriture du fichier cv
    	return this.userCollaborateurRepository.findByEmail(email);
        
    }
    
//    @RequestMapping(value="/",method = RequestMethod.GET)
//	public ResponseEntity entrepri() {
////    	System.out.println("Registering a new enterprise ...");
////    	UserCollaborateur entreprise = entrepriseService.findByEmail(entrepriseForm.getEmail());
////		if (entreprise != null)
////			throw new RuntimeException("This user already exists");
////		entreprise = new UserCollaborateur();
//        return ResponseEntity.ok().build();
//    }
}
