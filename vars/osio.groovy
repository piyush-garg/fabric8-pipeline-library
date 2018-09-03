#!/usr/bin/groovy
import io.fabric8.STone
import io.fabric8.events
import io.fabric8.plugins.*

def call(Map parameters = [:], Closure body) {

    def defaultLabel = buildId('nodejs')
    def label = parameters.get('label', defaultLabel)
    def resources = "hello"

    //def config = [templateConfig: "nodeJs"]
    //def config = ["template":"processed templateg"]
    node {

        //TODO: delete this
        // loading
        /*new analytics().register()
        events.emit("pipeline.start", "testarg")*/

        STone.on()

        checkout scm

        //body.resolveStrategy = Closure.DELEGATE_FIRST
        //body.delegate = config
        //echo "templateconfig ${body.templateConfig}"
        //body.cd.setProperty("resources" , "resources")
        body()

        //echo "nodeJs config ${config}"
    }

}
