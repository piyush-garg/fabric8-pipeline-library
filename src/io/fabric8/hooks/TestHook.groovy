package io.fabric8.hooks

class TestHook implements Hook {
    void execute() {
        echo "Test hook execution"
    }
}