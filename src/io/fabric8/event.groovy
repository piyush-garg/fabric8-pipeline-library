package io.fabric8

@Singleton
class event {
  private Map listeners = [:]

  def on(String event, Closure c) {
    on([event], c)
  }

  def on(List events, Closure c) {
    events.each { e ->
      d.listeners[e] = d.listeners[e] ?: [] as Set
      d.listeners[e].add(c)
      println "... registered for $e ${d.listeners[e]}"
    }
  }

 def emit(List events, Object... args) {
    events.each { e ->
      if (!d.listeners[e]) {
        return
      }
      d.listeners[e].each { c -> c.call([name: e], args) }
    }
  }

 def emit(String event, Object... args) {
    emit([event], args)
  }
}
