Simple tool to replace the deprecated [Sonar Report Plugin](https://docs.sonarqube.org/display/PLUG/Issues+Report+Plugin).

- Your project must have <i>sonarqube</i> plugin

  - <i>plugins { id "org.sonarqube" version "2.6.2" } </i>
  

- Just place the [JAR](https://github.com/italomlaino/sonar-report-generator/releases) in your project root directory, or specify it in the command-line arguments, and run it through terminal one of the following command patterns: 

  - <i>java -jar sonar-report-generator-*.jar</i>
 
  - <i>java -jar sonar-report-generator-*.jar /home/user/my_project</i>
  
   - <i>java -jar sonar-report-generator-*.jar /home/user/my_project sonarqube -Dsonar.login=***** -Dsonar.password=****</i>
  
- In IntelliJ IDEA, you can quickly go to the issue copying the location, opening the 'Navigate to file' window (CTRL + SHIFT + N) and pasting it.