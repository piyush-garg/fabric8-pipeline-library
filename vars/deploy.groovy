#!/usr/bin/groovy
import io.fabric8.Utils;

def call(envName = null, resources) {
    def userNamespace = new Utils().getUsersNamespace();
    def deployNamespace = userNamespace + "-" + envName;

    stage ("Deploy to ${envName}") {
        tagImageToDeployEnv(deployNamespace, userNamespace, resources.ImageStream, resources.tag)
        deployEnvironment(deployNamespace, resources.DeploymentConfig, resources.Service, resources.Route)
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