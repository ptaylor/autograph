# autograph


A simple graphing tools which reads arbitrary data from stdin and displays a graph in real time. Allows simple scripts to be written to visualize data inputs.



## Examples
```
./src/test/scripts/random-values 3 10000 10 100 5 1.0 | ./build/install/autograph/bin/autograph
```



## Graph load averages
```
(echo "1 min, 5 mins, 15 mins"; while : ; do uptime | sed -e 's/^.*://' ; sleep 1; done) | ./build/install/autograph/bin/autograph
```

## Graph process memory usage
PID=<process-id>
```
(echo 'VSZ RSS'; while : ; do  ps aux   | awk '{ if ($2 == $PID)  print $5, $6 } '; sleep 1; done) | ~/github/autograph/build/install/autograph/bin/autograph
```

## Building

```
./gradlew --info installDist
```

##

Creating distribution
```
./gradlew --info clean assembleDist
```
