package com.azhen.crawler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CommentCrawler {
	public static void main(String[] args) {
		CommentCrawler crawler = new CommentCrawler();

	}
	public void login() {

	}

	public static void f() throws Exception{
		//URL url = new URL("http://weibo.com/ou2h1qing?is_hot=1");
		//URL url = new URL("http://www.baidu.com");
		//	URL url = new URL("http://weibo.com/login.php");
		URL url = new URL("http://weibo.com/login.php");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		//printConnectionInfo(connection);
		//connectionInfo(connection);
		int responseCode = connection.getResponseCode();
		if (200 == responseCode) {
			//InputStream inputStream = (InputStream)connection.getContent();
			InputStream inputStream = connection.getInputStream();
			byte[] buf = new byte[1024];
			int len;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ( (len = inputStream.read(buf)) != -1) {
				baos.write(buf, 0, len);

			}
			String content = new String(baos.toByteArray(),"utf-8");
			System.out.println(content);
		} else if (responseCode == 301) {
			String location = connection.getHeaderField("Location");
			System.out.println(location);

			url = new URL(location);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);

			responseCode = connection.getResponseCode();
			if (responseCode == 200) {
				printConnectionInfo(connection);
			}
		}
	}

	public static void printConnectionInfo(HttpURLConnection connection) {
		try {
			System.out.println(connection.getResponseCode());
			System.out.println(connection.getResponseMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("ContentLength: " + connection.getContentLength());
		System.out.println("ContentLengthLong: " + connection.getContentLengthLong());
		System.out.println("ContentEncoding: " + connection.getContentEncoding());
		System.out.println("ContentType: " + connection.getContentType());

	}
}
