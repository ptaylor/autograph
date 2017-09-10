# auto graph


A simple graphing tools which reads arbitrary data from stdin and displays a graph in real time. Allows simple scripts to be written to visualize data inputs.

# Building

```
./gradlew --info fatJar
```


# Examples
```
./random-values 12 0.001 1000| java -jar build/libs/graph-pipe-all-1.0.0-SNAPSHOT.jar 
```

