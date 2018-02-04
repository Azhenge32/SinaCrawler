package com.azhen.test;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

import java.net.URI;

public class Login {
    public static void main(String[] args) throws Exception {
        ssoLogin();
    }
    public static void ssoLogin() throws Exception {
        StringBuilder cookie = new StringBuilder();
        cookie.append("U_TRS1=").append("000000e6.80a7f6f.5a3a8882.3346ec92;")
                .append("UOR=").append("www.baidu.com,mil.news.sina.com.cn,;")
                .append("ULV=").append("1514110057072:1:1:1::;")
                .append("lxlrttp=").append("1516344969;")
                .append("SINAGLOBAL=").append("10.71.2.95_1516800470.415031;")
                .append("SCF=").append("Ahn37KWtGzmSGGvRvPHMaUkjeYISY_AIk0aRFNybJ4W2SivKcpwoBHIRAfiW6o2B-VOCceiHeqrDsgL8pHBHMks.;")
                .append("Apache=").append("10.71.2.95_1517753041.826081;")
                .append("ULOGIN_IMG=").append("gz-d561e484a81af56050ce3dd30f8bc3010721;")
                .append("SUB=").append("2AkMtK5KXdcNxrAZVnPEVyW7rbI1H-jye_vthAn7tJhMyAhgv7l9RqSVutBF-XD6uXNx3XqzXc6kMyYpuSZJ9scm9;")
                .append("SUBP=").append("0033WrSXqPxfM72wWs9jqgMF55529P9D9WhI61Pywd3OFZ0DK-KyXKSQ5JpV8J8AeKnReo-pehepSKx5McLVqC4odcXt");

        StringBuilder requestBody = new StringBuilder();
        requestBody.append("entry=").append("weibo").append("&")
                .append("gateway=").append("1").append("&")
                .append("savestate=").append("7").append("&")
                 .append("qrcode_flag=").append("false").append("&")
                .append("useticket=").append("1").append("&")
               .append("pagerefer=").append("https%3A%2F%2Flogin.sina.com.cn%2Fcrossdomain2.php%3Faction%3Dlogout%26r%3Dhttps%253A%252F%252Fweibo.com%252Flogout.php%253Fbackurl%253D%25252F&").append("&")
                .append("vsnf=").append("1").append("&")
                .append("servertime=").append("1517755835").append("&")
                .append("nonce=").append("2DCXIA").append("&")
                    .append("pwencode=").append("rsa2").append("&")
                       .append("rsakv=").append("1330428213").append("&")
                         .append("sp=").append("b21ecf3e8c8dc196d92b54647a42c5a8243b25d89129dad46d48c2bcd5c2638d590b006b961a124e4f0efa717459a39dbc3412b4a810132a09acc4d62b4a85b55e16a4c8e89093ef7c12e36e4cd321169b5c303c3cf793acb95a7eafa457c5da0cd9061eb45ef8d750d74fbec67ad89d87b597d28f5f0e82af71050b42892112").append("&")
                         .append("sr=").append("1920*1080").append("&")
                         .append("encoding=").append("UTF-8").append("&")
                         .append("prelt=").append("431").append("&")
                .append("url=").append("https%3A%2F%2Fweibo.com%2Fajaxlogin.php%3Fframelogin%3D1%26callback%3Dparent.sinaSSOController.feedBackUrlCallBack&").append("&")
                .append("returntype=").append("META");

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(
                HttpRequest
                        .newBuilder(new URI("https://login.sina.com.cn/sso/login.php"))
                        .header("Host", "login.sina.com.cn")
                        .header("Connection", "keep-alive")
                        .header("Content-Length", "782")
                        .header("Cache-Control", "max-age=0")
                        .header("Origin", "https://weibo.com")
                        .header("Upgrade-Insecure-Requests", "1")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                        .header("Referer", "https://weibo.com/")
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .header("Accept-Language", "zh-CN,zh;q=0.9")
                        .header("Cookie", cookie.toString())
                        .POST(HttpRequest.BodyProcessor.fromString(requestBody.toString()))
                        .build(),
                HttpResponse.BodyHandler.asString()
        );
        int statusCode = response.statusCode();
        String body = response.body();
        System.out.println(body);
    }
}
