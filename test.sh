#!/bin/bash
./gradlew clean
./gradlew build
./gradlew -q run --args='6 3 0 1'
./gradlew -q run --args='6 3 1 1'
./gradlew -q run --args='6 3 2 1'
./gradlew -q run --args='6 3 3 1'
