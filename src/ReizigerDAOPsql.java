import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ReizigerDAOPsql implements ReizigerDAO{
    private Connection connectie = Main.getConnection();

    public ReizigerDAOPsql(Connection connectie) throws SQLException {
        this.connectie = connectie;
    }

    //opslaan van de reiziger
    @Override
    public boolean save(Reiziger reiziger) {
        try {
            PreparedStatement preparedStatement = connectie.prepareStatement("INSERT INTO reiziger values (?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, reiziger.getId());
            preparedStatement.setString(2, reiziger.getVoorletters());
            preparedStatement.setString(3, reiziger.getTussenvoegsel());
            preparedStatement.setString(4, reiziger.getAchternaam());
            preparedStatement.setDate(5, (Date) reiziger.getGeboortedatum());
            return preparedStatement.execute();
        } catch (SQLException sqlException) {
            System.out.println(sqlException + " fout bij opslaan!");
            return false;
        }
    }

    //updaten van de reizigers gegevens
    @Override
    public boolean update(Reiziger reiziger) {
        try {
            PreparedStatement preparedStatement = connectie.prepareStatement("UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?");
            preparedStatement.setString(1, reiziger.getVoorletters());
            preparedStatement.setString(2, reiziger.getTussenvoegsel());
            preparedStatement.setString(3, reiziger.getAchternaam());
            preparedStatement.setDate(4, (Date) reiziger.getGeboortedatum());
            preparedStatement.setInt(5, reiziger.getId());
            return preparedStatement.execute();
        } catch (SQLException sqlException) {
            System.out.println(sqlException + " fout bij updaten!");
            return false;
        }
    }

    //verwijderen van de reiziger
    @Override
    public boolean delete(Reiziger reiziger) {
        try {
            PreparedStatement preparedStatement = connectie.prepareStatement("DELETE FROM reiziger WHERE reiziger_id = ?");
            preparedStatement.setInt(1, reiziger.getId());
            return preparedStatement.execute();
        } catch (SQLException sqlException) {
            System.out.println(sqlException + " fout bij verwijderen!");
            return false;
        }
    }

    //vinden door de id
    @Override
    public Reiziger findById(int id) {
        try {
            PreparedStatement preparedStatement = connectie.prepareStatement("SELECT * FROM reiziger WHERE reiziger_id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            //vooraf gedefinieerde waardes
            String voorletters = null;
            String tussenvoegsel = null;
            String achternaam = null;
            Date geboortedatum = null;
            Reiziger reiziger;

            //als een volgende waarde bestaat
            while (resultSet.next()) {
                voorletters = resultSet.getString("voorletters");
                tussenvoegsel = resultSet.getString("tussenvoegsel");
                achternaam = resultSet.getString("achternaam");
                geboortedatum = resultSet.getDate("geboortedatum");
            }
            //plaats de volgende waardes en return die
            reiziger = new Reiziger(id, voorletters, tussenvoegsel, achternaam, geboortedatum);
            return reiziger;
        } catch (SQLException sqlException) {
            System.out.println(sqlException + " fout bij vinden door de volgende id:"  + id);
            return null;
        }
    }

    //vinden door de geboortedatum
    @Override
    public List<Reiziger> findByGbDatum(String datum) {
        try {
            List<Reiziger> opDatum = new ArrayList<>();
            PreparedStatement preparedStatement = connectie.prepareStatement("SELECT * FROM reiziger WHERE geboortedatum = ?");
            preparedStatement.setDate(1, Date.valueOf(datum));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("reiziger_id");
                int reizigerId = Integer.parseInt(id);
                String voorletters = resultSet.getString("voorletters");
                String tussenvoegsel = resultSet.getString("tussenvoegsel");
                String achternaam = resultSet.getString("achternaam");
                Date geboortedatum = resultSet.getDate("geboortedatum");
                Reiziger reiziger = new Reiziger(reizigerId, voorletters, tussenvoegsel, achternaam, geboortedatum);
                opDatum.add(reiziger);
            }
            return opDatum;
        } catch (SQLException sqlException) {
            System.out.println(sqlException + " fout bij vinden door geboortedatum!");
            return null;
        }
    }

    //vind alle
    @Override
    public List<Reiziger> findAll() {
        try {
            List<Reiziger> alleReizigers = new ArrayList<>();
            PreparedStatement preparedStatement = connectie.prepareStatement("SELECT * FROM reiziger;");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("reiziger_id");
                int reizigerId = Integer.parseInt(id);
                String voorletters = resultSet.getString("voorletters");
                String tussenvoegsel = resultSet.getString("tussenvoegsel");
                String achternaam = resultSet.getString("achternaam");
                Date geboortedatum = resultSet.getDate("geboortedatum");
                Reiziger reiziger = new Reiziger(reizigerId, voorletters, tussenvoegsel, achternaam, geboortedatum);
                alleReizigers.add(reiziger);
            }
            return alleReizigers;
        } catch (SQLException sqlException) {
            System.out.println(sqlException + " fout bij vinden findAll()!");
            return null;
        }
    }
}