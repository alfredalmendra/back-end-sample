package com.bes;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.expressme.openid.Association;
import org.expressme.openid.Authentication;
import org.expressme.openid.Endpoint;
import org.expressme.openid.OpenIdException;
import org.expressme.openid.OpenIdManager;

public class AuthenticationServlet extends HttpServlet {

	static final long ONE_HOUR = 3600000L;
	static final long TWO_HOUR = ONE_HOUR * 2L;
	static final String ATTR_MAC = "openid_mac";
	static final String ATTR_ALIAS = "openid_alias";

	OpenIdManager manager;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		manager = new OpenIdManager();
		// change to your domain
		manager.setRealm("http://localhost");
		// change to your servlet url
		// manager.setReturnTo("http://localhost/servlet-mapping");
		manager.setReturnTo("http://localhost/myservlet");
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String op = request.getParameter("op");
		if (op == null) {
			// check nonce:
			checkNonce(request.getParameter("openid.response_nonce"));
			// get authentication:
			byte[] mac_key = (byte[]) request.getSession().getAttribute(
					ATTR_MAC);
			String alias = (String) request.getSession().getAttribute(
					ATTR_ALIAS);
			Authentication authentication = manager.getAuthentication(request,
					mac_key, alias);
			String identity = authentication.getIdentity();
			String email = authentication.getEmail();
			// TODO: create user if not exist in database:
			showAuthentication(response.getWriter(), identity, email);
		} else if ("Google".equals(op)) {
			// redirect to Google sign on page:
			Endpoint endpoint = manager.lookupEndpoint("Google");
			Association association = manager.lookupAssociation(endpoint);
			request.getSession().setAttribute(ATTR_MAC,
					association.getRawMacKey());
			request.getSession().setAttribute(ATTR_ALIAS, endpoint.getAlias());
			String url = manager.getAuthenticationUrl(endpoint, association);
			response.sendRedirect(url);
		} else {
			throw new ServletException("Bad parameter op=" + op);
		}
	}

	void showAuthentication(PrintWriter pw, String identity, String email) {
		pw.print("<html><body><h1>Identity</h1><p>");
		pw.print(identity);
		pw.print("</p><h1>Email</h1><p>");
		pw.print(email == null ? "(null)" : email);
		pw.print("</p></body></html>");
		pw.flush();
	}

	void checkNonce(String nonce) {
		// check response_nonce to prevent replay-attack:
		if (nonce == null || nonce.length() < 20)
			throw new OpenIdException("Verify failed.");
		long nonceTime = getNonceTime(nonce);
		long diff = System.currentTimeMillis() - nonceTime;
		if (diff < 0)
			diff = (-diff);
		if (diff > ONE_HOUR)
			throw new OpenIdException("Bad nonce time.");
		if (isNonceExist(nonce))
			throw new OpenIdException("Verify nonce failed.");
		storeNonce(nonce, nonceTime + TWO_HOUR);
	}

	boolean isNonceExist(String nonce) {
		// TODO: check if nonce is exist in database:
		return false;
	}

	void storeNonce(String nonce, long expires) {
		// TODO: store nonce in database:
	}

	long getNonceTime(String nonce) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(
					nonce.substring(0, 19) + "+0000").getTime();
		} catch (ParseException e) {
			throw new OpenIdException("Bad nonce time.");
		}
	}

}
