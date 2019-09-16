mkdir build && cd build
wget "https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar"
java -jar BuildTools.jar --rev 1.14.4
mv spigot-1.14.4.jar ../server.jar && cd .. && rm -rf build