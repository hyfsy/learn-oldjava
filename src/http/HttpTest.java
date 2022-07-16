package http;

import org.junit.Test;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.*;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author baB_hyf
 * @date 2022/04/03
 */
public class HttpTest {

    @Test
    public void testUseProxy() throws Exception {
        URL url = new URL("http://www.baidu.com");
        // 使用代理地址去请求
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 8080));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
    }

    @Test
    public void testUseJVMGlobalProxy() throws Exception {
        // HTTP 代理，只能代理 HTTP 请求
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "9876");

        // HTTPS 代理，只能代理 HTTPS 请求
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "9876");

        // 同时代理 HTTP/HTTPS 请求
        System.setProperty("proxyHost", "127.0.0.1");
        System.setProperty("proxyPort", "9876");

        // SOCKS 代理，支持 HTTP 和 HTTPS 请求
        // 注意：如果设置了 SOCKS 代理就不要设 HTTP/HTTPS 代理，HTTP/HTTPS 优先级高于 SOCKS
        System.setProperty("socksProxyHost", "127.0.0.1");
        System.setProperty("socksProxyPort", "1080");

        // request

        URL url = new URL("http://www.baidu.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    }

    @Test
    public void testJVMGlobalAuthentication() throws Exception {

        // JVM全局的代理认证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("username", "password".toCharArray());
            }
        };
        Authenticator.setDefault(authenticator);

        // request

        URL url = new URL("http://www.baidu.com");
        // proxy处进行认证
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 8080));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
    }

    @Test
    public void testSkipSSL() throws Exception {
        SSLContext context = SSLContext.getInstance("TLS");
        TrustManager[] trustManagers = {
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String authType) throws CertificateException {
                        // noop
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String authType) throws CertificateException {
                        // noop
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };

        SecureRandom secureRandom = SecureRandom.getInstanceStrong();
        context.init(null, trustManagers, secureRandom);

        SSLSocketFactory socketFactory = context.getSocketFactory();


        // request

        URL url = new URL("https://www.baidu.com");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(socketFactory);
    }

    @Test
    public void testStandardSSL() throws Exception {

        String ksPath = "E:\\test.ks";
        char[] password = "password".toCharArray();

        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (InputStream is = new FileInputStream(ksPath)) {
            trustStore.load(is, password);
        }

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);

        SecureRandom secureRandom = SecureRandom.getInstanceStrong();

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, trustManagerFactory.getTrustManagers(), secureRandom);

        SSLSocketFactory socketFactory = context.getSocketFactory();


        // request

        URL url = new URL("https://www.baidu.com");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(socketFactory);
    }

    @Test
    public void testPropertySSL() throws Exception {

        System.setProperty("javax.net.ssl.trustStore", "E:\\test.ks");
        System.setProperty("javax.net.ssl.trustStorePassword", "password");

        // request

        URL url = new URL("https://www.baidu.com");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
    }
}
