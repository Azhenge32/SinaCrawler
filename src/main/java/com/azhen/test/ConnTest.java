package com.azhen.test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

public class ConnTest {
	/*public static void main(String args[]) throws Exception{
		String lianzaiUrl = "http://tieba.baidu.com/p/1243174814?pn=";
		String loginAction = "http://weibo.com";
		//ȡcookie
		String cookie =  getCookie("test","test",loginAction);
		if(!cookie.contains("USERID=")){
			System.out.println("��¼ʧ��");
			System.exit(1);
		}
		StringBuffer result = new StringBuffer();
		StringBuffer errorList = new StringBuffer();
		for(int i=1;i<=3;i++){
			String allUrl = getUrl(lianzaiUrl+i);
			String all[] = allUrl.split(";");
			for(int x = 0;x < all.length;x++){//�õ�ÿ�����ӵĵ�ַ
				String content = doRead(cookie, all[x]);
				if(null != content && !"".equals(content)){
					result.append(content);
				}else{
					errorList.append(all[x] + "\r\n");
				}
			}
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("F:\\����.txt")));
		BufferedWriter errorWriter = new BufferedWriter(new FileWriter(new File("F:\\errorList.txt")));
		writer.write(result.toString());
		writer.flush();
		writer.close();
		errorWriter.write(errorList.toString());
		errorWriter.flush();
		errorWriter.close();
	}*/

	public static String doRead(String cookie,String url) throws IOException {
		BufferedReader reader = null;
		String titleBegin = "<h1>";
		String titleEnd = "</h1>";
		String contentBegin = "class=\"d_post_content\">";
		String contentEnd = "</p>";
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setRequestProperty("Cookie",cookie);
		reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"gbk"));
		String line = "";
		StringBuffer resultBuffer = new StringBuffer();
		while((line = reader.readLine()) != null){
			resultBuffer.append(line);
		}
		String result = resultBuffer.toString();
		int titleBeginIndex = result.indexOf(titleBegin) + titleBegin.length();
		int titleEndIndex = result.indexOf(titleEnd);
		if(titleBeginIndex <0 || titleEndIndex <0){
			System.out.println("���Ӳ�����,url:"+url);
			return null;
		}
		String title = result.substring(titleBeginIndex,titleEndIndex);
		System.out.println("���ڶ�ȡ����:"+ title + "...");
		String content = title + "\r\n";
		while(result.contains(contentBegin)){
			int contentBeginIndex = result.indexOf(contentBegin) + contentBegin.length();
			result = result.substring(contentBeginIndex);
			int contentEndIndex = result.indexOf(contentEnd);
			content += result.substring(0,contentEndIndex);
			result = result.substring(contentEndIndex + contentEnd.length());
		}
		conn.disconnect();
		reader.close();
		content = content.replaceAll("<br>","\r\n");
		content = content.replaceAll("</br>","\r\n");
		content += "\r\n";
		return content;
	}

	/**
	 * ���һ�����������е����г�����
	 * @param lianzaiUrl
	 * @return
	 * @throws Exception
	 */
	public static String getUrl(String lianzaiUrl) throws Exception{
		URL url  = new URL(lianzaiUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"gbk"));
		String line = "";
		StringBuffer buffer = new StringBuffer();
		StringBuffer urlBuf = new StringBuffer();
		while((line = reader.readLine()) != null){
			buffer.append(line);
		}
		String result = buffer.toString();
		String contentBegin = "class=\"d_post_content\">";
		String contentEnd = "</p>";
		String urlBegin = "<a href=\"";
		String urlEnd = "\"";
		while(result.contains(contentBegin)){
			int contentStartIndex = result.indexOf(contentBegin) + contentBegin.length();
			result = result.substring(contentStartIndex);
			int contentEndIndex = result.indexOf(contentEnd);
			String content = result.substring(0,contentEndIndex);
			while (content.contains(urlBegin)){
				int urlBeginIndex = content.indexOf(urlBegin) + urlBegin.length();
				content = content.substring(urlBeginIndex);
				int urlEndIndex = content.indexOf(urlEnd);
				String href = content.substring(0,urlEndIndex).trim();
                /*http://tieba.baidu.com/p/1196506653
                http://tieba.baidu.com/p/1196506653?see_lz=1
                http://tieba.baidu.com/f?kz=1127473409
                http://tieba.baidu.com/p/1127473409?see_lz=1*/
				//http://tieba.baidu.com/f?kz=1127600193
				//http://tieba.baidu.com/p/1127600193?see_lz=1
				//��������תΪֻ��¥��ģʽ
				if(href.contains("http://tieba.baidu.com/f?")){
					String kz = href.substring("http://tieba.baidu.com/f?kz=".length());
					href = "http://tieba.baidu.com/p/" + kz.trim() + "?see_lz=1";
				}else{
					href += "?see_lz=1";
				}
				urlBuf.append(href + ";");
				content = content.substring(urlEndIndex + urlEnd.length());
			}
			result = result.substring(contentEndIndex + contentEnd.length());
		}
		reader.close();
		return urlBuf.toString();
	}




	private static void firstRequest(String url, Map<String, String> params, String encoding) throws Exception{
		StringBuilder sb = new StringBuilder();
		if(params!=null && !params.isEmpty()){
			for(Map.Entry<String, String> entry : params.entrySet()){
				sb.append(entry.getKey()).append('=');
				sb.append(URLEncoder.encode(entry.getValue(), encoding));
				sb.append('&');
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		byte[] entity = sb.toString().getBytes();
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setInstanceFollowRedirects(true);
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", String.valueOf(entity.length));
		OutputStream outStream = conn.getOutputStream();
		outStream.write(entity);
		//��ȡ�ض����ַ
		String redictURL= conn.getHeaderField( "Location" );

		System.out.println("��һ�������ض����ַ location="+redictURL);
		System.out.println("��һ������ conn.getResponseCode()="+conn.getResponseCode());

		//��ȡcookie
		Map<String,List<String>> map=conn.getHeaderFields();
		Set<String> set=map.keySet();
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			if (null != key && key.equals("Set-Cookie")) {
				System.out.println("key=" + key+",��ʼ��ȡcookie");
				List<String> list = map.get(key);
				StringBuilder builder = new StringBuilder();
				for (String str : list) {
					builder.append(str).toString();
				}
				String firstCookie=builder.toString();
				System.out.println("��һ�εõ���cookie="+firstCookie);
			}
		}

	}

	public static String getCookie(String loginAction) throws Exception{
		//��¼
		URL url = new URL(loginAction);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");

		//��ȡ�ض����ַ
		String redictURL= conn.getHeaderField( "Location" );
		System.out.println("��" + redirectTime + "�������ض����ַ location="+redictURL);
		System.out.println("��" + redirectTime + "һ������ conn.getResponseCode()="+conn.getResponseCode());
		String sessionId = "";
		String cookieVal = "";
		String key = null;
		//ȡcookie
		for(int i = 1; (key = conn.getHeaderFieldKey(i)) != null; i++){
			if(key.equalsIgnoreCase("set-cookie")){
				cookieVal = conn.getHeaderField(i);
				cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
				sessionId = sessionId + cookieVal + ";";
			}
		}
		System.out.println("��" + redirectTime + "������cookieΪ: " + sessionId);
		if (null != redictURL) {
			getCookie(redictURL);
		}
		return null;
	}

	private static int redirectTime  = 1;
	private static final String BAIDU_URL = "https://www.baidu.com/";
	private static final String WEIBO_URL = "http://weibo.com";

	public static void getCookieTest() throws Exception {
		String sessionId = getCookie(WEIBO_URL);
		System.out.println(sessionId);
	}
	public static void main(String[] args) throws Exception {
		getCookieTest();
	}
}
