public class Room {

    private String id;
    private String building;
    private int floor;
    private int room;
    private String directions;

    public Room(String id, String building, int floor, int room, String directions) {
        this.id = id;
        this.building = building;
        this.floor = floor;
        this.room = room;
        this.directions = directions;
    }

    public String getId() {
        return id;
    }

    public String getBuilding() {
        return building;
    }

    public int getFloor() {
        return floor;
    }

    public int getRoom() {
        return room;
    }

    public String getDirections() {
        return directions;
    }
}
