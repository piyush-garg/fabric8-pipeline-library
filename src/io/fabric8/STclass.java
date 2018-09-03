package io.fabric8;

import groovy.lang.Singleton;

/**
 * Created by hshinde on 9/3/18.
 */

@Singleton
public class STclass {
    int t;

    public void on() {
        System.out.printf("on " + t);
    }
}
