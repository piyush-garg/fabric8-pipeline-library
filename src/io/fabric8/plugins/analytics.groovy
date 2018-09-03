#!/usr/bin/groovy

package io.fabric8.plugins

import io.fabric8.EventType
import io.fabric8.Events

def register() {
    Events.on(EventType.PIPELINE_START) {
        sh "ls ."
    }

    Events.on([EventType.PIPELINE_START]) {
        echo "end"
    }
}

