#!/usr/bin/groovy

@Grab( 'org.slf4j:slf4j-api:1.7.5' )
@Grab( 'org.reflections:reflections:0.9.9-RC1' )
import org.reflections.*
import io.fabric8.Utils

def call(Map args, Closure body) {
    stage("Build application") {

        def namespace = args.namespace ?: new Utils().getUsersNamespace()
        createImageStream(args.app.ImageStream, namespace)
        buildProject(args.app.BuildConfig, namespace)

        new Reflections('io.fabric8').getSubTypesOf(Hook).each {
            echo "{$it.name}"
        }
    }
}

def createImageStream(imageStream, namespace) {
    def isName = imageStream.metadata.name
    def isFound = shWithOutput("oc get is/$isName -n $namespace --ignore-not-found")
    if (!isFound) {
        ocApplyResource(imageStream, namespace)
    } else {
        echo "image stream exist ${isName}"
    }
}

def buildProject(buildConfig, namespace) {
    ocApplyResource(buildConfig, namespace)
    openshiftBuild(buildConfig: "${buildConfig.metadata.name}", showBuildLogs: 'true')
}

def shWithOutput(String command) {
    return sh(
            script: command,
            returnStdout: true
    ).trim()
}

def ocApplyResource(resource, namespace) {
    def resourceFile = "/tmp/${namespace}-${env.BUILD_NUMBER}-${resource.kind}.yaml"
    writeYaml file: resourceFile, data: resource
    sh "oc apply -f ${resourceFile} -n ${namespace}"
}
