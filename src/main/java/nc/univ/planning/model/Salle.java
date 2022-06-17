package nc.univ.planning.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Salle {
    
    private String nom;

    private Integer capacite;

    private final Connection connection;

    public Salle(Connection connection){
        this.connection = connection;
    }

    public Salle(Connection connection, String nom, Integer capacite){
        this.nom = nom;
        this.capacite = capacite;
        this.connection = connection;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getCapacite() {
        return capacite;
    }

    public void setCapacite(Integer capacite) {
        this.capacite = capacite;
    }

    public void insertSalle(String nom, Integer capacite) throws SQLException {
        try (var statement = connection.prepareStatement("INSERT INTO salle (NOM, CAPACITE) VALUES (?, ?)")) {
            statement.setString(1, nom);
            statement.setDouble(2, capacite);
            statement.execute();
        }
    }

    public List<Salle> selectSalle() throws SQLException {
        List<Salle> salles = new ArrayList<Salle>();
        try(Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM salle");
            while(rs.next()){
                salles.add(new Salle(this.connection, rs.getString("NOM"), rs.getInt("CAPACITE")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salles;
    }

    public void deleteSalle(String nom) throws SQLException {
        try(Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM salle WHERE `NOM`='"+ nom+"'");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

}
