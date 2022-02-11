package assignment_1;

import java.sql.Timestamp;

public class Actor {
    int actor_id;
    String first_name;
    String last_name;
    String last_update;

    public int getActor_id() {
        return actor_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setActor_id(int actor_id) {
        this.actor_id = actor_id;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }
}
