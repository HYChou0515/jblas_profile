# jblas_profile

Profiling jblas vs apache common math
This has been tested on linux system.

# Quick Start

```
./profile.sh
```

# Requirement

1. jdk
2. gradle
3. libgfortran3

# Docker Instruction

If your system is not able to run java, gradle or fortran, you may want to try using docker.

```
docker build -t jblas-profile:latest docker/
docker run -it -v $PWD:/jblas_profile jblas-profile:latest bash profile.sh
```
