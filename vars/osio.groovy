#!/usr/bin/groovy

import io.fabric8.Events
import io.fabric8.plugins.*

def call(body) {

    node {

        new analytics().register()
        Events.emit("pipeline.start", "testarg")

        checkout scm

        body()

        Events.emit("pipeline.end", "testarg")
    }
}
