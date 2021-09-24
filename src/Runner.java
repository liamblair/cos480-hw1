import java.io.File;

public class Runner {
    public static void main(String[] args) {
        RoomBoss roomBoss = new RoomBoss();
        roomBoss.connect("","");
        roomBoss.createTables();

//        if (args[0] == "<") {
//            File file = new File(args[1]);
//
//        } else {
//
//        }
    }
}
