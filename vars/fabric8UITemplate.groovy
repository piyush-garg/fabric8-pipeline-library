#!/usr/bin/groovy
import io.fabric8.Fabric8Commands

def call(Map parameters = [:], body) {
    def flow = new Fabric8Commands()

    def defaultLabel = buildId('ui')
    def label = parameters.get('label', defaultLabel)

    def uiImage = parameters.get('uiImage', 'piyushgarg/testnode')
    def inheritFrom = parameters.get('inheritFrom', 'base')
    def jnlpImage = (flow.isOpenShift()) ? 'fabric8/jenkins-slave-base-centos7:v54e55b7' : 'jenkinsci/jnlp-slave:2.62'

    def cloud = flow.getCloudConfig()

    def utils = new io.fabric8.Utils()

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
                                //command: '/bin/sh -c',
                                //args: '/bin/sh',
                                ttyEnabled: true,
                                workingDir: '/home/jenkins/')
                ],
                volumes: [
                        secretVolume(secretName: 'jenkins-docker-cfg', mountPath: '/home/jenkins/.docker'),
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
                        secretVolume(secretName: 'jenkins-ssh-config', mountPath: '/root/.ssh-ro'),
                        secretVolume(secretName: 'jenkins-git-ssh', mountPath: '/root/.ssh-git-ro'),
                        secretVolume(secretName: 'npm-token', mountPath: '/home/jenkins/.npm-token'),
                        hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock')]) {
            body()
        }
    }

}
