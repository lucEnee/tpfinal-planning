package nc.univ.planning.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

public class Niveau {

    private String id;

    private String libelle;

    private final Connection connection;

    public Niveau(Connection connection){
        this.connection = connection;
    }

    public Niveau(Connection connection, String id, String libelle){
        this.id = id;
        this.libelle = libelle;
        this.connection = connection;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public List<Niveau> selectNiveau() throws SQLException {
        List<Niveau> niveaux = new ArrayList<Niveau>();
        try(Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM niveaux");
            while(rs.next()){
                niveaux.add(new Niveau(this.connection, rs.getString("ID"), rs.getString("LIBELLE")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return niveaux;
    }
}
