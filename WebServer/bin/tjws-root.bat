cd ..
set JDK_HOME=C:\Program Files\Java\jdk1.7.0
java -Xmx700m -cp "lib\servlet.jar;lib\war.jar;lib\webserver.jar;lib\jasper.jar;%JDK_HOME%\lib\tools.jar" -Dtjws.webappdir=webapps -Dtjws.wardeploy.warname-as-context=yes -Dtjws.wardeploy.as-root=addressbook Acme.Serve.Main -a aliases.properties -p 80 -l -c cgi-bin
