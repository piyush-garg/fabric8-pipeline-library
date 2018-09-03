#!/usr/bin/groovy
package io.fabric8

class d {
    static listeners = [:]
}

static def on(String event, Closure c) {
    on([event], c)
}

static def on(List events, Closure c) {
    def listeners = io.fabric8.d.listeners
    events.each { e ->
        listeners[e] = listeners[e] ?: [] as Set
        listeners[e].add(c)
        println "... registered for $e ${listeners[e]}"
    }
}

static def emit(List events, Object... args) {
    events.each { e ->
      if (!d.listeners[e]) {
        return
      }
      d.listeners[e].each { c -> c.call([name: e], args) }
    }
}

static def emit(String event, Object... args) {
    emit([event], args)
}

