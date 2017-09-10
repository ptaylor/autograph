# auto graph


A simple graphing tools which reads arbitrary data from stdin and displays a graph in real time. Allows simple scripts to be written to visualize data inputs.

# Building

```
./gradlew --info fatJar
```


# Examples
```
./src/test/scripts/random-values 12 0.001 1000 | ./src/main/scripts/autograph
```



# Graph load averages
```
(echo "1-min 5-mins 15-mins"; while : ; do uptime | sed -e 's/^.*://' ; sleep 1; done) | ./src/main/scripts/autograph
```

