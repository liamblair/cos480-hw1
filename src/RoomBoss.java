/*
    Liam Blair
    COS 480 - Database Management Systems
    Homework 1
    September 24, 2021 - October 5, 2021
 */

import java.sql.*;


public class RoomBoss {

    private String url = "jdbc:sqlite:db.sqlite";
    private Connection conn = null;
    private int roomId;
    private int facilitiesId;
    private int reservationsId;

    public RoomBoss(){
        // Do I even need this??
        roomId = 0;
        facilitiesId = 0;
        reservationsId = 0;
    }

    public void connect(String username, String password) {
        // TODO username/password close conn
        // confused about connection with username and password

        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Connection Established");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createTables() {
        // TODO might be done
        // not sure if ids are right
        String rooms = "CREATE TABLE IF NOT EXISTS Rooms (\n" +
                "id varchar(20) NOT NULL PRIMARY KEY,\n" +
                "building varchar(20) NOT NULL,\n" +
                "floor integer NOT NULL,\n" +
                "room integer NOT NULL,\n" +
                "directions varchar(100)\n" +
                ");";

        String facilities = "CREATE TABLE IF NOT EXISTS Facilities (\n" +
                "id varchar(20) NOT NULL PRIMARY KEY,\n" +
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
            Statement statement = conn.createStatement();
            statement.execute(rooms);
            statement.execute(facilities);
            statement.execute(reservations);
            System.out.println("Tables Created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void destroyTables() {
        String dropRooms = "DROP TABLE IF EXISTS Rooms;";
        String dropFacilities = "DROP TABLE IF EXISTS Facilities;";
        String dropReservations = "DROP TABLE IF EXISTS Reservations;";

        try {
            Statement statement = conn.createStatement();
            statement.execute(dropRooms);
            statement.execute(dropFacilities);
            statement.execute(dropReservations);
            System.out.println("Tables Destroyed");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public String addRoom(String building, int floor, int room, String directions) {
        // TODO fix keys with previous instance
//        try {
//            roomId = getLastId("Rooms");
//        } catch (NullPointerException e) {
//            System.out.println(e.getMessage());
//            roomId = 0;
//        }

        String insert = "INSERT INTO Rooms(id, building, floor, room, directions) VALUES(?,?,?,?,?)";
        roomId++;
        String identifier = "room" + Integer.toString(roomId);
        System.out.println(identifier);
        try {
            PreparedStatement pstmt = conn.prepareStatement(insert);
            pstmt.setString(1, identifier);
            pstmt.setString(2, building);
            pstmt.setInt(3, floor);
            pstmt.setInt(4, room);
            pstmt.setString(5, directions);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Room added");
        return identifier;
    }

    public String findRoom(String s) {
        // TODO
        return null;
    }

    public String describeRoom(String identifier) {
        // TODO
        return null;
    }

    public String addFacilities(String building, int room, int chairs, int screens, String comments) {
        // TODO
        return "";
    }

    public String addReservation(String start, String end, String building, int room, String name) {
        // TODO
        return "";
    }

    public String describeReservation(int identifier) {
        // TODO
        return null;
    }

    public String makeReservation(String pattern, int length, String building, int room, String name) {
        // TODO
        return "";
    }

    public void matchReservation(String pattern, String building, int room) {
        // TODO
    }

    public void getFreeRoom(String pattern, int length, int chairs, int screens) {
        // TODO
    }

    private int getLastId(String table) {
        // TODO doesnt work -- returns 0 every time
        String request = "SELECT * FROM " + table + " ORDER BY ROWID DESC LIMIT 1";

        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(request);
            System.out.println(rs.getInt("id"));
            return rs.getInt("id");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

}
