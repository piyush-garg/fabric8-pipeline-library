#!/usr/bin/groovy
import io.fabric8.Fabric8Commands
def call(Map parameters = [:], body) {
    def flow = new Fabric8Commands()

    def defaultLabel = buildId('ui')
    def label = parameters.get('label', defaultLabel)

    def uiImage = parameters.get('uiImage', 'fabric8/fabric8-ui-builder:v3c57d6b')
    def inheritFrom = parameters.get('inheritFrom', 'base')
    def jnlpImage = (flow.isOpenShift()) ? 'fabric8/jenkins-slave-base-centos7:0.0.1' : 'jenkinsci/jnlp-slave:2.62'

    def cloud = flow.getCloudConfig()

    def userSecret = 'planner-team-test-osio-token'

    def testEnvVars = [
            secretEnvVar(secretName: userSecret, key: 'PLANNER_TOKEN', secretKey: 'planner-test-osio-token'),

            envVar(key: 'DOCKER_CONFIG', value: '/home/jenkins/.docker/')
    ]

    def utils = new io.fabric8.Utils()
    // 0.13 introduces a breaking change when defining pod env vars so check version before creating build pod
    if (utils.isKubernetesPluginVersion013()) {
        if (utils.isUseOpenShiftS2IForBuilds()) {
            echo 'Runnning on openshift so using S2I binary source and Docker strategy'
            podTemplate(cloud: cloud, label: label, serviceAccount: 'jenkins', inheritFrom: "${inheritFrom}",
                    containers: [
                            containerTemplate(
                                    name: 'jnlp',
                                    image: "${jnlpImage}",
                                    args: '${computer.jnlpmac} ${computer.name}',
                                    workingDir: '/home/jenkins/'),
                            containerTemplate(
                                    name: 'ui',
                                    image: "${uiImage}",
                                    command: '/bin/sh -c',
                                    args: 'cat',
                                    ttyEnabled: true,
                                    workingDir: '/home/jenkins/',
                                    envVars: testEnvVars)
                    ],
                    volumes: [
                            secretVolume(secretName: 'jenkins-docker-cfg', mountPath: '/home/jenkins/.docker'),
                            secretVolume(secretName: 'npm-npmrc', mountPath: '/home/jenkins/.npm-npmrc'),
                            secretVolume(secretName: 'npm-token', mountPath: '/home/jenkins/.npm-token'),
                            secretVolume(secretName: 'jenkins-hub-api-token', mountPath: '/home/jenkins/.apitoken')]) {
                body()
            }
        } else {
            echo 'Mounting docker socket to build docker images'
            podTemplate(cloud: cloud, label: label, serviceAccount: 'jenkins', inheritFrom: "${inheritFrom}",
                    containers: [
                            containerTemplate(
                                    name: 'ui',
                                    image: "${uiImage}",
                                    command: '/bin/sh -c',
                                    args: 'cat',
                                    privileged: true,
                                    workingDir: '/home/jenkins/',
                                    ttyEnabled: true,
                                    envVars: testEnvVars)
                    ],
                    volumes: [
                            secretVolume(secretName: 'jenkins-docker-cfg', mountPath: '/home/jenkins/.docker'),
                            secretVolume(secretName: 'jenkins-hub-api-token', mountPath: '/home/jenkins/.apitoken'),
                            secretVolume(secretName: 'npm-npmrc', mountPath: '/home/jenkins/.npm-npmrc'),
                            secretVolume(secretName: 'jenkins-ssh-config', mountPath: '/root/.ssh'),
                            secretVolume(secretName: 'jenkins-git-ssh', mountPath: '/root/.ssh-git'),
                            secretVolume(secretName: 'npm-token', mountPath: '/home/jenkins/.npm-token'),
                            hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock')]) {
                body()
            }
        }
    } else {
        if (utils.isUseOpenShiftS2IForBuilds()) {
            echo 'Runnning on openshift so using S2I binary source and Docker strategy'
            podTemplate(cloud: cloud, label: label, serviceAccount: 'jenkins', inheritFrom: "${inheritFrom}",
                    containers: [
                            [name: 'jnlp', image: "${jnlpImage}", args: '${computer.jnlpmac} ${computer.name}',  workingDir: '/home/jenkins/'],
                            [name: 'ui', image: "${uiImage}", command: '/bin/sh -c', args: 'cat', ttyEnabled: true,  workingDir: '/home/jenkins/', envVars: [[key: 'DOCKER_CONFIG', value: '/home/jenkins/.docker/']]]],
                    volumes: [
                            secretVolume(secretName: 'jenkins-docker-cfg', mountPath: '/home/jenkins/.docker'),
                            secretVolume(secretName: 'npm-npmrc', mountPath: '/home/jenkins/.npm-npmrc'),
                            secretVolume(secretName: 'npm-token', mountPath: '/home/jenkins/.npm-token'),
                            secretVolume(secretName: 'jenkins-hub-api-token', mountPath: '/home/jenkins/.apitoken')]) {
                body()
            }
        } else {
            echo 'Mounting docker socket to build docker images'
            podTemplate(cloud: cloud, label: label, serviceAccount: 'jenkins', inheritFrom: "${inheritFrom}",
                    containers: [
                            [name: 'ui', image: "${uiImage}", command: '/bin/sh -c', args: 'cat', privileged: true,  workingDir: '/home/jenkins/', ttyEnabled: true, envVars: [[key: 'DOCKER_CONFIG', value: '/home/jenkins/.docker/']]]],
                    volumes: [
                            secretVolume(secretName: 'jenkins-docker-cfg', mountPath: '/home/jenkins/.docker'),
                            secretVolume(secretName: 'jenkins-hub-api-token', mountPath: '/home/jenkins/.apitoken'),
                            secretVolume(secretName: 'npm-npmrc', mountPath: '/home/jenkins/.npm-npmrc'),
                            secretVolume(secretName: 'npm-token', mountPath: '/home/jenkins/.npm-token'),
                            hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock')],
                    envVars: [[key: 'DOCKER_HOST', value: 'unix:/var/run/docker.sock'], [key: 'DOCKER_CONFIG', value: '/home/jenkins/.docker/']]) {
                body()
            }
        }
    }
}
