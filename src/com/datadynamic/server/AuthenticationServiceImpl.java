package com.datadynamic.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.datadynamic.client.remoteservices.AuthenticationService;
import com.datadynamic.server.utils.AuthenticationUtils;
import com.datadynamic.server.utils.Configuration;
import com.datadynamic.server.utils.DB;
import com.datadynamic.server.utils.ServerLog;
import com.datadynamic.shared.FieldVerifier;
import com.datadynamic.shared.pojos.ActionResponse;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.warrenstrange.googleauth.GoogleAuthenticator;

@SuppressWarnings("serial")
public class AuthenticationServiceImpl extends RemoteServiceServlet implements AuthenticationService {

   /*@Override
    public String processCall( String payload ) {
        HttpServletRequest req = getThreadLocalRequest();
        HttpSession session = req.getSession();
		return payload;
    }*/
	
	@Override
	public ActionResponse isLoggedIn() {	
		if(AuthenticationUtils.IsAuthenticated(this.getThreadLocalRequest())) {
			return new ActionResponse();
		} else {
			return new ActionResponse(false, "session, not logged in");
		}
	}

	@Override
	public ActionResponse login(String username, int token) {		
		// Replace session if they already have one
		HttpServletRequest request = this.getThreadLocalRequest();
		if(request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }
		
		String enabledevadmin = Configuration.getSetting("enabledevadmin");		
		if(enabledevadmin.equals("true") == true && username.equals("admin") == true) {
			HttpSession session = request.getSession();
			session.setAttribute("login.isDone", "true");
			session.setAttribute("user.name", username);
            session.setAttribute("user.role", "admin");
            return new ActionResponse();
		}
		
		String isValid = FieldVerifier.isValidCredentials(username, token);
		
		if(isValid.equals("true")) {
			try {
				Connection conn = DB.getConnection();
	            PreparedStatement pstmtUserMeta =
	                conn.prepareStatement("SELECT top 1 password, invalidattempts, secretkey, role FROM users where username = ?");
	            pstmtUserMeta.setString(1, username);                
	            ResultSet clientPasswordRS = pstmtUserMeta.executeQuery();            
	            
	            String clientPassword = "";
				int invalidAttempts = -1;
	            String secretKey = "";
	            String role = "";
	            
	            while(clientPasswordRS.next()) {
	                clientPassword = clientPasswordRS.getString("password");
	                invalidAttempts = clientPasswordRS.getInt("invalidattempts");
	                secretKey = clientPasswordRS.getString("secretkey");
	                role = clientPasswordRS.getString("role");
	            }
	            clientPasswordRS.close();
	            
	            if(clientPassword.equals("")) {
	                PreparedStatement pstmtUpdateLoginHistory =
	                    conn.prepareStatement(
	                        "insert into loginhistory (username, successful, date, ipaddress, status) values " +
	                        "(?, 0, ?, ?, 0, 1, 0)");                    
	                pstmtUpdateLoginHistory.setString(1, username);
	                pstmtUpdateLoginHistory.setString(2, (new Date()).toString());
	                pstmtUpdateLoginHistory.setString(3, request.getRemoteAddr());
	                pstmtUpdateLoginHistory.setString(4, "no result from username query");
	                pstmtUpdateLoginHistory.execute();                  
	                return new ActionResponse(false, "invalid credentials");
	            }
	            
	            if(invalidAttempts > Integer.parseInt(Configuration.getSetting("userloginattempts"))) {
	            	if(Configuration.getSetting("userlockout").equals("true")) {
		                PreparedStatement pstmtUpdateLoginHistory =
			                    conn.prepareStatement(
			                        "insert into loginhistory (username, successful, date, ipaddress, status) values " +
			                        "(?, 0, ?, ?, 0, 0, 1)");                    
		                pstmtUpdateLoginHistory.setString(1, username);
		                pstmtUpdateLoginHistory.setString(2, (new Date()).toString());
		                pstmtUpdateLoginHistory.setString(3, request.getRemoteAddr());
		                pstmtUpdateLoginHistory.setString(4, "attempts exceeded");
		                pstmtUpdateLoginHistory.execute();                
		                return new ActionResponse(false, "attempts exceeded");	            		
	            	}
	            }
	            
	            GoogleAuthenticator gAuth = new GoogleAuthenticator();
                if (gAuth.authorize(secretKey, token) == true) {
                    HttpSession session = request.getSession();
                    
                    PreparedStatement pstmtUpdateAttempts =
                        conn.prepareStatement("update users set invalidattempts = 0 where username = ?");                    
                    pstmtUpdateAttempts.setString(1, username);
                    pstmtUpdateAttempts.execute();                    
                    
                    PreparedStatement pstmtUpdateLoginHistory =
                        conn.prepareStatement(
                            "insert into loginhistory (username, successful, date, ipaddress, status) values " +
                            "(?, 1, ?, ?, 0, 0, 0)");                    
                    pstmtUpdateLoginHistory.setString(1, username);
                    pstmtUpdateLoginHistory.setString(2, (new Date()).toString());
                    pstmtUpdateLoginHistory.setString(3, request.getRemoteAddr());
                    pstmtUpdateLoginHistory.setString(4, "login success");
                    pstmtUpdateLoginHistory.execute();                     
                    
                    session.setAttribute("login.isDone", "true");
        			session.setAttribute("user.name", username);
                    session.setAttribute("user.role", role);
                    return new ActionResponse();	                    
                } else {
                    PreparedStatement pstmtUpdateAttempts =
                        conn.prepareStatement("update users set invalidattempts = invalidattempts + 1 where username = ?");                    
                    pstmtUpdateAttempts.setString(1, username);
                    pstmtUpdateAttempts.execute();
                    
                    PreparedStatement pstmtUpdateLoginHistory =
                        conn.prepareStatement(
                            "insert into loginhistory (username, successful, date, ipaddress, status) values " +
                            "(?, 0, ?, ?, 1, 0, 0)");                    
                    pstmtUpdateLoginHistory.setString(1, username);
                    pstmtUpdateLoginHistory.setString(2, (new Date()).toString());
                    pstmtUpdateLoginHistory.setString(3, request.getRemoteAddr());
                    pstmtUpdateLoginHistory.setString(4, "invalid credentials");
                    pstmtUpdateLoginHistory.execute();	                    
                    return new ActionResponse(false, "invalid credentials");
                }       
			} catch (Exception ex) {
				ServerLog.error(ex.getMessage());
				return new ActionResponse(false, "no db error");
			}
		} else {
			return new ActionResponse(false, "not valid credentials");
		}			
	}

	@Override
	public ActionResponse logout() {
		HttpServletRequest request = this.getThreadLocalRequest();
		if(request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }
		return new ActionResponse();
	}	

}
