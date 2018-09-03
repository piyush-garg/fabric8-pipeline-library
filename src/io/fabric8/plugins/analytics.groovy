package io.fabric8.plugins

import io.fabric8.event

def register() {
    event.instance.on("pipeline.start") {
        sh "ls ."
    }
}

