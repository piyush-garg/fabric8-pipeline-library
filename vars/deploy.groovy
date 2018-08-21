#!/usr/bin/groovy
import io.fabric8.Utils;

def call(Map args = [:]) {
    def userNamespace = new Utils().getUsersNamespace();
    def deployNamespace = userNamespace + "-" + args.env;

    if (args.approval == 'manual') {
        askForInput()
    }

    stage ("Deploy to ${args.env}") {
        tagImageToDeployEnv(deployNamespace, userNamespace, args.app.ImageStream, args.app.tag)
        deployEnvironment(deployNamespace, args.app.DeploymentConfig, args.app.Service, args.app.Route)
    }
}

def askForInput(String version) {
    def approvalTimeOutMinutes = 30
    def appVersion = version ? "version ${version}" : "application"
    def proceedMessage = """Would you like to promote ${appVersion} to the next environment?"""

    stage("Approve") {
        try {
            timeout(time: approvalTimeOutMinutes, unit: 'MINUTES') {
                input id: 'Proceed', message: "\n${proceedMessage}"
            }
        } catch (err) {
            throw err
        }
    }
}

def tagImageToDeployEnv(deployNamespace, userNamespace, is, tag) {
    try {
        def imageName = is.metadata.name
        sh "oc tag -n ${deployNamespace} --alias=true ${userNamespace}/${imageName}:${tag} ${imageName}:${tag}"
    } catch (err) {
        error "Error running OpenShift command ${err}"
    }
}

def deployEnvironment(deployNamespace, dc,  service, route) {
    ocApplyResource(dc, deployNamespace)
    openshiftVerifyDeployment(depCfg: "${dc.metadata.name}", namespace: "${deployNamespace}")
    ocApplyResource(service, deployNamespace)
    ocApplyResource(route, deployNamespace)
    displayRouteURL(deployNamespace, route)
}

def displayRouteURL(nameSpace, route) {
    try {
        ROUTE_PREVIEW = shWithOutput("oc get route -n ${nameSpace} ${route.metadata.name} --template 'http://{{.spec.host}}'")
        echo nameSpace.capitalize() + " URL: ${ROUTE_PREVIEW}"
    } catch (err) {
        error "Error running OpenShift command ${err}"
    }
}

def ocApplyResource(resource, namespace) {
    def resourceFile = "/tmp/${namespace}-${env.BUILD_NUMBER}-${resource.kind}.yaml"
    writeYaml file: resourceFile, data: resource
    sh "oc apply -f ${resourceFile} -n ${namespace}"
}

def shWithOutput(String command) {
    return sh(
            script: command,
            returnStdout: true
    ).trim()
}
