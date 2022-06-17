package nc.univ.planning.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public class Cours {
    
    private String id;

    private String debut;

    private String fin;

    private String niveau_scolaire;

    private String salle;

    private final Connection connection;

    public Cours(Connection connection){
        this.connection = connection;
    }

    public Cours(Connection connection, String id, String debut, String fin, String niveau_scolaire, String salle){
        this.connection = connection;
        this.id = id;
        this.debut = debut;
        this.fin = fin;
        this.niveau_scolaire = niveau_scolaire;
        this.salle = salle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDebut() {
        return debut;
    }

    public void setDebut(String debut) {
        this.debut = debut;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

    public String getNiveau_scolaire() {
        return niveau_scolaire;
    }

    public void setNiveau_scolaire(String niveau_scolaire) {
        this.niveau_scolaire = niveau_scolaire;
    }

    public String getSalle() {
        return salle;
    }

    public void setSalle(String salle) {
        this.salle = salle;
    }

    public void insertCours(String id, String debut, String fin, String niveau_scolaire, String salle) throws SQLException {
        try (var statement = connection.prepareStatement("INSERT INTO cours (ID, DEBUT, FIN, NIVEAU_SCOLAIRE, SALLE) VALUES (?, ?, ?, ?, ?)")) {
            statement.setString(1, id);
            statement.setString(2, debut);
            statement.setString(3, fin);
            statement.setString(4, niveau_scolaire);
            statement.setString(5, salle);
            statement.execute();
        }
    }

    public List<Cours> selectCours() throws SQLException {
        List<Cours> listecours = new ArrayList<Cours>();
        try(Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM cours");
            while(rs.next()){
                listecours.add(new Cours(this.connection, rs.getString("ID"), rs.getString("DEBUT"), rs.getString("FIN"), rs.getString("NIVEAU_SCOLAIRE"), rs.getString("SALLE")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listecours;
    }

    public void deleteCours(String id) throws SQLException {
        try(Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM cours WHERE `ID`='"+ id +"'");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
