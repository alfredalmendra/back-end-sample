package com.bes;

public class GoogleAuthenticationSample {
	public static void main(String... args) {

		java.util.Properties props = System.getProperties();
		props.put("proxySet", "true");
		props.put("proxyHost", "10.43.100.13");
		props.put("proxyPort", "8080");

		class MyServ extends Acme.Serve.Serve {
			// Overriding method for public access
			public void setMappingTable(PathTreeDictionary mappingtable) {
				super.setMappingTable(mappingtable);
			}

			// add the method below when .war deployment is needed
			public void addWarDeployer(String deployerFactory, String throttles) {
				super.addWarDeployer(deployerFactory, throttles);
			}
		}
		;

		final MyServ srv = new MyServ();
		// setting aliases, for an optional file servlet
		Acme.Serve.Serve.PathTreeDictionary aliases = new Acme.Serve.Serve.PathTreeDictionary();
		aliases.put("/*", new java.io.File("C:\\temp"));
		// note cast name will depend on the class name, since it is anonymous
		// class
		srv.setMappingTable(aliases);
		// setting properties for the server, and exchangeable Acceptors
		java.util.Properties properties = new java.util.Properties();
		properties.put("port", 80);
		properties.setProperty(Acme.Serve.Serve.ARG_NOHUP, "nohup");
		srv.arguments = properties;
		srv.addDefaultServlets(null); // optional file servlet
		srv.addServlet("/myservlet", new AuthenticationServlet()); // optional
		// the pattern above is exact match, use /myservlet/* for mapping any
		// path startting with /myservlet (Since 1.93)
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				srv.notifyStop();
				srv.destroyAllServlets();
			}
		}));
		srv.serve();
	}
}