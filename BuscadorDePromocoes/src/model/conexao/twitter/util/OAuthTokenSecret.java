package model.conexao.twitter.util;
/**
 * 
 * @author john
 * classe utilizada  para encapsular os tokens de segurança
 */
	
public class OAuthTokenSecret {

	String UserAccessToken;
	String UserAccessSecret;

	public String getAccessSecret() {
		return UserAccessSecret;
	}

	public void setAccessSecret(String AccessSecret) {
		this.UserAccessSecret = AccessSecret;
	}

	public String getAccessToken() {
		return UserAccessToken;
	}

	public void setAccessToken(String AccessToken) {
		this.UserAccessToken = AccessToken;
	}

	public OAuthTokenSecret(String token, String secret) {
		this.setAccessToken(token);
		this.setAccessSecret(secret);
	}

	@Override
	public String toString() {
		return "Access Token: " + getAccessToken() + " Access Secret: "
				+ getAccessSecret();
	}
}
