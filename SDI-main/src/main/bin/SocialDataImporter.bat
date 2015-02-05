:: starter script for SocialDataImporter
::
:: needs java 8 runtime

@echo off

set LIB_DIR=../lib

set CLASSPATH=../cfg
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/activation-1.1.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/antlr-2.7.7.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/aopalliance-1.0.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/commons-codec-1.9.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/commons-collections-3.2.1.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/commons-email-1.3.3.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/commons-lang3-3.3.2.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/commons-logging-1.1.3.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/commons-net-3.3.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/core-0.9-RC1.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/dbunit-2.5.0.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/dir.txt
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/dom4j-1.6.1.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/hibernate-commons-annotations-4.0.2.Final.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/hibernate-core-4.2.8.Final.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/hibernate-entitymanager-4.2.8.Final.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/hibernate-jpa-2.0-api-1.0.1.Final.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/javassist-3.18.1-GA.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/jboss-logging-3.1.0.GA.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/jboss-transaction-api_1.1_spec-1.0.1.Final.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/jsch-0.1.51.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/log4j-1.2-api-2.1.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/log4j-1.2.17.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/log4j-api-2.1.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/log4j-core-2.1.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/mail-1.4.7.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/main-0.9-RC1.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/mysql-connector-java-5.1.34.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/oxwall-plugins-0.9-RC1.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/slf4j-api-1.7.7.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/slf4j-log4j12-1.7.6.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/spring-aop-4.1.1.RELEASE.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/spring-beans-4.1.1.RELEASE.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/spring-context-4.1.1.RELEASE.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/spring-core-4.1.1.RELEASE.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/spring-expression-4.1.1.RELEASE.jar
set CLASSPATH=%CLASSPATH%;%LIB_DIR%/hsqldb-2.3.2.jar



:: set CMD_LINE=java -cp %CLASSPATH% -Dinputcollector.thing.alternateName=Username ch.sdi.SocialDataImporter 
set CMD_LINE=java -cp %CLASSPATH% ch.sdi.SocialDataImporter 
echo executing command line: %CMD_LINE%
%CMD_LINE%

pause