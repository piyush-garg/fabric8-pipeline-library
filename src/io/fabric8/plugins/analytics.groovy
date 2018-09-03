package io.fabric8.plugins

import io.fabric8.Event

def register() {
    Event.instance.on("pipeline.start") {
        sh "ls ."
    }
}

