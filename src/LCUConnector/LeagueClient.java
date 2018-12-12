package LCUConnector;


import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.Charset;


public class LeagueClient implements ILeagueClient
{
    String LeagueExecutablePath;

    String Token;
    String ApiUri;
    int Port;
    int LeaguePID;

    private HttpClient Client;
    byte[] EncodedAuth;
    HttpPost httpost;
    HttpGet httpget;
    HttpPut httput;
    HttpDelete httpdelete;

    /**
     * Used for Creating the connection if you know the location of the league of legends execuatble.
     * @param leagueExecutablePath Path to the LeagueOfLegends executable
     */
    public LeagueClient(String leagueExecutablePath)
    {
        this.Client =  HttpClientBuilder.create().build();
        this.LeagueExecutablePath = leagueExecutablePath;
        //ExecuteGetRequest("lol-maps/v1/maps", new Object()); //Todo: Remove Later, this is used for testing
    }

    /**
     * Used For Creating the connection if you dont know where the League Of Legends executable is
     */
    public LeagueClient()
    {

    }

    public void Connect()
    {
        GetLockFileCredentials(LeagueExecutablePath);
    }


    public HttpResponse MakeApiRequest(HttpMethod methodType, String endpoint, Object Data) throws RequestTypeNotFoundException
    {
        switch(methodType)
        {
            case Get:
                return ExecuteGetRequest(endpoint, Data);
            case Put:
                return ExecutePutRequest(endpoint,Data);
            case Post:
                return ExecutePostRequest(endpoint,Data);
            case Delete:
                return ExecuteDeleteRequest(endpoint,Data);
            default:
                throw new RequestTypeNotFoundException(methodType);
        }
    }

    public void Disconnect()
    {
        //todo
    }

    private void GetLockFileCredentials(String PathToLockFile)
    {
        BufferedReader reader;
        try
        {
            reader = new BufferedReader(new FileReader(PathToLockFile));
            String line = reader.readLine();
            String[] items = line.split(":");
            this.Token = items[3];
            this.Port = Integer.parseInt(items[2]);
            this.ApiUri = "https://127.0.0.1:" + this.Port + "/";
            this.LeaguePID = Integer.parseInt(items[1]);


            String auth = "riot:" + this.Token;
            this.EncodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));

        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private HttpResponse ExecuteGetRequest(String endPoint, Object data)
    {
        try
        {
            this.httpget =  new HttpGet(ApiUri + endPoint);
            this.httpget.setHeader("Accept", "application/json");

            httpget.setHeader("Content-type", "application/json");
            httpget.setHeader("Authorization", "Basic " +  new String(this.EncodedAuth));
            HttpResponse response = this.Client.execute(httpget);
            return response;
        }

        catch(Exception e)
        {
            System.out.println(e);
        }
        return null;
    }

    private HttpResponse ExecutePostRequest(String endPoint, Object data)
    {
        try
        {
            this.httpost =  new HttpPost(ApiUri + endPoint);
            this.httpost.setHeader("Accept", "application/json");

            String json = (String)data;
            StringEntity entity = new StringEntity(json);
            this.httpost.setEntity(entity);

            httpost.setHeader("Content-type", "application/json");
            httpost.setHeader("Authorization", "Basic " +  new String(this.EncodedAuth));
            HttpResponse response = this.Client.execute(httpost);

            return response;
        }

        catch(Exception e)
        {
            System.out.println(e);
        }
        return null;
    }

    private HttpResponse ExecutePutRequest(String endPoint, Object data)
    {
        try
        {
            this.httput =  new HttpPut(ApiUri + endPoint);
            this.httput.setHeader("Accept", "application/json");

            String json = (String)data;
            StringEntity entity = new StringEntity(json);
            this.httput.setEntity(entity);

            httput.setHeader("Content-type", "application/json");
            httput.setHeader("Authorization", "Basic " +  new String(this.EncodedAuth));
            HttpResponse response = this.Client.execute(httput);

            return response;
        }

        catch(Exception e)
        {
            System.out.println(e);
        }
        return null;
    }

    private HttpResponse ExecuteDeleteRequest(String endPoint, Object data)
    {
        try
        {
            this.httpdelete =  new HttpDelete(ApiUri + endPoint);
            this.httpdelete.setHeader("Accept", "application/json");

            httpdelete.setHeader("Content-type", "application/json");
            httpdelete.setHeader("Authorization", "Basic " +  new String(this.EncodedAuth));
            HttpResponse response = this.Client.execute(httpdelete);

            return response;
        }

        catch(Exception e)
        {
            System.out.println(e);
        }
        return null;
    }


}
