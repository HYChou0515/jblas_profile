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

# Usage

```
gradle run --args="<l> <n> <algo> <print-result=0>"
```
- l: number of instances
- n: number of features
- algo: should be 0~3,
  - 0: apache-original: use apache common math3 and naive matrix multiplication
  - 1: apache-diag: use apache common math3 and only calculate diagonal elements
  - 2: jblas-diag: use jblas and only calculate diagonal elements
  - 3: jblas-eigen: use jblas and make good use of the property of eigen-decomposition
- print-result: whether to print out the resulting vector

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
