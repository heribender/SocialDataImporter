:: starter script for SocialDataImporter
::
:: needs java 8 runtime

@echo off

set LIB_DIR=../lib

set CLASSPATH=../cfg
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/core-0.0.1-SNAPSHOT.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/main-0.0.1-SNAPSHOT.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/oxwall-importer-0.0.1-SNAPSHOT.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/oxwall-plugins-0.0.1-SNAPSHOT.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/activation-1.1.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/aopalliance-1.0.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/commons-codec-1.9.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/commons-email-1.3.3.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/commons-lang3-3.3.2.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/commons-logging-1.1.3.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/log4j-1.2-api-2.1.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/log4j-api-2.1.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/log4j-core-2.1.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/mail-1.4.7.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/spring-aop-4.1.1.RELEASE.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/spring-beans-4.1.1.RELEASE.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/spring-context-4.1.1.RELEASE.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/spring-core-4.1.1.RELEASE.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/spring-expression-4.1.1.RELEASE.jar


:: set CMD_LINE=java -cp %CLASSPATH% -Dinputcollector.usernamekey=Username ch.sdi.SocialDataImporter 
set CMD_LINE=java -cp %CLASSPATH% ch.sdi.SocialDataImporter 
echo executing command line: %CMD_LINE%
%CMD_LINE%

pause