package io.fabric8

/**
 * Created by hshinde on 9/3/18.
 */
@Singleton
class STone implements Serializable {
    int t

    void on() {
        echo "on " + t
    }
}
