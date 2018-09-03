package io.fabric8

/**
 * Created by hshinde on 9/3/18.
 */
class Event implements Serializable {
    int t
    static private Event instance

    private Event() {}

    static Event instance() {
        if(instance == null) {
            instance = new Event()
        }

        return instance;
    }

    static void on() {
        instance().t = 1
        echo "hello"
    }
}
