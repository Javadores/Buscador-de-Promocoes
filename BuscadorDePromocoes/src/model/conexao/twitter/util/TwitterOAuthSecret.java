package model.conexao.twitter.util;


public class TwitterOAuthSecret {
	
	
	 public static OAuthTokenSecret userAccessSecret()
	    {
	        String accesstoken = "1262619914-tcCPB1SyXy3BMuui9OAhprcPmqg3z2csSjDSCNY";
	        String accesssecret = "cXXO0qFLBjLXGtE97pnf5Vv1RZGxZ2FZ97wCYiaVU";
	        OAuthTokenSecret tokensecret = new OAuthTokenSecret(accesstoken,accesssecret);
	        return tokensecret;
	    }
 

}
