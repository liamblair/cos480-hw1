import java.io.File;

public class Runner {
    public static void main(String[] args) {
        RoomBoss roomBoss = new RoomBoss();

        roomBoss.connect("","");
        roomBoss.destroyTables();
        roomBoss.createTables();

        roomBoss.addRoom("Neville", 1, 110, "Near the north entrance");
        roomBoss.addRoom("Neville", 2, 210, "take the stairs");
        roomBoss.addRoom("Neville", 2, 227, "Large room near");
        roomBoss.addRoom("Corbett", 1, 101, "Right there");
        roomBoss.addRoom("Corbett", 2, 201, "Left, then right");

        roomBoss.addFacilities("Neville", 210, 25, 0);
        roomBoss.addFacilities("Neville", 227, 35, 1);
        roomBoss.addFacilities("Corbett", 101, 2, 0);

        roomBoss.addReservation("2016-09-01 09:00:00", "2016-09-08 10:00:00", "Neville", 110, "liam");
        roomBoss.addReservation("2017-10-13 11:30:00", "2016-10-13 12:30:00", "Neville", 210, "cam");
        roomBoss.addReservation("2016-09-10 08:00:00", "2016-09-11 09:00:00", "Neville", 110, "arpin");
        roomBoss.addReservation("2016-11-02 13:20:00", "2016-11-02 14:20:00", "Corbett", 101, "ainslie");

        roomBoss.describeRoom("room2");
        roomBoss.describeRoom("room6");



//        if (args[0] == "<") {
//            File file = new File(args[1]);
//
//        } else {
//
//        }
    }
}
