#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )/.." && pwd )"

# Generate the classpath
export HADOOP_CLASSPATH=$(find $DIR/lib/ -type f -name "*.jar" | paste -sd:)

# Ensure our libs (mainly specific guava version) has precedence over the one provided by hadoop
export HADOOP_USER_CLASSPATH_FIRST=true

# Standard launch. CCGeneratorCli implements Tool interface.
echo yarn jar $DIR/lib/jubilant-octo-umbrella.jar ch.daplab.yarn.CCGeneratorCli $@
yarn jar $DIR/lib/jubilant-octo-umbrella.jar ch.daplab.yarn.CCGeneratorCli $@
