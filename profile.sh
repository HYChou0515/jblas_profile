#!/bin/bash
./gradlew clean
./gradlew build
l=(100 300 1000 3000)
n=(10 30 100 300)
algo=0
for li in ${l[@]}; do
for ni in ${n[@]}; do
	./gradlew -q run --args="$li $ni $algo" >> profile.log
done
done
l=(100 300 1000 3000)
n=(10 30 100 300)
algo=1
for li in ${l[@]}; do
for ni in ${n[@]}; do
	./gradlew -q run --args="$li $ni $algo" >> profile.log
done
done
l=(100 300 1000 3000 100000)
n=(10 30 100 300)
algo=2
for li in ${l[@]}; do
for ni in ${n[@]}; do
	./gradlew -q run --args="$li $ni $algo" >> profile.log
done
done
l=(100 300 1000 3000 100000)
n=(10 30 100 300 1000)
algo=3
for li in ${l[@]}; do
for ni in ${n[@]}; do
	./gradlew -q run --args="$li $ni $algo" >> profile.log
done
done
