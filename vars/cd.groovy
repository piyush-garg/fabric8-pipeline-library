#!/usr/bin/groovy

def call(body = null) {

    if(env.BRANCH_NAME.equals('master')) {
        body()
    }
}