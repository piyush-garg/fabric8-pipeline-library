#!/usr/bin/groovy
import io.fabric8.Fabric8Commands

def call(Map args = [:], body = null) {
    def flow = new Fabric8Commands()
    def label = buildId('nodejs')
    def cloud = flow.getCloudConfig()
    def inheritFrom = 'base'
    def jnlpImage = (flow.isOpenShift()) ? 'fabric8/jenkins-slave-base-centos7:vb0268ae' : 'jenkinsci/jnlp-slave:2.62'

    podTemplate(cloud: cloud, label: label, serviceAccount: 'jenkins', inheritFrom: "${inheritFrom}",
            containers: [
                    containerTemplate(
                            name: args.containerName,
                            image: args.image,
                            command: '/bin/sh -c',
                            args: 'cat',
                            ttyEnabled: true,
                            workingDir: '/home/jenkins/',
                            resourceLimitMemory: '640Mi'
                    ),
                    containerTemplate(
                            name: 'jnlp',
                            image: "${jnlpImage}",
                            args: '${computer.jnlpmac} ${computer.name}',
                            workingDir: '/home/jenkins/',
                            resourceLimitMemory: '256Mi'
                    )
            ],
            volumes: [
                    secretVolume(secretName: 'jenkins-release-gpg', mountPath: '/home/jenkins/.gnupg-ro'),
                    secretVolume(secretName: 'jenkins-hub-api-token', mountPath: '/home/jenkins/.apitoken'),
                    secretVolume(secretName: 'jenkins-ssh-config', mountPath: '/root/.ssh-ro'),
                    secretVolume(secretName: 'jenkins-git-ssh', mountPath: '/root/.ssh-git-ro')
            ]
    ) {
        node (label) {
            container(name: args.containerName, shell: args.shell) {
                body()
            }
        }
    }
}