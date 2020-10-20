package com.az.pplayer.Base;

import android.annotation.SuppressLint;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import info.guardianproject.netcipher.NetCipher;

/**
 * Created by Javad on 2017-12-05 at 8:29 PM.
 */
public class SSLHelper {

    public static Document getDocUrl(String url) throws IOException {
        HttpsURLConnection netCipherconnection = NetCipher.getHttpsURLConnection(url);
        netCipherconnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Whale/2.8.107.17 Safari/537.36");
        netCipherconnection.connect();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(netCipherconnection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String stringHTML;
        while ((stringHTML = bufferedReader.readLine()) != null)
            stringBuilder.append(stringHTML);
        bufferedReader.close();
        return Jsoup.parse(String.valueOf(stringBuilder));
    }
}