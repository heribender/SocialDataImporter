SocialDataImporter
==================

Tool for bulk import of users into social platforms.

Beside this inherent usfulness it is also a kind of proove-of-concept on how to build a pluggable java stand alone application with the most up-to-date standard technologies.

Start date: 05.11.2014

Technologies:
  - java 8 (incl. streams and lamdas)
  - Dependency Injection: spring-context 4.1.1.RELEASE
  - JPA (hibernate JPA 2.0)
  - Hibernate 4.2.8.Final
  - mySqlConnector 5.1.34
  - javax.mail 1.4.7 
  - apache commons-email 1.3.3
  - FTP: apache commons-net 3.3
  - SSH: com.jcraft 0.1.5.1
  - several apache commons libraries
  - log4j2 2.1 (incl. several bridges for redirecting log outputs from included libs which use elder log technologies to log4j2)
  - maven 3.1.1
  - JUnit4 4.11
  - PowerMock 1.5.6
  - spring-test 4.1.1.RELEASE
  - hsqldb 2.3.2
  - Spring Tool Suite 3.6.2.RELEASE
  

For more infos see the wiki home (https://github.com/heribender/SocialDataImporter/wiki).

Co-developers are welcome!

Update 15.12.2014
-----------------
A fully working prototype is finished. The tool is now capable of:
  - Parse CSV data (incl. date values, avatar pictures in jpg hexadecimal format, custom formats with configurable and pluggable converters)
  - normalize the collected data (according to googles standardization attempts, see http://schema.org/Person)
  - import the collected data to an actual oxwall 1.7.1 installation (see http://www.oxwall.org) on a linux ubuntu 14.4.1 server, including DB, FTP and SSH access
  - Compiling a personalizable mail and send it out a SMTP server
  - everything is fully configurable and pluggable, with a centralized technique for configuration personalization in a manner which preservers the personalization when updating the product

The actual product version is "0.9RC1"

Update 22.03.2015
-----------------
After finetuning several aspects, fixing some bugs, adding a filter framework, finishing the documentation, fullfilling the TODO of a nicely formatted report and - the most important point - having used the tool for a successful import to a productive oxwall platform, I decided to release the version 1.0.
 
The actual product version is "1.0"



