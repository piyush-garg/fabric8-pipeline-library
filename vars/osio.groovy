#!/usr/bin/groovy
import io.fabric8.Events
import io.fabric8.plugins.*

def call(Map parameters = [:], Closure body) {

    def defaultLabel = buildId('nodejs')
    def label = parameters.get('label', defaultLabel)
    def resources = "hello"

    node {

        //TODO: delete this
        // loading
        new analytics().register()
        Events.emit("pipeline.start", "testarg")

        checkout scm

        //body.resolveStrategy = Closure.DELEGATE_FIRST
        //body.delegate = config
        //echo "templateconfig ${body.templateConfig}"
        //body.cd.setProperty("resources" , "resources")
        body()

        //echo "nodeJs config ${config}"
    }

}
