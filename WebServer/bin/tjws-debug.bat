cd ..
set JDK_HOME=C:\Program Files\Java\jdk1.7.0
java -cp "lib\servlet.jar;lib\war.jar;lib\webserver.jar;lib\jasper.jar;%JDK_HOME%\lib\tools.jar" -Dtjws.webappdir=webapps -Dtjws.wardeploy.dynamically=16 -Dtjws.wardeploy.warname-as-context=yes Acme.Serve.Main -a aliases.properties -p 80 -l -c cgi-bin
