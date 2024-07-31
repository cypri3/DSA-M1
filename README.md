Pour l'installation: 
wget https://github.com/gradle/gradle-distributions/releases/download/v8.0.2/gradle-8.0.2-bin.zip
sudo unzip gradle-8.0.2-bin.zip -d /opt/gradle
export PATH=/opt/gradle/gradle-8.0.2/bin:$PATH
source ~/.bashrc

Pour voir la version et vérifier l'installation :
gradle -v

./gradlew clean
rm -rf ~/.gradle/caches/
./gradlew build

Voici les 3 commandes principales : 

./gradlew run
./gradlew test
./gradlew build

# Autre méthode :

# Vérifie la version de Java
java -version

# Installe le JDK 17 si nécessaire (Ubuntu/Debian)
sudo apt update
sudo apt install openjdk-17-jdk

# Configure JAVA_HOME
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc
source ~/.bashrc

# Vérifie les variables d'environnement
echo $JAVA_HOME
java -version

# Assure-toi que gradle.properties est configuré
echo 'org.gradle.java.home=/usr/lib/jvm/java-17-openjdk-amd64' > gradle.properties

# Recompile le projet
./gradlew clean build
