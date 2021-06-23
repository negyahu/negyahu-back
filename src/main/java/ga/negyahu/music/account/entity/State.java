package ga.negyahu.music.account.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum State {

    ACTIVE, DORMANT, IGNORE, DELETED, WAIT, PRIVATE;

    public static State getState(String value) {
        for(State state : values()){
            if(state.name().equalsIgnoreCase(value)) return state;
        }
        return State.ACTIVE;
    }

    @JsonCreator
    public static State fromJson(@JsonProperty("state") String value) {
        return getState(value);
    }

}
