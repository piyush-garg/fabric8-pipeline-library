#!/usr/bin/groovy

def call(Map args = [:], body = null){
    def spec = specForImage(args.image, args.version?: 'latest')
    println "Got spec for $args: $spec"
    pod(name: args.image, image: spec.image, shell: spec.shell) {
      body()
    }
}

def specForImage(image, version){
  def specs = [
    "node": [
      "latest": [
            image: "piyushgarg/test${image}:${version}",
            shell: '/bin/bash'
        ],
      "8.9": [
            image: "piyushgarg/test${image}:${version}",
            shell: '/bin/bash'
      ],
    ],
    "oc": [
      "latest": [
            image: "piyushgarg/testnode:latest",
            shell: '/bin/bash'
      ],
    ],
  ]
  // TODO: validate image in specs
  return specs[image][version]
}
