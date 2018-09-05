#!/usr/bin/groovy

def call(Map args = [:], body = null){
    def spec = specForImage(args.image, args.version?: 'latest')
    pod image: spec.image, name: image, shell: spec.shell {
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
            image: "piyushgarg/testnode:${version}",
            shell: '/bin/bash'
      ],
    ],
  ]
  // TODO: validate image in specs
  println "Getting spec for $image : $version"
  return specs[image][version]
}
