@echo off

mkdir laserlight

javac src\laserlight\*.java -d .

mkdir images

copy src\images images\

jar cfm out.jar META-INF\MANIFEST.MF laserlight\*.class images\*.png

rmdir /S /Q images

rmdir /S /Q laserlight


