#!/usr/bin/groovy

def call(Map args = [:], Closure body) {
    //def resources = processTemplate(templateConfig)
    //def config = [templateConfig1: "cd"]
    def targetBranch = args.branch ?: /^PR-\d+$/

    echo "branch name ${env.BRANCH_NAME}"
    echo "target ${targetBranch}"
    echo "test ${env.BRANCH_NAME ==~ targetBranch}"

    if (env.BRANCH_NAME ==~ targetBranch) {
        //echo "templateconfig ${body.templateConfig}"
        //echo "resoures ${resources}"
        //echo "resources ${body.resources}"
        //body.resolveStrategy = Closure.DELEGATE_FIRST
        //body.delegate = config
        body()
        //echo "cd config ${config}"
    }
}