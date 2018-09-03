#!/usr/bin/groovy

def call(Map args = [:], body = null){
    def spec = containerImageName(args.image, args.version)
    pod(image: spec.image, containerName: spec.containerName, shell: spec.shell ) {
        body()
    }
}

def containerImageName(image, version){
    return [
            image: "${image}:${version}",
            containerName: 'node',
            shell: '/bin/bash'
    ]
}