#!/usr/bin/groovy

def call(body = null) {
  def targetBranch = args.branch ?: 'master'
  if (env.BRANCH_NAME.equals(targetBranch)) {
    body()
  }
}
