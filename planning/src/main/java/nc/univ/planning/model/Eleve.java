package nc.univ.planning.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Eleve {

    private Integer id;
    
    private String nom;

    private String prenom;

    private Integer age;

    private String niveau_scolaire;

    private final Connection connection;

    public Eleve(Connection connection){
        this.connection = connection;
    }

    public Eleve(Connection connection,Integer id, String nom, String prenom, Integer age, String niveau_scolaire){
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.niveau_scolaire = niveau_scolaire;
        this.connection = connection;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getNiveau_scolaire() {
        return niveau_scolaire;
    }

    public void setNiveau_scolaire(String niveau_scolaire) {
        this.niveau_scolaire = niveau_scolaire;
    }

    public void insertEleve(Integer id, String nom, String prenom, Integer age, String niveau_scolaire) throws SQLException {
        try (var statement = connection.prepareStatement("INSERT INTO eleve (ID, NOM, PRENOM, AGE, NIVEAU_SCOLAIRE) VALUES (?, ?, ?, ?, ?)")) {
            statement.setInt(1, id);
            statement.setString(2, nom);
            statement.setString(3, prenom);
            statement.setInt(4, age);
            statement.setString(5, niveau_scolaire);
            statement.execute();
        }
    }

    public List<Eleve> selectEleve() throws SQLException {
        List<Eleve> eleves = new ArrayList<Eleve>();
        try(Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM eleve");
            while(rs.next()){
                eleves.add(new Eleve(this.connection, rs.getInt("ID"), rs.getString("NOM"), rs.getString("PRENOM"), rs.getInt("AGE"), rs.getString("NIVEAU_SCOLAIRE")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eleves;
    }

    public void deleteEleve(Integer id) throws SQLException {
        try(Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM eleve WHERE `ID`="+ id);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
