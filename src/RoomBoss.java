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
    }

    public void connect(String username, String password) {
        // TODO close conn
        // still confused about username/password should the db correspond to the username?
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Connection Established");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        roomId = getLastId("Rooms");
        facilitiesId = getLastId("Facilities");
        reservationsId = getLastId("Reservations");

        System.out.println(functionTermination);
    }

    public void createTables() {
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
            roomId = 0;
            facilitiesId = 0;
            reservationsId = 0;
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(functionTermination);
    }

    public void addRoom(String building, int floor, int room, String directions) {
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
        // TODO grab the buildings and directions from the result set and search/sort
        String request = "SELECT * FROM Rooms";

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(request);


            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(functionTermination);
    }

    public void describeRoom(String identifier) {
        // TODO works :) prints result set closed sometimes with previous instances on room6
        String request = "SELECT * FROM Rooms WHERE id = ?;";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(request);
            preparedStatement.setString(1, identifier);
            ResultSet rs = preparedStatement.executeQuery();

            if (!rs.next()) {
                System.out.println(functionTermination);
                rs.close();
                preparedStatement.close();
                return;
            }

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
        String request = "SELECT * FROM Reservations WHERE cnum = ?;";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(request);
            preparedStatement.setString(1, identifier);
            ResultSet rs = preparedStatement.executeQuery();

            if (!rs.next()) {
                System.out.println(functionTermination);
                rs.close();
                preparedStatement.close();
                return;
            }

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
        String request = "SELECT * FROM " + table + " ORDER BY ROWID DESC LIMIT 1;";
        String id = "";

        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(request);
            if (table.equals("Rooms") || table.equals("Facilities")) {
                id = rs.getString("id");
            } else {
                id = rs.getString("cnum");
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        if (table.equals("Rooms")) {
            try {
                id = id.substring(4);
            } catch (IndexOutOfBoundsException e) {
                return 0;
            }
            return Integer.valueOf(id);
        } else if (table.equals("Facilities")) {
            try {
                id = id.substring(8);
            } catch (IndexOutOfBoundsException e) {
                return 0;
            }
            return Integer.valueOf(id);
        } else {
            try {
                id = id.substring(11);
            } catch (IndexOutOfBoundsException e) {
                return 0;
            }
            return Integer.valueOf(id);
        }
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
    }
}
