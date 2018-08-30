package io.fabric8.hooks

class HookFactory {
    def HookEvent event

    public HookFactory() {
        event = null
    }

    public HookFactory(HookEvent event) {
      this.event = event
    }

    public List<Hook> getHooks() {
        def content = libraryResource 'default-hooks'
        println content
    }
}