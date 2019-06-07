#!/bin/bash

set -ev
mvn clean install
./scripts/move_to_scala_2.12.sh
mvn clean install
./scripts/move_to_scala_2.11.sh
