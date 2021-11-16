/*
    Liam Blair
    COS 480 - Database Management Systems
    Homework 1
    September 24, 2021 - October 14, 2021
 */

import com.cronutils.model.Cron;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;

import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RoomBoss {

    private final String url = "jdbc:sqlite:db.sqlite";
    private final String functionTermination = "-----";
    private final String separator = " : ";
    private Connection conn = null;
    private int roomId;
    private int facilitiesId;
    private int reservationsId;
    private CronDefinition definition;
    private ZonedDateTime start;
    private CronParser parser;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd[' '][HH:mm[:ss]]");
    private LocalDate limit;
    private ZonedDateTime zLimit;

    public RoomBoss(){
    }

    public void connect(String username, String password) {
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        roomId = getLastId("Rooms");
        facilitiesId = getLastId("Facilities");
        reservationsId = getLastId("Reservations");

        definition = CronDefinitionBuilder.defineCron().withMinutes().and().withHours().and()
                .withDayOfMonth().and().withMonth().and().withDayOfWeek().withValidRange(0, 7)
                .withMondayDoWValue(1).and().withYear().withValidRange(1970, 2018).and().instance();

        start = ZonedDateTime.of(1970, 01, 01,
                0, 0, 0, 0, ZoneId.of("UTC"));

        parser = new CronParser(definition);

        limit = LocalDate.parse("2018-12-31 23:59:59", formatter);
        zLimit = limit.atStartOfDay(ZoneOffset.UTC);

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
        String identifier = "room" + roomId;
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
        String request = "SELECT * FROM Rooms";

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(request);

            s = s.toLowerCase();
            ArrayList<Room> list = new ArrayList<>();

            while (resultSet.next()) {
                Room room = new Room(resultSet.getString("id"),
                        resultSet.getString("building"),
                        resultSet.getInt("floor"),
                        resultSet.getInt("room"),
                        resultSet.getString("directions"));

                String building = room.getBuilding().toLowerCase();
                String directions = room.getDirections().toLowerCase();

                if (building.contains(s) || directions.contains(s)) {
                    list.add(room);
                }
            }

            list.sort(new Comparator<Room>() {
                @Override
                public int compare(Room o1, Room o2) {
                    String building1 = o1.getBuilding().toLowerCase();
                    String building2 = o2.getBuilding().toLowerCase();

                    if (building1.equals(building2)) {
                        return Integer.compare(o1.getRoom(), o2.getRoom());
                    } else {
                        return building1.compareTo(building2);
                    }
                }
            });

            for (Room room : list) {
                String description = room.getId() + separator +
                        room.getBuilding() + separator +
                        room.getRoom();
                System.out.println(description);
            }

            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(functionTermination);
    }

    public void describeRoom(String identifier) {
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
        String insert = "INSERT INTO Facilities(id, building, room, chairs, screens) VALUES(?,?,?,?,?);";
        facilitiesId++;
        String identifier = "facility" + facilitiesId;

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
        String insert = "INSERT INTO Reservations(cnum, rstart, rend, building, room, name) VALUES(?,?,?,?,?,?);";
        reservationsId++;
        String identifier = "reservation" + reservationsId;

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
        Cron cron = parser.parse(pattern);
        ExecutionTime executionTime = ExecutionTime.forCron(cron);

        String insert = "INSERT INTO Reservations(cnum, rstart, rend, building, room, name) VALUES(?,?,?,?,?,?);";

        if (!executionTime.nextExecution(start).isPresent()) {
            return;
        }

        ZonedDateTime last = executionTime.nextExecution(start).get();

        while (executionTime.nextExecution(last).isPresent()) {
            if (last.isAfter(zLimit)){
                break;
            }

            reservationsId++;
            String identifier = "reservation" + reservationsId;

            last = executionTime.nextExecution(last).get();
            String start = last.format(formatter);

            ZonedDateTime end = last.plusMinutes(length);
            String rend = end.format(formatter);

            try {
                PreparedStatement preparedStatement = conn.prepareStatement(insert);
                preparedStatement.setString(1, identifier);
                preparedStatement.setString(2, start);
                preparedStatement.setString(3, rend);
                preparedStatement.setString(4, building);
                preparedStatement.setInt(5, room);
                preparedStatement.setString(6, name);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            System.out.println(identifier);
        }
        System.out.println(functionTermination);
    }

    public void matchReservation(String pattern, String building, int room) {
        Cron cron = parser.parse(pattern);
        ExecutionTime executionTime = ExecutionTime.forCron(cron);

        String request = "SELECT cnum, rstart, rend FROM Reservations WHERE room = ? AND building = ?";

        try {
            PreparedStatement statement = conn.prepareStatement(request);
            statement.setInt(1, room);
            statement.setString(2, building);
            ResultSet resultSet = statement.executeQuery();
            statement.close();

            if (!executionTime.nextExecution(start).isPresent()) {
                return;
            }

            ZonedDateTime last = executionTime.nextExecution(start).get();

            while (executionTime.nextExecution(last).isPresent()) {
                if (last.isAfter(zLimit)) {
                    break;
                }

                last = executionTime.nextExecution(last).get();
                String lastString = last.format(formatter);
                LocalDateTime lastDT = LocalDateTime.parse(lastString, formatter);

                while (resultSet.next()) {
                    String rstart = resultSet.getString("rstart");
                    String rend = resultSet.getString("rend");
                    LocalDateTime start = LocalDateTime.parse(rstart);
                    LocalDateTime end = LocalDateTime.parse(rend);

                    if ((lastDT.isAfter(start) && lastDT.isBefore(end)) || lastDT.equals(start) || lastDT.equals(end)) {
                        System.out.println(resultSet.getString("cnum"));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(functionTermination);
    }

    public void getFreeRoom(String pattern, int length, int chairs, int screens) {
        Cron cron = parser.parse(pattern);
        ExecutionTime executionTime = ExecutionTime.forCron(cron);

        String request = "SELECT building, room FROM Reservations WHERE rstart <> ? AND rend <> ?";

        if (!executionTime.nextExecution(start).isPresent()) {
            return;
        }

        ZonedDateTime last = executionTime.nextExecution(start).get();

        while (executionTime.nextExecution(last).isPresent()) {
            if (last.isAfter(zLimit)) {
                break;
            }

            last = executionTime.nextExecution(last).get();
            String rstart = last.format(formatter);
            ZonedDateTime end = last.plusMinutes(length);
            String rend = end.format(formatter);

            try {
                PreparedStatement preparedStatement = conn.prepareStatement(request);
                preparedStatement.setString(1, rstart);
                preparedStatement.setString(2, rend);
                ResultSet resultSet = preparedStatement.executeQuery();
                preparedStatement.close();

                while (resultSet.next()) {
                    String facilitiesRequest = "SELECT building, room FROM Facilities WHERE chairs >= ? AND screens >= ?";

                    PreparedStatement fPStmt = conn.prepareStatement(facilitiesRequest);
                    fPStmt.setInt(1, chairs);
                    fPStmt.setInt(2, screens);
                    ResultSet fResultSet = fPStmt.executeQuery();
                    fPStmt.close();


                    String fBuilding = fResultSet.getString("building");
                    String rBuilding = resultSet.getString("building");
                    int fRoom = fResultSet.getInt("room");
                    int rRoom = resultSet.getInt("room");

                    while (fResultSet.next()) {
                        if (fBuilding.equals(rBuilding) && fRoom == rRoom) {
                            System.out.println(fBuilding + separator + fRoom);
                        }
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(functionTermination);
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
            return Integer.parseInt(id);
        } else if (table.equals("Facilities")) {
            try {
                id = id.substring(8);
            } catch (IndexOutOfBoundsException e) {
                return 0;
            }
            return Integer.parseInt(id);
        } else {
            try {
                id = id.substring(11);
            } catch (IndexOutOfBoundsException e) {
                return 0;
            }
            return Integer.parseInt(id);
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
