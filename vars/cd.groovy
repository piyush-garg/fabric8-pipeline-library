#!/usr/bin/groovy

def call(Closure body) {
    //def resources = processTemplate(templateConfig)
    //def config = [templateConfig: "cd"]

    if(env.BRANCH_NAME.equals('master')) {

        //body.resolveStrategy = Closure.DELEGATE_FIRST
        //body.delegate = config
        body()

        //echo "cd config ${config}"
    }
}