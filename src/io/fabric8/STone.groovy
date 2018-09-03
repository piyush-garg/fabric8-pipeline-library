package io.fabric8

/**
 * Created by hshinde on 9/3/18.
 */
class STone implements Serializable {
    int t
    static private STone instance;

    private STone() {}

    static STone instance() {
        if(instance == null) {
            instance = new STone()
        }

        return instance;
    }

    void on() {
        echo "on " + t
    }
}
