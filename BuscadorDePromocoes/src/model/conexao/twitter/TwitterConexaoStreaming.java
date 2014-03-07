/* TweetTracker. Copyright (c) Arizona Board of Regents on behalf of Arizona State University
 * @author shamanth
 */
package model.conexao.twitter;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import model.base.Connector;
import model.base.Data;
import model.conexao.twitter.util.OAuthTokenSecret;
import model.conexao.twitter.util.OAuthUtils;
import model.conexao.twitter.util.Tweet;
import model.conexao.twitter.util.TwitterOAuthSecret;
import model.conexao.twitter.util.DataRetrieved;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import controller.bd.Dao;
import controller.bd.TweetDao;


public class TwitterConexaoStreaming implements Connector
{
	private OAuthTokenSecret OAuthToken;
    private final int RECORDS_TO_PROCESS = 1000;
    private final int MAX_GEOBOXES = 25;
    private final int MAX_KEYWORDS = 400;
    private final int MAX_USERS = 5000;
    private  HashSet<String> Keywords;
    private HashSet<String> Geoboxes;
    private HashSet<String> Userids;
    private  DataRetrieved retorno;
    private Dao banco = new TweetDao();
    //pode ser lido de um banco de dados
    private final String CONFIG_FILE_PATH = "streaming/streaming.config";
    
    private final String DEF_OUTPATH = "streaming/";

    /**
     * Loads the Twitter access token and secret for a user
     */    
    private void LoadTwitterToken()
    {
//        OAuthExample oae = new OAuthExample();
//        OAuthToken =  oae.GetUserAccessKeySecret();
        OAuthToken = TwitterOAuthSecret.userAccessSecret();
    }
    
    @Override
	public Data getUserPosts(String url) {
    	
		LoadTwitterToken();
		ReadParameters(CONFIG_FILE_PATH);
		InputStream in = CreateStreamingConnection(url);
		ProcessTwitterStream(in);
		
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Data getProfile(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Data performASearch(String query) {
		// TODO Auto-generated method stub
		return null;
	}


    /**
     * Creates a connection to the Streaming Filter API
     * @param baseUrl the URL for Twitter Filter API
     * @param outFilePath Location to place the exported file
     */
    private InputStream CreateStreamingConnection(String baseUrl)
    {
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, new Integer(90000));
        //Step 1: Initialize OAuth Consumer
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(OAuthUtils.CONSUMER_KEY,OAuthUtils.CONSUMER_SECRET);
        consumer.setTokenWithSecret(OAuthToken.getAccessToken(),OAuthToken.getAccessSecret());
        //Step 2: Create a new HTTP POST request and set parameters
        HttpPost httppost = new HttpPost(baseUrl);
        try {            
            httppost.setEntity(new UrlEncodedFormEntity(CreateRequestBody(), "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        try {
             //Step 3: Sign the request
                consumer.sign(httppost);
            } catch (OAuthMessageSignerException ex) {
                ex.printStackTrace();
            } catch (OAuthExpectationFailedException ex) {
                ex.printStackTrace();
            } catch (OAuthCommunicationException ex) {
                ex.printStackTrace();
            }
        HttpResponse response;
        InputStream is = null;
        try {
             //Step 4: Connect to the API
                response = httpClient.execute(httppost);
                if (response.getStatusLine().getStatusCode()!= HttpStatus.SC_OK)
                {
                    throw new IOException("Got status " +response.getStatusLine().getStatusCode());
                }
                else
                {
                    System.out.println(OAuthToken.getAccessToken()+ ": Processing from " + baseUrl);
                    HttpEntity entity = response.getEntity();
                    try {
                        is = entity.getContent();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (IllegalStateException ex) {
                        ex.printStackTrace();
                    }
                   
                }
         } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return is;
    }

    /**
     *  Processes a stream of tweets and writes them to a file one tweet per line. Each tweet here is represented by a JSON document.
     * @param is input stream already connected to the streaming API
     * @param outFilePath file to put the collected tweets in
     * @throws InterruptedException
     * @throws IOException
     */
    private void ProcessTwitterStream(InputStream is)
    {
      
        try {
        	
            JSONTokener jsonTokener = new JSONTokener(new InputStreamReader(is, "UTF-8"));
         
          
            while (true) {
            	
                try {   
                
                    JSONObject jsontweet = new JSONObject(jsonTokener);
                    JSONObject jsonuser = new JSONObject(jsontweet.getString("user"));
                    Tweet tweet = new Tweet();
                             
                    tweet.setId(jsontweet.getString("id_str"));
                    tweet.setPost(jsontweet.getString("text"));
                    tweet.setUsuario(jsonuser.getString("screen_name"));
                    tweet.setData(new Date(System.currentTimeMillis()));
                    tweet.setTime(new Time(System.currentTimeMillis()));
                      
                        //joga os Tweets para o banco de dados
            //----------------------------------------------------------------------------------------------
                        	System.out.println(tweet);
                        	banco.insert(tweet);

            //----------------------------------------------------------------------------------------------
                      
                      
                      
                       
                  
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }                
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } 
    }


    /**
     * Reads the file and loads the parameters to be crawled. Expects that the parameters are tab separated values and the
     * @param filename
     */
    private void ReadParameters(String filename)
    {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
            String temp = "";
            int count = 1;
            if(Userids==null)
            {
                Userids = new HashSet<String>();
            }
            if(Geoboxes==null)
            {
                Geoboxes = new HashSet<String>();
            }
            if(Keywords==null)
            {
                Keywords = new HashSet<String>();
            }
            while((temp = br.readLine())!=null)
            {
                if(!temp.isEmpty())
                {
                    if(count==1)
                    {                        
                        String[] keywords = temp.split("\t");
                        HashSet<String> temptags = new HashSet<String>();
                        for(String word:keywords)
                        {
                            if(!temptags.contains(word))
                            {
                                temptags.add(word);
                            }
                        }
                        FilterKeywords(temptags);
                    }
                    else
                    if(count==2)
                    {
                        String[] geoboxes = temp.split("\t");
                        HashSet<String> tempboxes = new HashSet<String>();
                        for(String box:geoboxes)
                        {
                            if(!tempboxes.contains(box))
                            {
                                tempboxes.add(box);
                            }
                        }
                        FilterGeoboxes(tempboxes);
                    }
                    else
                    if(count==3)
                    {
                        String[] userids = temp.split("\t");
                        HashSet<String> tempids = new HashSet<String>();
                        for(String id:userids)
                        {
                            if(!tempids.contains(id))
                            {
                                tempids.add(id);
                            }
                        }
                        FilterUserids(tempids);
                    }
                    count++;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        finally{
            try {
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void FilterUserids(HashSet<String> userids)
    {
        if(userids!=null)
        {
            int maxsize = MAX_USERS;
            if(userids.size()<maxsize)
            {
                maxsize = userids.size();
            }
            for(String id:userids)
            {
                Userids.add(id);
            }
        }
    }

    private void FilterGeoboxes(HashSet<String> geoboxes)
    {
        if(geoboxes!=null)
        {
            int maxsize = MAX_GEOBOXES;
            if(geoboxes.size()<maxsize)
            {
                maxsize = geoboxes.size();
            }
            for(String box:geoboxes)
            {
                Geoboxes.add(box);
            }
        }
    }
    /**
     * Keep only the maximum permitted number of parameters for a connection. Ignoring the rest.
     * This can be extended to create multiple sets to be crawled by different threads.
     */
    private void FilterKeywords(HashSet<String> hashtags)
    {          
        if(hashtags!=null)
        {
            int maxsize = MAX_KEYWORDS;
            if(hashtags.size()<maxsize)
            {
                maxsize = hashtags.size();
            }
            for(String tag:hashtags)
            {
                Keywords.add(tag);
            }
        }
             
    }

     private List<NameValuePair> CreateRequestBody()
     {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        if(Userids != null&&Userids.size()>0)
        {
            params.add(CreateNameValuePair("follow", Userids));
            System.out.println("userids = "+Userids);
        }
        if (Geoboxes != null&&Geoboxes.size()>0) {
            params.add(CreateNameValuePair("locations", Geoboxes));
            System.out.println("locations = "+Geoboxes);

        }
        if (Keywords != null&&Keywords.size()>0) {
            params.add(CreateNameValuePair("track", Keywords));
            System.out.println("keywords = "+Keywords);
        }
        return params;
    }

    private NameValuePair CreateNameValuePair(String name, Collection<String> items)
    {
        StringBuilder sb = new StringBuilder();
        boolean needComma = false;
        for (String item : items) {
            if (needComma) {
                sb.append(',');
            }
            needComma = true;
            sb.append(item);
        }
        return new BasicNameValuePair(name, sb.toString());
    }

	}
