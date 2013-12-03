package br.com.ufrrj.conexao.twitter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import br.com.ufrrj.base.Connector;
import br.com.ufrrj.base.Data;
import br.com.ufrrj.conexao.twitter.util.APIType;
import br.com.ufrrj.conexao.twitter.util.OAuthTokenSecret;
import br.com.ufrrj.conexao.twitter.util.OAuthUtils;
import br.com.ufrrj.conexao.twitter.util.Profile;
import br.com.ufrrj.conexao.twitter.util.SearchResults;
import br.com.ufrrj.conexao.twitter.util.TwitterOAuthSecret;
import br.com.ufrrj.conexao.twitter.util.Twitts;

public class TwitterConexao implements Connector {

    private BufferedWriter OutFileWriter;
	private OAuthTokenSecret OAuthTokens;
	
	
	private ArrayList<String> Usernames = new ArrayList<String>();
	private OAuthConsumer Consumer;

	
	@Override
	public Data getUserPosts(String url) {

		Twitts twitts = new Twitts();

		LoadTwitterToken();

		this.Consumer = GetConsumer();
        
		twitts.setData(GetStatuses(url));

		return twitts;
	}
	
	@Override
	public Data performASearch(String query) {
		
		LoadTwitterToken();
		Consumer = GetConsumer();
		SearchResults resultados = new SearchResults();
		resultados.setData(GetSearchResults(query));
		
		return resultados;
	}
	
	
	@Override
	public Data getProfile(String url) {
		
		LoadTwitterToken();
		this.Consumer = GetConsumer();
		
		Profile p = new Profile();
		p.setData(GetProfile(url));
		return p;
	}
	
	
	/**
	 * Cria um OAuthConsumer com os tokens de acesso
	 * 
	 * 
	 * @return consumer
	 */

	public OAuthConsumer GetConsumer() {
		OAuthConsumer consumer = new DefaultOAuthConsumer(
				OAuthUtils.CONSUMER_KEY, OAuthUtils.CONSUMER_SECRET);

		consumer.setTokenWithSecret(OAuthTokens.getAccessToken(),
				OAuthTokens.getAccessSecret());
		return consumer;
	}

	
	/**
	 * Carrega o acessToken
	 */
	public void LoadTwitterToken() {
	
		OAuthTokens = TwitterOAuthSecret.userAccessSecret();
	}

	
	
	public JSONObject GetRateLimitStatus() {
		try {
			URL url = new URL(
					"https://api.twitter.com/1.1/application/rate_limit_status.json");
			HttpURLConnection huc = (HttpURLConnection) url.openConnection();
			huc.setReadTimeout(5000);
			Consumer.sign(huc);
			huc.connect();
			BufferedReader bRead = new BufferedReader(new InputStreamReader(
					(InputStream) huc.getContent()));
			StringBuffer page = new StringBuffer();
			String temp = "";
			while ((temp = bRead.readLine()) != null) {
				page.append(temp);
			}
			bRead.close();
			return (new JSONObject(page.toString()));
		} catch (JSONException ex) {
			Logger.getLogger(TwitterConexao.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (OAuthCommunicationException ex) {
			Logger.getLogger(TwitterConexao.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (OAuthMessageSignerException ex) {
			Logger.getLogger(TwitterConexao.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (OAuthExpectationFailedException ex) {
			Logger.getLogger(TwitterConexao.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (IOException ex) {
			Logger.getLogger(TwitterConexao.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		return null;
	}

	/**
	 * inicializa o FileWriter
	 * 
	 * 
	 *           
	 * 
	 *           
	 */
	public void InitializeWriters(String outFilename) {
		try {
			File fl = new File(outFilename);
			if (!fl.exists()) {
				fl.createNewFile();
			}
			/**
			 * Use UTF-8 encoding when saving files to avoid losing Unicode
			 * characters in the data
			 */
			OutFileWriter = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outFilename, true), "UTF-8"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Fecha o fileWriter para salvar os arquivos
	 */
	public void CleanupAfterFinish() {
		try {
			OutFileWriter.close();
		} catch (IOException ex) {
			Logger.getLogger(TwitterConexao.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

	/**
	 * Escreve os arquivos recebidos no output file
	 * 
	 * @param data
	 *            contem a informaçao recebida pelo json
	 * @param user
	 *            name of the user currently being written
	 */
	public void WriteToFile(String user, String data) {
		try {
			OutFileWriter.write(data);
			OutFileWriter.newLine();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Retorna as informaçoes do profile do usuario
	 * 
	 * @param username
	 *           . nome do usuario que se deseja retornar o profile
	 * @return as informaçoes do profile como um JsonObject
	 */
	public JSONObject GetProfile(String username) {
		BufferedReader bRead = null;
		JSONObject profile = null;
		try {
			System.out.println("Processing profile of " + username);
			boolean flag = true;
			URL url = new URL(
					"https://api.twitter.com/1.1/users/show.json?screen_name="
							+ username);
			HttpURLConnection huc = (HttpURLConnection) url.openConnection();
			huc.setReadTimeout(5000);
			// Step 2: Sign the request using the OAuth Secret
			Consumer.sign(huc);
			huc.connect();
			if (huc.getResponseCode() == 404 || huc.getResponseCode() == 401) {
				System.out.println(huc.getResponseMessage());
			} else if (huc.getResponseCode() == 500
					|| huc.getResponseCode() == 502
					|| huc.getResponseCode() == 503) {
				try {
					huc.disconnect();
					System.out.println(huc.getResponseMessage());
					Thread.sleep(3000);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			} else
			// Step 3: If the requests have been exhausted, then wait until the
			// quota is renewed
			if (huc.getResponseCode() == 429) {
				try {
					huc.disconnect();
					Thread.sleep(this.GetWaitTime("/users/show/:id"));
					flag = false;
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
			if (!flag) {
				// recreate the connection because something went wrong the
				// first time.
				huc.connect();
			}
			StringBuilder content = new StringBuilder();
			if (flag) {
				bRead = new BufferedReader(new InputStreamReader(
						(InputStream) huc.getContent()));
				String temp = "";
				while ((temp = bRead.readLine()) != null) {
					content.append(temp);
				}
			}
			huc.disconnect();
			try {
				profile = new JSONObject(content.toString());
			} catch (JSONException ex) {
				ex.printStackTrace();
			}
		} catch (OAuthCommunicationException ex) {
			ex.printStackTrace();
		} catch (OAuthMessageSignerException ex) {
			ex.printStackTrace();
		} catch (OAuthExpectationFailedException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return profile;
	}

	/**
	 * Retorna os seguidores de um usuario
	 * 
	 * @param username
	 *           o nome do usuario que voce deseja que sejam retornados os seguidores
	 *           
	 * @return uma lista contendo os seguidores de um usuario
	 */
	public JSONArray GetFollowers(String username) {
		BufferedReader bRead = null;
		JSONArray followers = new JSONArray();
		try {
			System.out.println(" followers user = " + username);
			long cursor = -1;
			while (true) {
				if (cursor == 0) {
					break;
				}
				// Step 1: Create the APi request using the supplied username
				URL url = new URL(
						"https://api.twitter.com/1.1/followers/list.json?screen_name="
								+ username + "&cursor=" + cursor);
				HttpURLConnection huc = (HttpURLConnection) url
						.openConnection();
				huc.setReadTimeout(5000);
				// Step 2: Sign the request using the OAuth Secret
				Consumer.sign(huc);
				huc.connect();
				if (huc.getResponseCode() == 400
						|| huc.getResponseCode() == 404) {
					System.out.println(huc.getResponseMessage());
					break;
				} else if (huc.getResponseCode() == 500
						|| huc.getResponseCode() == 502
						|| huc.getResponseCode() == 503
						|| huc.getResponseCode() == 504) {
					try {
						System.out.println(huc.getResponseMessage());
						huc.disconnect();
						Thread.sleep(3000);
						continue;
					} catch (InterruptedException ex) {
						Logger.getLogger(TwitterConexao.class.getName()).log(
								Level.SEVERE, null, ex);
					}
				} else
				// Step 3: If the requests have been exhausted, then wait until
				// the quota is renewed
				if (huc.getResponseCode() == 429) {
					try {
						huc.disconnect();
						Thread.sleep(this.GetWaitTime("/followers/list"));
						continue;
					} catch (InterruptedException ex) {
						Logger.getLogger(TwitterConexao.class.getName()).log(
								Level.SEVERE, null, ex);
					}
				}
				// Step 4: Retrieve the followers list from Twitter
				bRead = new BufferedReader(new InputStreamReader(
						(InputStream) huc.getContent()));
				StringBuilder content = new StringBuilder();
				String temp = "";
				while ((temp = bRead.readLine()) != null) {
					content.append(temp);
				}
				try {
					JSONObject jobj = new JSONObject(content.toString());
					// Step 5: Retrieve the token for the next request
					cursor = jobj.getLong("next_cursor");
					JSONArray idlist = jobj.getJSONArray("users");
					if (idlist.length() == 0) {
						break;
					}
					for (int i = 0; i < idlist.length(); i++) {
						followers.put(idlist.getJSONObject(i));
					}
				} catch (JSONException ex) {
					Logger.getLogger(TwitterConexao.class.getName()).log(Level.SEVERE,
							null, ex);
				}
			}
		} catch (OAuthCommunicationException ex) {
			ex.printStackTrace();
		} catch (OAuthMessageSignerException ex) {
			ex.printStackTrace();
		} catch (OAuthExpectationFailedException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return followers;
	}

	/**
	 * Retorna as mensagens de um determinado usuario
	 * 
	 * @param username
	 *            o nome do usuario que voce deseja ter as mensagens
	 *            
	 * @return uma lista com as mensagens
	 */
	public JSONArray GetStatuses(String username) {
		BufferedReader bRead = null;
		// Get the maximum number of tweets possible in a single page 200
		int tweetcount = 10;
		// Include include_rts because it is counted towards the limit anyway.
		boolean include_rts = true;
		JSONArray statuses = new JSONArray();
		try {
			System.out.println("Processing status messages of " + username);
			long maxid = 0;
			
			int paginas = 0;
			
			while (paginas < 50) {

				URL url = null;
				if (maxid == 0) {
					url = new URL(
							"https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name="
									+ username + "&include_rts=" + include_rts
									+ "&count=" + tweetcount);

				} else {
					// use max_id to get the tweets in the next page. Use
					// max_id-1 to avoid getting redundant tweets.
					url = new URL(
							"https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name="
									+ username + "&include_rts=" + include_rts
									+ "&count=" + tweetcount + "&max_id="
									+ (maxid - 1));

				}
				HttpURLConnection huc = (HttpURLConnection) url
						.openConnection();
				huc.setReadTimeout(5000);
				Consumer.sign(huc);
				huc.connect();
				if (huc.getResponseCode() == 400
						|| huc.getResponseCode() == 404) {
					System.out.println(huc.getResponseCode());
				
					break;
				} else if (huc.getResponseCode() == 500
						|| huc.getResponseCode() == 502
						|| huc.getResponseCode() == 503) {
					try {
						System.out.println(huc.getResponseCode());
						Thread.sleep(3000);
					} catch (InterruptedException ex) {
						Logger.getLogger(TwitterConexao.class.getName()).log(
								Level.SEVERE, null, ex);
					}
				} else
				// Step 3: If the requests have been exhausted, then wait until
				// the quota is renewed
				if (huc.getResponseCode() == 429) {
					try {
						huc.disconnect();
						Thread.sleep(this
								.GetWaitTime("/statuses/user_timeline"));
						continue;
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				}
				bRead = new BufferedReader(new InputStreamReader(
						(InputStream) huc.getInputStream()));
				StringBuilder content = new StringBuilder();
				String temp = "";
				while ((temp = bRead.readLine()) != null) {
					content.append(temp);
				}
				try {

					JSONArray statusarr = new JSONArray(content.toString());
					if (statusarr.length() == 0) {
						break;
					}
					for (int i = 0; i < statusarr.length(); i++) {
						JSONObject jobj = statusarr.getJSONObject(i);
						statuses.put(jobj);

						// Get the max_id to get the next batch of tweets
						if (!jobj.isNull("id")) {
							maxid = jobj.getLong("id");

						}

					}
				} catch (JSONException ex) {
					ex.printStackTrace();
				}
             paginas++;
			}
			System.out.println(statuses.length());
		} catch (OAuthCommunicationException ex) {
			ex.printStackTrace();
		} catch (OAuthMessageSignerException ex) {
			ex.printStackTrace();
		} catch (OAuthExpectationFailedException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return statuses;
	}

	/**
	 * Retorna os amigos de um usuario
	 * 
	 * @param username
	 *           o nome do usuario que voce deseja obbter a lista de amigos
	 * @return uma lista contendo os amigos do usuario
	 */
	public JSONArray GetFriends(String username) {
		BufferedReader bRead = null;
		JSONArray friends = new JSONArray();
		try {
			System.out.println("Processing friends of " + username);
			long cursor = -1;
			while (true) {
				if (cursor == 0) {
					break;
				}
				// Step 1: Create the APi request using the supplied username
				URL url = new URL(
						"https://api.twitter.com/1.1/friends/list.json?screen_name="
								+ username + "&cursor=" + cursor);
				HttpURLConnection huc = (HttpURLConnection) url
						.openConnection();
				huc.setReadTimeout(5000);
				// Step 2: Sign the request using the OAuth Secret
				Consumer.sign(huc);
				huc.connect();
				if (huc.getResponseCode() == 400
						|| huc.getResponseCode() == 401) {
					System.out.println(huc.getResponseMessage());
					break;
				} else if (huc.getResponseCode() == 500
						|| huc.getResponseCode() == 502
						|| huc.getResponseCode() == 503) {
					try {
						System.out.println(huc.getResponseMessage());
						Thread.sleep(3000);
						continue;
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				} else
				// Step 3: If the requests have been exhausted, then wait until
				// the quota is renewed
				if (huc.getResponseCode() == 429) {
					try {
						huc.disconnect();
						Thread.sleep(this.GetWaitTime("/friends/list"));
						continue;
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				}
				// Step 4: Retrieve the friends list from Twitter
				bRead = new BufferedReader(new InputStreamReader(
						(InputStream) huc.getContent()));
				StringBuilder content = new StringBuilder();
				String temp = "";
				while ((temp = bRead.readLine()) != null) {
					content.append(temp);
				}
				try {
					JSONObject jobj = new JSONObject(content.toString());
					// Step 5: Retrieve the token for the next request
					cursor = jobj.getLong("next_cursor");
					JSONArray userlist = jobj.getJSONArray("users");
					if (userlist.length() == 0) {
						break;
					}
					for (int i = 0; i < userlist.length(); i++) {
						friends.put(userlist.get(i));
					}
				} catch (JSONException ex) {
					ex.printStackTrace();
				}
				huc.disconnect();
			}
		} catch (OAuthCommunicationException ex) {
			ex.printStackTrace();
		} catch (OAuthMessageSignerException ex) {
			ex.printStackTrace();
		} catch (OAuthExpectationFailedException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return friends;
	}

	/**
	 * Retorna o tempo de espera se o limite da RestApi For alcançado
	 * 
	 * @param api
	 *            o nome da Api que esta sendo usada
	 * @return um numero em millisegundos que se deve esperar até a prosima requisição
	 */
	public long GetWaitTime(String api) {
		JSONObject jobj = this.GetRateLimitStatus();
		if (jobj != null) {
			try {
				if (!jobj.isNull("resources")) {
					JSONObject resourcesobj = jobj.getJSONObject("resources");
					JSONObject apilimit = null;
					if (api.equals(APIType.USER_TIMELINE)) {
						JSONObject statusobj = resourcesobj
								.getJSONObject("statuses");
						apilimit = statusobj.getJSONObject(api);
					} else if (api.equals(APIType.FOLLOWERS)) {
						JSONObject followersobj = resourcesobj
								.getJSONObject("followers");
						apilimit = followersobj.getJSONObject(api);
					} else if (api.equals(APIType.FRIENDS)) {
						JSONObject friendsobj = resourcesobj
								.getJSONObject("friends");
						apilimit = friendsobj.getJSONObject(api);
					} else if (api.equals(APIType.USER_PROFILE)) {
						JSONObject userobj = resourcesobj
								.getJSONObject("users");
						apilimit = userobj.getJSONObject(api);
					}
					int numremhits = apilimit.getInt("remaining");
					if (numremhits <= 1) {
						long resettime = apilimit.getInt("reset");
						resettime = resettime * 1000; // convert to milliseconds
						return resettime;
					}
				}
			} catch (JSONException ex) {
				ex.printStackTrace();
			}
		}
		return 0;
	}

	
	
	
	
	/**
     * Realiza uma busca que coincida com o parametro dado
     * @param  criterio de busca ex: hashtags
     * @return um array com os status dos usuarios
     */
    public JSONArray GetSearchResults(String query)
    {
        try{
            //construct the request url
            String URL_PARAM_SEPERATOR = "&";
            StringBuilder url = new StringBuilder();
            url.append("https://api.twitter.com/1.1/search/tweets.json?q=");
            //query needs to be encoded
            url.append(URLEncoder.encode(query, "UTF-8"));
            url.append(URL_PARAM_SEPERATOR);
            url.append("count=100");
            URL navurl = new URL(url.toString());
            HttpURLConnection huc = (HttpURLConnection) navurl.openConnection();
            huc.setReadTimeout(5000);
            Consumer.sign(huc);
            huc.connect();
            if(huc.getResponseCode()==400||huc.getResponseCode()==404||huc.getResponseCode()==429)
            {
                System.out.println(huc.getResponseMessage());
                try {
                    huc.disconnect();
                    Thread.sleep(this.GetWaitTime("/friends/list"));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            if(huc.getResponseCode()==500||huc.getResponseCode()==502||huc.getResponseCode()==503)
            {
                System.out.println(huc.getResponseMessage());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TwitterConexao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            BufferedReader bRead = new BufferedReader(new InputStreamReader((InputStream) huc.getInputStream()));
            String temp;
            StringBuilder page = new StringBuilder();
            while( (temp = bRead.readLine())!=null)
            {
                page.append(temp);
            }
            JSONTokener jsonTokener = new JSONTokener(page.toString());
            try {
                JSONObject json = new JSONObject(jsonTokener);
                JSONArray results = json.getJSONArray("statuses");
                return results;
            } catch (JSONException ex) {
                Logger.getLogger(TwitterConexao.class.getName()).log(Level.SEVERE, null, ex);
            }            
        } catch (OAuthCommunicationException ex) {
            Logger.getLogger(TwitterConexao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OAuthMessageSignerException ex) {
            Logger.getLogger(TwitterConexao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OAuthExpectationFailedException ex) {
            Logger.getLogger(TwitterConexao.class.getName()).log(Level.SEVERE, null, ex);
        }catch(IOException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

	
	
	
	
	

	

}
