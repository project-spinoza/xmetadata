#!/bin/bash

if [ ! -z "$1" ] && [ ! -z "$2" ]; then
  java -jar ./target/xmetadata-1.0-jar-with-dependencies.jar $1 $2
else
  echo "please specify app mode e.g. sh launch.sh --mode cmd"
fi

