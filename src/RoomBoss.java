/*
    Liam Blair
    COS 480 - Database Management Systems
    September 24, 2021 - October 5, 2021
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class RoomBoss {

    private String url = "jdbc:sqlite:db.sqlite";

    public RoomBoss(){
        // Do I even need this??
    }

    public void connect(String username, String password) {
        // TODO username/password
        // confused about connection with username and password


        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Connection Established");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void createTables() {
        // TODO
        // not sure if ids are right
        String rooms = "CREATE TABLE IF NOT EXISTS Rooms (\n" +
                "id integer PRIMARY KEY,\n" +
                "building varchar(20) NOT NULL,\n" +
                "floor integer NOT NULL,\n" +
                "room integer NOT NULL,\n" +
                "directions varchar(100)\n" +
                ");";

        String facilities = "CREATE TABLE IF NOT EXISTS Facilities (\n" +
                "id integer PRIMARY KEY,\n" +
                "building varchar(20) NOT NULL,\n" +
                "room integer NOT NULL,\n" +
                "chair integer NOT NULL,\n" +
                "screens integer NOT NULL,\n" +
                "comments text\n" +
                ");";

        String reservations = "CREATE TABLE IF NOT EXISTS Reservations (\n" +
                "cnum integer PRIMARY KEY,\n" +
                "rstart text NOT NULL,\n" +
                "rend text NOT NULL,\n" +
                "building varchar(20) NOT NULL,\n" +
                "room integer NOT NULL,\n" +
                "name text NOT NULL\n" +
                ");";

        try {
            Connection conn = DriverManager.getConnection(url);
            Statement statement = conn.createStatement();
            statement.execute(rooms);
            statement.execute(facilities);
            statement.execute(reservations);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void destroyTables() {
        // TODO
    }

    public int addRoom(String building, int floor, int room, String directions) {
        // TODO
        return 0;
    }

    public String findRoom(String s) {
        // TODO
        return null;
    }

    public String describeRoom(int identifier) {
        // TODO
        return null;
    }

    public int addFacilities(String building, int room, int chairs, int screens, String comments) {
        // TODO
        return 0;
    }

    public int addReservation(String start, String end, String building, int room, String name) {
        // TODO
        return 0;
    }

    public String describeReservation(int identifier) {
        // TODO
        return null;
    }

    public int makeReservation(String pattern, int length, String building, int room, String name) {
        // TODO
        return 0;
    }

    public void matchReservation(String pattern, String building, int room) {
        // TODO
    }

    public void getFreeRoom(String pattern, int length, int chairs, int screens) {
        // TODO
    }

}
