#!/usr/bin/groovy

package io.fabric8.plugins

import io.fabric8.Events
import io.fabric8.Events.EVENT

def register() {
    Events.on(EVENT.PIPELINE_START) {
        sh "ls ."
    }

    Events.on([EVENT.PIPELINE_END]) {
        echo "end"
    }
}

