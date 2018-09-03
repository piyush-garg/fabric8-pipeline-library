#!/usr/bin/groovy

package io.fabric8.plugins

import io.fabric8.Events

def register() {
    Events.on("pipeline.build_success") {
        echo "invoking bayesian analytics ${it}"
    }
}

