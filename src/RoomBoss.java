/*
    Liam Blair
    COS 480 - Database Management Systems
    Homework 1
    September 24, 2021 - October 5, 2021
 */

import java.sql.*;


public class RoomBoss {

    private final String url = "jdbc:sqlite:db.sqlite";
    private final String functionTermination = "-----";
    private final String separator = " : ";
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
        System.out.println(functionTermination);
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
                "chairs integer NOT NULL,\n" +
                "screens integer NOT NULL,\n" +
                "comments text\n" +
                ");";

        String reservations = "CREATE TABLE IF NOT EXISTS Reservations (\n" +
                "cnum varchar(20) PRIMARY KEY,\n" +
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
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(functionTermination);
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
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(functionTermination);
    }

    public void addRoom(String building, int floor, int room, String directions) {
        // TODO fix keys with previous instance
//        try {
//            roomId = getLastId("Rooms");
//        } catch (NullPointerException e) {
//            System.out.println(e.getMessage());
//            roomId = 0;
//        }

        String insert = "INSERT INTO Rooms(id, building, floor, room, directions) VALUES(?,?,?,?,?);";
        roomId++;
        String identifier = "room" + Integer.toString(roomId);
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insert);
            preparedStatement.setString(1, identifier);
            preparedStatement.setString(2, building);
            preparedStatement.setInt(3, floor);
            preparedStatement.setInt(4, room);
            preparedStatement.setString(5, directions);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(identifier);
        System.out.println(functionTermination);
    }

    public void findRoom(String s) {
        // TODO
    }

    public void describeRoom(String identifier) {
        // TODO works :) prints "ResultSet closed" if nothing found tho - should print nothing
        String request = "SELECT * FROM Rooms WHERE id = ?;";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(request);
            preparedStatement.setString(1, identifier);
            ResultSet rs = preparedStatement.executeQuery();

            // this is throwing a column out of bounds
//            if (rs.wasNull()) {
//                System.out.println(functionTermination);
//                return;
//            }

            String building = rs.getString("building");
            int roomInt = rs.getInt("room");
            String room = Integer.toString(roomInt);
            String floor = Integer.toString(rs.getInt("floor"));

            String facilitiesRequest = "SELECT chairs, screens from Facilities WHERE building = ? AND room = ?;";
            PreparedStatement facilitiesPStmt = conn.prepareStatement(facilitiesRequest);
            facilitiesPStmt.setString(1, building);
            facilitiesPStmt.setInt(2, roomInt);
            ResultSet facilitiesRS = facilitiesPStmt.executeQuery();

            String chairs = Integer.toString(facilitiesRS.getInt("chairs"));
            String screens = Integer.toString(facilitiesRS.getInt("screens"));

            String description = building + separator
                    + room + separator
                    + floor + separator
                    + chairs + separator
                    + screens;

            System.out.println(description);
            preparedStatement.close();
            facilitiesPStmt.close();
            rs.close();
            facilitiesRS.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(functionTermination);
    }

    public void addFacilities(String building, int room, int chairs, int screens) {
        // TODO do i not insert comments? should be done
        // that error is annoying me
        String insert = "INSERT INTO Facilities(id, building, room, chairs, screens) VALUES(?,?,?,?,?);";
        facilitiesId++;
        String identifier = "facility" + Integer.toString(facilitiesId);

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insert);
            preparedStatement.setString(1, identifier);
            preparedStatement.setString(2, building);
            preparedStatement.setInt(3, room);
            preparedStatement.setInt(4, chairs);
            preparedStatement.setInt(5, screens);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(identifier);
        System.out.println(functionTermination);
    }

    public void addReservation(String start, String end, String building, int room, String name) {
        // TODO maybe change the date data type
        String insert = "INSERT INTO Reservations(cnum, rstart, rend, building, room, name) VALUES(?,?,?,?,?,?);";
        reservationsId++;
        String identifier = "reservation" + Integer.toString(reservationsId);

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insert);
            preparedStatement.setString(1, identifier);
            preparedStatement.setString(2, start);
            preparedStatement.setString(3, end);
            preparedStatement.setString(4, building);
            preparedStatement.setInt(5, room);
            preparedStatement.setString(6, name);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(identifier);
        System.out.println(functionTermination);
    }

    public void describeReservation(String identifier) {
        // TODO prints "ResultSet closed" if nothing found - should print nothing
        String request = "SELECT * FROM Reservations WHERE cnum = ?;";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(request);
            preparedStatement.setString(1, identifier);
            ResultSet rs = preparedStatement.executeQuery();

            String building = rs.getString("building");
            String room = Integer.toString(rs.getInt("room"));
            String start = rs.getString("rstart");
            String end = rs.getString("rend");
            String name = rs.getString("name");

            String description = building + separator
                    + room + separator
                    + start + separator
                    + end + separator
                    + name;

            System.out.println(description);
            preparedStatement.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(functionTermination);
    }

    public void makeReservation(String pattern, int length, String building, int room, String name) {
        // TODO
    }

    public void matchReservation(String pattern, String building, int room) {
        // TODO
    }

    public void getFreeRoom(String pattern, int length, int chairs, int screens) {
        // TODO
    }

    private int getLastId(String table) {
        // TODO doesnt work -- returns 0 every time
        String request = "SELECT * FROM " + table + " ORDER BY ROWID DESC LIMIT 1;";

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
