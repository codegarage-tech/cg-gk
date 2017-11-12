package com.customviews.library.httprequest;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONObject;

import com.customviews.library.model.TaskParameter;
import com.customviews.library.model.TaskResult;

import android.util.Log;

/**
 * This class encapsulates methods for requesting a server via HTTP GET/POST and
 * provides methods for parsing response from the server.
 */
public class HttpUtility {

    public static TaskResult doGetRequest(String requestURL) {

        TaskResult response = null;
        HttpURLConnection httpConn = null;

        try {

            URL url = new URL(requestURL);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setUseCaches(false);
            httpConn.setReadTimeout(15000);
            httpConn.setConnectTimeout(15000);
            httpConn.setRequestProperty("Content-type", "application/json");
            httpConn.setRequestProperty("Accept", "application/json");
            httpConn.setDoInput(true); // true if we want to read server's
            // response
            httpConn.setDoOutput(false); // false indicates this is a GET
            // request

            response = readStream(httpConn);

            return response;

        } catch (Exception e) {
            return new TaskResult(e);
        } finally {
            disconnectHttpURLConnection(httpConn);
        }
    }

    public static TaskResult doRestPostRequest(String URL, JSONObject param, ArrayList<TaskParameter> header) {

        URL url = null;
        HttpURLConnection urlConn = null;
        TaskResult response = null;

        try {

            url = new URL(URL);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setReadTimeout(15000);
            urlConn.setConnectTimeout(15000);
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            setHeader(urlConn, header);
            urlConn.connect();

            if (writeStream(urlConn, param)) {
                response = readStream(urlConn);
                Log.d("Post response:", response.getResult().toString());
            }

            return response;

        } catch (Exception e) {
            return new TaskResult(e);
        } finally {
            disconnectHttpURLConnection(urlConn);
        }
    }

    private static void setHeader(HttpURLConnection urlConnection, ArrayList<TaskParameter> header) {
        if (urlConnection != null) {
            if (header != null && header.size() > 0) {
                for (int i = 0; i < header.size(); i++) {
                    TaskParameter mHeader = header.get(i);
                    urlConnection.setRequestProperty(mHeader.getKey().toString(), mHeader.getValue().toString());
                }
            } else {
                urlConnection.setRequestProperty("Content-type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
            }
        }
    }

    public static boolean writeStream(HttpURLConnection httpURLConnection, JSONObject param) {
        if (httpURLConnection != null && param != null) {
            DataOutputStream printout = null;
            try {
                String stringParam = param.toString();
                Log.d("Parameter data:", stringParam);
                byte[] data = stringParam.getBytes("UTF-8");
                printout = new DataOutputStream(httpURLConnection.getOutputStream());
                printout.write(data);
                return true;
            } catch (Exception e) {
                return false;
            } finally {
                try {
                    if (printout != null) {
                        printout.flush();
                        printout.close();
                    }
                } catch (Exception e) {
                }
            }
        }
        return false;
    }

    public static void disconnectHttpURLConnection(HttpURLConnection httpConn) {
        if (httpConn != null) {
            httpConn.disconnect();
        }
    }

    public static TaskResult readStream(HttpURLConnection httpURLConnection) {

        InputStream inputStream = null;
        String line = null;
        String result = null;
        BufferedReader reader = null;

        try {

            if (httpURLConnection != null && httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                result = sb.toString();
                return new TaskResult(result);
            } else {
                return new TaskResult(new Exception(httpURLConnection.getResponseMessage()));
            }

        } catch (Exception e) {
            return new TaskResult(e);
        } finally {
            try {
                reader.close();
                inputStream.close();
            } catch (Exception e) {
            }
        }
    }
}