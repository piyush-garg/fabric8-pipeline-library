#!/usr/bin/groovy

def call(Closure body) {
    //def resources = processTemplate(templateConfig)
    //def config = [templateConfig1: "cd"]

    if(env.BRANCH_NAME.equals('master')) {
        echo "templateconfig ${body.templateConfig}"
        echo "resoures ${resources}"
        //echo "resources ${body.resources}"
        //body.resolveStrategy = Closure.DELEGATE_FIRST
        //body.delegate = config
        body()
        //echo "cd config ${config}"
    }
}