import java.io.File;

public class Runner {
    public static void main(String[] args) {
        RoomBoss roomBoss = new RoomBoss();
        roomBoss.connect("","");
        roomBoss.destroyTables();
        roomBoss.createTables();
        roomBoss.addRoom("neville", 1, 122, "right side from back");
        roomBoss.addRoom("dpc", 1, 101, "big lecture hall");
        roomBoss.addRoom("stevens", 2, 237, "up the stairs on the left");

//        if (args[0] == "<") {
//            File file = new File(args[1]);
//
//        } else {
//
//        }
    }
}
