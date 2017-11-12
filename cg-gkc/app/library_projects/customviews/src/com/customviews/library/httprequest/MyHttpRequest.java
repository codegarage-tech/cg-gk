package com.customviews.library.httprequest;//package com.customviews.library.httprequest;
//
//import java.io.BufferedReader;
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.SocketException;
//import java.net.SocketTimeoutException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpVersion;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.conn.ConnectTimeoutException;
//import org.apache.http.conn.scheme.PlainSocketFactory;
//import org.apache.http.conn.scheme.Scheme;
//import org.apache.http.conn.scheme.SchemeRegistry;
//import org.apache.http.conn.ssl.SSLSocketFactory;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.params.BasicHttpParams;
//import org.apache.http.params.HttpParams;
//import org.apache.http.params.HttpProtocolParams;
//import org.apache.http.protocol.HTTP;
//import org.json.JSONException;
//
//import android.app.Activity;
//import android.util.Log;
//import com.customviews.library.model.TaskParameter;
//
//
///**
// * HttpRequest This class handles POST and GET requests and enables you to
// * upload files via post. Cookies are stored in the HttpClient.
// *
// * @author Sander Borgman
// * @url http://www.sanderborgman.nl
// * @version 1
// */
//public class MyHttpRequest {
//
//	/*
//	 *
//	 * do log in
//	 */
//
//	public static String currentUserName = "";
//	public static String currentPass = "";
//	public static String currentVersion = "";
//	public static String currentURL = "";
//
//	public static void resetAllUserData() {
//
//		MyHttpRequest.currentUserName = "";
//		MyHttpRequest.currentPass = "";
//		MyHttpRequest.currentVersion = "";
//		MyHttpRequest.currentURL = "";
//
//	}
//
//	public static String doLogin(String url, final String username,
//			final String pass) {
//
//		MyHttpRequest.currentUserName = username;
//		MyHttpRequest.currentPass = pass;
//
//		final HttpPost httpost = new HttpPost(url);
//
//		Log.d("Log in URL is ", url);
//		MyHttpRequest.currentURL = url;
//
//		final List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//		nvps.add(new BasicNameValuePair("email", username));
//		nvps.add(new BasicNameValuePair("password", pass));
//
//		try {
//			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
//
//			return MyHttpRequest.getData(httpost);
//
//		} catch (final UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		catch (final IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (final URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return "error";
//
//	}
//
//	public static String doPostRequest(String url,
//			ArrayList<NameValuePair> myParameter) {
//
//		final HttpPost httpost = new HttpPost(url);
//
//		try {
//			httpost.setEntity(new UrlEncodedFormEntity(myParameter, HTTP.UTF_8));
//
//			return MyHttpRequest.getData(httpost);
//
//		} catch (final UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		catch (final IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (final URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return "error";
//
//	}
//
//	public static String doDynamicPostRequest(String url,
//			ArrayList<TaskParameter> myParameter) throws NumberFormatException,
//			JSONException, URISyntaxException, ClientProtocolException,
//			IOException, SocketException, ConnectTimeoutException,
//			SocketTimeoutException, UnsupportedEncodingException,
//			MalformedURLException {
//
//		ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
//		for (int i = 0; i < myParameter.size(); i++) {
//			parameters.add(new BasicNameValuePair(myParameter.get(i).getKey().toString(),
//					myParameter.get(i).getValue().toString()));
//
//		}
//		final HttpPost httpost = new HttpPost(url);
//		// try {
//		httpost.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
//
//		return MyHttpRequest.getData(httpost);
//
//	}
//
//	public static String doFileUpload(final Activity activity,
//			String uploadURL, String filePathInSDCard,
//			String parameterNameOfFile, ArrayList<TaskParameter> otherParameters)
//			throws NumberFormatException, JSONException, URISyntaxException,
//			ClientProtocolException, IOException, SocketException,
//			ConnectTimeoutException, SocketTimeoutException,
//			UnsupportedEncodingException, MalformedURLException {
//
//		String ServerRespone = "";
//		int serverResponseCode = 0;
//
//		final String upLoadServerUrl = uploadURL;
//		final String fileName = filePathInSDCard;
//
//		ArrayList<TaskParameter> myParameter = new ArrayList<TaskParameter>();
//		myParameter = otherParameters;
//
//		// Checking file
//		File sourceFile = new File(fileName);
//		Log.w("file name are...", "" + sourceFile);
//
//		if (!sourceFile.isFile()) {
//			Log.e("uploadFile", "Source File Does not exist");
//		}
//
//		// server request config
//		HttpURLConnection conn = null;
//		DataOutputStream dos = null;
//		String lineEnd = "\r\n";
//		String twoHyphens = "--";
//		String boundary = "*****";
//		int bytesRead, bytesAvailable, bufferSize;
//		byte[] buffer;
//		int maxBufferSize = 1 * 1024 * 1024;
//
//		FileInputStream fileInputStream = new FileInputStream(sourceFile);
//		URL url = new URL(upLoadServerUrl);
//		conn = (HttpURLConnection) url.openConnection();
//
//		// / Allow for mathod for inout and output
//		conn.setDoInput(true); // Allow Inputs
//		conn.setDoOutput(true); // Allow Outputs
//		conn.setUseCaches(false); // Don't use a Cached Copy
//
//		// Enable for post mathod
//		conn.setRequestMethod("POST");
//		conn.setRequestProperty("Connection", "Keep-Alive");
//		conn.setRequestProperty("ENCTYPE", "multipart/form-data");
//		conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="
//				+ boundary);
//		conn.setRequestProperty("uploaded_file", fileName);
//		dos = new DataOutputStream(conn.getOutputStream());
//		dos.writeBytes(twoHyphens + boundary + lineEnd);
//
//		// for other parameter
//		if (myParameter.size() > 0) {
//
//			for (int i = 0; i < myParameter.size(); i++) {
//
//				dos.writeBytes("Content-Disposition: form-data; name=\""
//						+ myParameter.get(i).getKey().toString() + "\"" + lineEnd);
//				dos.writeBytes(lineEnd);
//				dos.writeBytes(myParameter.get(i).getValue().toString());
//				dos.writeBytes(lineEnd);
//				dos.writeBytes(twoHyphens + boundary + lineEnd);
//			}
//		}
//		// for file
//		dos.writeBytes("Content-Disposition: form-data; name=\""
//				+ parameterNameOfFile + "\";filename=\"" + fileName + "\""
//				+ lineEnd);
//		dos.writeBytes(lineEnd);
//		bytesAvailable = fileInputStream.available(); // create a
//														// buffer of
//														// maximum
//														// size
//
//		bufferSize = Math.min(bytesAvailable, maxBufferSize);
//		buffer = new byte[bufferSize];
//
//		// read file and write it into form...
//		bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//
//		while (bytesRead > 0) {
//			dos.write(buffer, 0, bufferSize);
//			bytesAvailable = fileInputStream.available();
//			bufferSize = Math.min(bytesAvailable, maxBufferSize);
//			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//		}
//
//		// send multipart form data necesssary after file data...
//		dos.writeBytes(lineEnd);
//		dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//
//		// Responses from the server (code and message)
//		serverResponseCode = conn.getResponseCode();
//		final String serverResponseMessage = conn.getResponseMessage();
//
//		InputStream servere = conn.getInputStream();
//		ServerRespone = MyHttpRequest.GetText(servere);
//
//		Log.i("uploadFile", "HTTP Response code is : " + serverResponseCode);
//		Log.i("uploadFile", "HTTP Response message is : "
//				+ serverResponseMessage);
//		Log.i("uploadFile", "HTTP Response return is : " + ServerRespone);
//
//		if (serverResponseCode == 200) {
//			activity.runOnUiThread(new Runnable() {
//				@Override
//				public void run() {
//
//				}
//			});
//		}
//
//		// close the streams //
//		fileInputStream.close();
//		dos.flush();
//		dos.close();
//
//		return ServerRespone;
//	}
//
//	/*
//	 * get data as string
//	 */
//
//	public static String getData(final HttpPost httpost) throws IOException,
//			URISyntaxException {
//
//		String inputLine = "Error";
//		final StringBuffer buf = new StringBuffer();
//
//		{
//
//			InputStream ins = null;
//
//			ins = MyHttpRequest.getUrlData(httpost);
//
//			final InputStreamReader isr = new InputStreamReader(ins);
//			final BufferedReader in = new BufferedReader(isr);
//
//			while ((inputLine = in.readLine()) != null) {
//				buf.append(inputLine);
//			}
//
//			in.close();
//
//		}
//
//		return buf.toString();
//
//	}
//
//	/*
//	 * get input stream
//	 */
//	public static InputStream getUrlData(final HttpPost httpost)
//			throws URISyntaxException, ClientProtocolException, IOException {
//
//		final HttpClient client = MyHttpRequest.getClient();
//
//		final HttpResponse res = client.execute(httpost);
//
//		System.out.println("post response for  register: "
//				+ res.getStatusLine());
//
//		return res.getEntity().getContent();
//	}
//
//	/*
//	 * get https client
//	 */
//	public static DefaultHttpClient getClient() {
//		DefaultHttpClient ret = null;
//		// sets up parameters
//		final HttpParams params = new BasicHttpParams();
//
//		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
//		HttpProtocolParams.setContentCharset(params, "utf-8");
//		params.setBooleanParameter("http.protocol.expect-continue", false);
//		final SchemeRegistry registry = new SchemeRegistry();
//		registry.register(new Scheme("http", PlainSocketFactory
//				.getSocketFactory(), 80));
//		final SSLSocketFactory sslSocketFactory = SSLSocketFactory
//				.getSocketFactory();
//		sslSocketFactory
//				.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//		registry.register(new Scheme("https", sslSocketFactory, 443));
//		final ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(
//				params, registry);
//		ret = new DefaultHttpClient(manager, params);
//		return ret;
//
//	}
//
//	/*
//	 * get inputstream for get request
//	 */
//
//	public static InputStream getInputStreamForGetRequest(final String url)
//			throws URISyntaxException, ClientProtocolException, IOException {
//
//		Log.w("URL is ", url);
//		final DefaultHttpClient httpClient = MyHttpRequest.getClient();
//		URI uri;
//		InputStream data = null;
//
//		uri = new URI(url);
//		final HttpGet method = new HttpGet(uri);
//
//		final HttpResponse res = httpClient.execute(method);
//
//		System.out.println("Login form get: " + res.getStatusLine());
//
//		System.out.println("get login cookies:");
//		httpClient.getCookieStore().getCookies();
//
//		final String code = res.getStatusLine().toString();
//		Log.w("status line ", code);
//		data = res.getEntity().getContent();
//
//		return data;
//	}
//
//	public static long getInputStreamForRequestSize(final String url)
//			throws URISyntaxException, ClientProtocolException, IOException {
//
//		Log.w("URL is ", url);
//		final DefaultHttpClient httpClient = MyHttpRequest.getClient();
//		URI uri;
//		InputStream data = null;
//
//		uri = new URI(url);
//		final HttpGet method = new HttpGet(uri);
//
//		final HttpResponse res = httpClient.execute(method);
//
//		System.out.println("Login form get: " + res.getStatusLine());
//
//		System.out.println("get login cookies:");
//		httpClient.getCookieStore().getCookies();
//
//		final String code = res.getStatusLine().toString();
//		Log.w("status line ", code);
//		data = res.getEntity().getContent();
//
//		return res.getEntity().getContentLength();
//	}
//
//	/**
//	 * Get string from stream
//	 *
//	 * @param InputStream
//	 * @return
//	 * @throws java.io.IOException
//	 */
//	public static String GetText(final InputStream in) throws IOException {
//		String text = "";
//		final BufferedReader reader = new BufferedReader(new InputStreamReader(
//				in));
//		final StringBuilder sb = new StringBuilder();
//		String line = null;
//		try {
//			while ((line = reader.readLine()) != null) {
//				sb.append(line + "\n");
//			}
//			text = sb.toString();
//
//		} finally {
//
//			in.close();
//
//		}
//		return text;
//	}
//
//	public static String doRegister(String url, final String firstName,
//			String lastName, String email, final String pass) {
//
//		final HttpPost httpost = new HttpPost(url);
//
//		Log.d("Log in URL is ", url);
//		MyHttpRequest.currentURL = url;
//
//		final List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//		nvps.add(new BasicNameValuePair("email", email));
//		nvps.add(new BasicNameValuePair("password", pass));
//		nvps.add(new BasicNameValuePair("firstname", firstName));
//		nvps.add(new BasicNameValuePair("lastname", lastName));
//
//		try {
//			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
//
//			return MyHttpRequest.getData(httpost);
//
//		} catch (final UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		catch (final IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (final URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return "failed";
//
//	}
//
//}