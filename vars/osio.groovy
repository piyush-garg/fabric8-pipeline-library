#!/usr/bin/groovy
import io.fabric8.Events
import io.fabric8.plugins.*
import io.fabric8.Events.EVENT

def call(Map parameters = [:], Closure body) {

    def defaultLabel = buildId('nodejs')
    def label = parameters.get('label', defaultLabel)
    def resources = "hello"

    node {
        new analytics().register()
        Events.emit(EVENT.PIPELINE_START, "testarg")

        checkout scm

        body()

        Events.emit(EVENT.PIPELINE_END, "testarg")
    }

}
