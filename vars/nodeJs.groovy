#!/usr/bin/groovy

def call(Map parameters = [:], Closure body) {

    def defaultLabel = buildId('nodejs')
    def label = parameters.get('label', defaultLabel)
    def resources = "hello"
    //def config = [templateConfig: "nodeJs"]
    //def config = ["template":"processed templateg"]
    node {
        //checkout scm

        //body.resolveStrategy = Closure.DELEGATE_FIRST
        //body.delegate = config
        //echo "templateconfig ${body.templateConfig}"
        //body.cd.setProperty("resources" , "resources")
        body()

        //echo "nodeJs config ${config}"
    }

}
