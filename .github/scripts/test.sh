set -eux

javac src/test/java/com/minersstudios/Main.java
output="$(java -cp src/test/java com.minersstudios.Main)"
return test "$output" = 'Hello, PackmanDude!'
