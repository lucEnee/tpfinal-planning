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

import nc.univ.planning.form.SalleForm;
import nc.univ.planning.model.Salle;

@Controller
public class SalleController {
    private static List<Salle> salles = new ArrayList<Salle>();

	public static List<Salle> getSalles() {
		return salles;
	}

	@Value("${error.message}")
	private String errorMessage;

	private Connection connection;

    @RequestMapping(value = { "/consultationsalle" }, method = RequestMethod.GET)
	public String consultationsalle(Model model) {

		model.addAttribute("salles", salles);

		return "consultationsalle";
	}

	@RequestMapping(value = { "/consultationsalle" }, method = RequestMethod.POST)
	public String supprcours(Model model,  //
        @ModelAttribute("salleForm") SalleForm salleForm) {

		model.addAttribute("salles", salles);

        try {
            this.connection = DriverManager.getConnection("jdbc:h2:mem:planning", "sa", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String nom = salleForm.getNom();

        Salle newSalle = new Salle(connection);

        try {
            newSalle.deleteSalle(nom);
            for (int i = 0; i < salles.size(); i++) {
                if (salles.get(i).getNom().equals(nom)) {
                    salles.remove(i);
                }
            }
        } catch (SQLException e) {
			e.printStackTrace();
		}

		return "consultationsalle";
	}

    @RequestMapping(value = { "/creationsalle" }, method = RequestMethod.GET)
	public String showcreationsalle(Model model) {

		SalleForm salleForm = new SalleForm();
		model.addAttribute("salleForm", salleForm);

		return "creationsalle";
	}

    @RequestMapping(value = { "/creationsalle" }, method = RequestMethod.POST)
	public String savesalle(Model model, //
		@ModelAttribute("salleForm") SalleForm salleForm) {
		
		try {
			this.connection = DriverManager.getConnection("jdbc:h2:mem:planning", "sa", "");
		} catch (SQLException e) {
			e.printStackTrace();
		}


		String nom = salleForm.getNom();
		Integer capacite = salleForm.getCapacite();

		if (nom != null && nom.length() > 0 //
				&& capacite != 0) {
			Salle newSalle = new Salle(connection, nom, capacite);
			try {
				newSalle.insertSalle(nom, capacite);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			salles.add(newSalle);

			return "redirect:/consultationsalle";
		}

		model.addAttribute("errorMessage", errorMessage);
		return "creationsalle";
	}
}
