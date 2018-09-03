#!/usr/bin/groovy
import io.fabric8.Utils;

def call(Map args = [:]) {
    def userNamespace = new Utils().getUsersNamespace();
    def deployNamespace = userNamespace + "-" + args.env;

    if (args.approval == 'manual') {
        askForInput(args.app.tag, args.env)
    }

    stage ("Deploy to ${args.env}") {
        tagImageToDeployEnv(deployNamespace, userNamespace, args.app.ImageStream, args.app.tag)
        deployEnvironment(deployNamespace, args.app.tag, args.app.DeploymentConfig, args.app.Service, args.app.Route)
    }
}

def askForInput(String version, String environment) {
    def approvalTimeOutMinutes = 30
    def appVersion = version ? "version ${version}" : "application"
    def appEnvironment = environment ? "${environment} environment" : "next environment"
    def proceedMessage = """Would you like to promote ${appVersion} to the ${appEnvironment}?"""

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

def deployEnvironment(deployNamespace, version, dc,  service, route) {
    ocApplyResource(dc, deployNamespace)
    openshiftVerifyDeployment(depCfg: "${dc.metadata.name}", namespace: "${deployNamespace}")
    ocApplyResource(service, deployNamespace)
    ocApplyResource(route, deployNamespace)
    def routeUrl = displayRouteURL(deployNamespace, route)
    def yaml = """
      ---
      environmentName: "Stage"
      serviceUrls:
        spring-boot-application: "$routeUrl"
      deploymentVersions:
        spring-boot-application: "$version"
    """
    new Utils().addAnnotationToBuild("environment.services.fabric8.io/$deployNamespace", yaml);
}

def displayRouteURL(namespace, route) {
    try {
        def routeUrl = shWithOutput("oc get route -n ${namespace} ${route.metadata.name} --template 'http://{{.spec.host}}'")
        echo namespace.capitalize() + " URL: ${routeUrl}"
        return routeUrl
    } catch (err) {
        error "Error running OpenShift command ${err}"
    }
    return null
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
