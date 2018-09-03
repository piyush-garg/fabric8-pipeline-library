#!/usr/bin/groovy

package io.fabric8.plugins

import io.fabric8.Events

def register() {
    Events.on("pipeline.start") {
        sh "ls ."
    }

    Events.on(["pipeline.end"]) {
        echo "end"
    }
}

