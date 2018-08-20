#!/usr/bin/groovy

def call(Map args = [:], Closure body) {
    //def resources = processTemplate(templateConfig)
    //def config = [templateConfig1: "cd"]
    def targetBranch = args.branch ?: /^PR-\d+$/

    if (env.BRANCH_NAME.trim() ==~ targetBranch) {
        //echo "templateconfig ${body.templateConfig}"
        //echo "resoures ${resources}"
        //echo "resources ${body.resources}"
        //body.resolveStrategy = Closure.DELEGATE_FIRST
        //body.delegate = config
        body()
        //echo "cd config ${config}"
    }
}