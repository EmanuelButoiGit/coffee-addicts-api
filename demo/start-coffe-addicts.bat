@echo off
echo Starting "start-coffee-addicts.jar"...
echo Please add your JDK 17 path and the custom CSV url..
start "" http://localhost:8080/graphiql?path=/graphql
"C:\Program Files\OpenLogic\jdk-17.0.6.10-hotspot\bin\java.exe" -jar coffee.addicts.jar --csv.url=https://raw.githubusercontent.com/Agilefreaks/test_oop/master/coffee_shops.csv
pause