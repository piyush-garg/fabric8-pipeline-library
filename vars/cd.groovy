#!/usr/bin/groovy

def call(Closure body) {
    echo  "branch ${env.BRANCH_NAME}"
    if(env.BRANCH_NAME.equals('master')) {
        echo "from cd"
        body()
    }
}