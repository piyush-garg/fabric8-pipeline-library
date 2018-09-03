package io.fabric8

class events implements Serializable {
    /*private Map listeners = [:]
    static private events instance

    private events() {}

    static events instance() {
        if(instance == null) {
            instance = new events()
        }
        return instance
    }

    static def on(String event, Closure c) {
        instance().on([event], c)
    }

    def on(List events, Closure c) {
        events.each { e ->
            listeners[e] = listeners[e] ?: [] as Set
            listeners[e].add(c)
            println "... registered for $e ${listeners[e]}"
        }
    }

    static def emit(String event, Object... args) {
        instance().emit([event], args)
    }

    def emit(List events, Object... args) {
        events.each { e ->
            if (!listeners[e]) {
                return
            }
            listeners[e].each { c -> c.call([name: e], args) }
        }
    }*/
}
