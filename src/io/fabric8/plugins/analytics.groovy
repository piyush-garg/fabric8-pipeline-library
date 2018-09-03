#!/usr/bin/groovy

package io.fabric8.plugins

import io.fabric8.Events
import static io.fabric8.Events.EVENT.*

def register() {
    Events.on(PIPELINE_START) {
        sh "ls ."
    }

    Events.on([PIPELINE_END]) {
        echo "end"
    }
}

