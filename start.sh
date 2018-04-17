#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd ${DIR}
mvn clean package
java -cp target/gradoop-demo-shaded.jar org.gradoop.demo.server.Server "$@"
