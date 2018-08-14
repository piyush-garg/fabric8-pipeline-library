#!/usr/bin/groovy

def call(Closure body) {
    if(env.BRANCH_NAME.equals('master')) {
        echo "from cd ${templateConfig}"
        body()
    }
}