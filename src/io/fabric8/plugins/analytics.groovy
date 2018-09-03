#!/usr/bin/groovy

package io.fabric8.plugins

import io.fabric8.events

def register() {
    events.on("pipeline.start") {
        sh "ls ."
    }
}

