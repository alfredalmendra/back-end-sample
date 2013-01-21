cd ..
set JDK_HOME=C:\Program Files\Java\jdk1.7.0
java -Xmx700m -cp "lib\servlet.jar;lib\war.jar;lib\webserver.jar;lib\jasper.jar;%JDK_HOME%\lib\tools.jar" -Dtjws.webappdir=webapps -Dtjws.wardeploy.warname-as-context=yes -Dtjws.virtual -Dtjws.wardeploy.as-root.jaddressbook.com=addressbook -Dtjws.wardeploy.as-root.deuf.travelspal.com=sqlfair Acme.Serve.Main -a aliases.properties -p 80 -l -c cgi-bin
