# Autograph

Automatically displays time graph of arbitary data.   

Steps:
1. Write a script to extract the data and write it to stdout.
2. Pipe the output to ``autograph``


## Example: graph of random data
```
./src/test/scripts/random-values 5 10000 10 100 5 0.2 | ./build/install/autograph/bin/autograph
```

![Screenshot](/src/doc/images/random_graph.png)


## Example: graph of Unix load averages
```
(echo "1 min, 5 mins, 15 mins"; while : ; do uptime | sed -e 's/^.*://' ; sleep 1; done) | ./build/install/autograph/bin/autograph
```

![Screenshot](/src/doc/images/load_average_graph.png)

## Example: graph memory usage of a process
PID=??
```
(echo 'VSZ RSS'; while : ; do  ps aux   | awk '{ if ($2 == $PID)  print $5, $6 } '; sleep 1; done) | ~/github/autograph/build/install/autograph/bin/autograph
```

## Building

```
./gradlew --info installDist
```

## Creating distribution
```
./gradlew --info clean assembleDist
```

