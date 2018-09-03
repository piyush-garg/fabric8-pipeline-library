#!/usr/bin/groovy
import io.fabric8.EventType
import io.fabric8.Events
import io.fabric8.plugins.*

def call(Map parameters = [:], Closure body) {

    def defaultLabel = buildId('nodejs')
    def label = parameters.get('label', defaultLabel)
    def resources = "hello"

    node {
        new analytics().register()
        Events.emit(EventType.PIPELINE_START, "testarg")

        checkout scm

        body()

        Events.emit(EventType.PIPELINE_END, "testarg")
    }

}
