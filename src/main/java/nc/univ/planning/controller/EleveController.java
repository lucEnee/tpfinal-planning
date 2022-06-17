package nc.univ.planning.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import nc.univ.planning.form.EleveForm;
import nc.univ.planning.model.Eleve;
import nc.univ.planning.model.Niveau;

@Controller
public class EleveController {
    private static List<Eleve> eleves = new ArrayList<Eleve>();
    private static List<Niveau> niveaux = new ArrayList<Niveau>();

    public static List<Eleve> getEleves() {
        return eleves;
    }

    public static List<Niveau> getNiveaux() {
        return niveaux;
    }

    @Value("${error.message}")
	private String errorMessage;

	private Connection connection;

    @RequestMapping(value = { "/consultationeleve" }, method = RequestMethod.GET)
	public String consultationeleve(Model model) {

		model.addAttribute("eleves", eleves);

		return "consultationeleve";
	}

    @RequestMapping(value = { "/consultationeleve" }, method = RequestMethod.POST)
	public String suppreleve(Model model,  //
        @ModelAttribute("eleveForm") EleveForm eleveForm) {

		model.addAttribute("eleves", eleves);

        try {
            this.connection = DriverManager.getConnection("jdbc:h2:mem:planning", "sa", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int id = eleveForm.getId();

        Eleve newEleve = new Eleve(connection);

        try {
            newEleve.deleteEleve(id);
            for (int i = 0; i < eleves.size(); i++) {
                if (eleves.get(i).getId() == id) {
                    eleves.remove(i);
                }
            }
        } catch (SQLException e) {
			e.printStackTrace();
		}

		return "consultationeleve";
	}

    @RequestMapping(value = { "/creationeleve" }, method = RequestMethod.GET)
	public String showcreationeleve(Model model) {

        model.addAttribute("niveaux", niveaux);

        try {
			this.connection = DriverManager.getConnection("jdbc:h2:mem:planning", "sa", "");
		} catch (SQLException e) {
			e.printStackTrace();
		}

        Niveau niveau = new Niveau(connection);
		EleveForm eleveForm = new EleveForm();
        
        try {
            niveaux = niveau.selectNiveau();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		model.addAttribute("eleveForm", eleveForm);

		return "creationeleve";
	}

    @RequestMapping(value = { "/creationeleve" }, method = RequestMethod.POST)
	public String saveEleve(Model model, //
		@ModelAttribute("eleveForm") EleveForm eleveForm) {
		
		try {
			this.connection = DriverManager.getConnection("jdbc:h2:mem:planning", "sa", "");
		} catch (SQLException e) {
			e.printStackTrace();
		}


        Integer id = eleveForm.getId();
		String nom = eleveForm.getNom();
        String prenom = eleveForm.getPrenom();
		Integer age = eleveForm.getAge();
        String niveau_scolaire = eleveForm.getNiveau_scolaire();

		if (nom != null && nom.length() > 0 //
				&& id != 0 //
                && prenom != null && prenom.length() > 0 //
                && age > 0 //
                && niveau_scolaire != null && niveau_scolaire.length()>0) {
			Eleve newEleve = new Eleve(connection, id, nom, prenom, age, niveau_scolaire);
			
            try {
				newEleve.insertEleve(id, nom, prenom, age, niveau_scolaire);
                eleves.add(newEleve);
			} catch (SQLException e) {
				e.printStackTrace();
			}
            
			

			return "redirect:/consultationeleve";
		}

		model.addAttribute("errorMessage", errorMessage);
		return "creationeleve";
	}
}
