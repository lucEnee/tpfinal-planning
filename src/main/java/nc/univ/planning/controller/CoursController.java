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

import nc.univ.planning.form.CoursForm;
import nc.univ.planning.model.Cours;
import nc.univ.planning.model.Eleve;
import nc.univ.planning.model.Niveau;
import nc.univ.planning.model.Salle;

@Controller
public class CoursController {
    private static List<Eleve> eleves = new ArrayList<Eleve>();
    private static List<Niveau> niveaux = new ArrayList<Niveau>();
    private static List<Salle> salles = new ArrayList<Salle>();
    private static List<Cours> listecours = new ArrayList<Cours>();

    public static List<Eleve> getEleves() {
        return eleves;
    }

    public static List<Niveau> getNiveaux() {
        return niveaux;
    }

    public static List<Salle> getSalles() {
        return salles;
    }

    @Value("${error.message}")
	private String errorMessage;

	private Connection connection;

    @RequestMapping(value = { "/consultationcours" }, method = RequestMethod.GET)
	public String consultationcours(Model model) {

        try {
			this.connection = DriverManager.getConnection("jdbc:h2:mem:planning", "sa", "");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		model.addAttribute("listecours", listecours);
        model.addAttribute("eleves", eleves);

        Eleve newEleve = new Eleve(connection);

        try {
            eleves = newEleve.selectEleve();
        } catch (SQLException e) {
            e.printStackTrace();
        }

		return "consultationcours";
	}

    @RequestMapping(value = { "/consultationcours" }, method = RequestMethod.POST)
	public String supprcours(Model model,  //
        @ModelAttribute("coursForm") CoursForm coursForm) {

		model.addAttribute("listecours", listecours);

        try {
            this.connection = DriverManager.getConnection("jdbc:h2:mem:planning", "sa", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String id = coursForm.getId();

        Cours newCours = new Cours(connection);

        try {
            newCours.deleteCours(id);
            for (int i = 0; i < listecours.size(); i++) {
                if (listecours.get(i).getId().equals(id)) {
                    listecours.remove(i);
                }
            }
        } catch (SQLException e) {
			e.printStackTrace();
		}

		return "consultationcours";
	}

    @RequestMapping(value = { "/creationcours" }, method = RequestMethod.GET)
	public String showcreationcours(Model model) {

        model.addAttribute("niveaux", niveaux);
        model.addAttribute("salles", salles);


        try {
			this.connection = DriverManager.getConnection("jdbc:h2:mem:planning", "sa", "");
		} catch (SQLException e) {
			e.printStackTrace();
		}

        Niveau niveau = new Niveau(connection);
        Salle salle = new Salle(connection);
		CoursForm coursForm = new CoursForm();
        
        try {
            salles = salle.selectSalle();
            niveaux = niveau.selectNiveau();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		model.addAttribute("coursForm", coursForm);

		return "creationcours";
	}

    @RequestMapping(value = { "/creationcours" }, method = RequestMethod.POST)
	public String saveCours(Model model, //
		@ModelAttribute("coursForm") CoursForm coursForm) {
		
		try {
			this.connection = DriverManager.getConnection("jdbc:h2:mem:planning", "sa", "");
		} catch (SQLException e) {
			e.printStackTrace();
		}


        String id = coursForm.getId();
		String debut = coursForm.getDebut();
        String fin = coursForm.getFin();
        String niveau_scolaire = coursForm.getNiveau_scolaire();
        String salle = coursForm.getSalle();

        System.out.println(debut);

		if (id != null && id.length() > 0 //
				&& debut != null //
                && salle != null && salle.length() > 0 //
                && fin != null //
                && niveau_scolaire != null && niveau_scolaire.length()>0) {
			Cours newCours = new Cours(connection, id, debut, fin, niveau_scolaire, salle);
			
            try {
				newCours.insertCours(id, debut, fin, niveau_scolaire, salle);
                listecours.add(newCours);
			} catch (SQLException e) {
				e.printStackTrace();
			}
            
			

			return "redirect:/consultationcours";
		}

		model.addAttribute("errorMessage", errorMessage);
		return "creationcours";
	}
}
