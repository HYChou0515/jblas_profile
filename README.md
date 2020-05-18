# jblas_profile

Profiling jblas vs apache common math
This has been tested on linux and windows system.

# Quick Start

## Linux

```
gradle wrapeer
./profile.sh
```

## Windows

```
gradle wrapper
gradlew run --args "6 3 0 1"
gradlew run --args "6 3 1 1"
gradlew run --args "6 3 2 1"
gradlew run --args "6 3 3 1"
```

# Requirement

1. jdk
2. gradle
3. libgfortran3 [linux only]

# Docker Instruction

If your system is not able to run java, gradle or fortran, you may want to try using docker.

```
docker build -t jblas-profile:latest docker/
docker run -it -v $PWD:/jblas_profile jblas-profile:latest bash profile.sh
```
