#!/usr/bin/groovy

package io.fabric8.plugins

import io.fabric8.EventType
import io.fabric8.Events

def register() {
    Events.on(EventType.BUILD_SUCCES) {
        echo "invoking bayesian analytics ${it}"
    }
}

