#!/usr/bin/groovy

import io.fabric8.Events
import io.fabric8.plugins.*

def call(Map parameters = [:], Closure body) {

    def defaultLabel = buildId('nodejs')
    def label = parameters.get('label', defaultLabel)
    def resources = "hello"

    node {
        new analytics().register()
        Events.emit("pipeline.start", "testarg")

        checkout scm

        body()

        Events.emit("pipeline.end", "testarg")
    }

}
