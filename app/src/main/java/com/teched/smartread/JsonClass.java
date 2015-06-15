package com.teched.smartread;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class JsonClass {
    public static String getJSON(String address) {
        String parsedString = "";

        try {
            URL url = new URL(address.replace(" ", "%20"));
            URLConnection conn = url.openConnection();

            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            InputStream is = httpConn.getInputStream();
            parsedString = convertinputStreamToString(is);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return parsedString;
    }
    public static String convertinputStreamToString(InputStream ists)
            throws IOException {
        if (ists != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader r1 = new BufferedReader(new InputStreamReader(
                        ists, "UTF-8"));
                while ((line = r1.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                ists.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }
    public static void DownloadBook(Context context, String bookName) {
        try {
            PackageManager m = context.getPackageManager();
            String s = context.getPackageName();
            PackageInfo p = m.getPackageInfo(s, 0);
            s = p.applicationInfo.dataDir + "/app_book";

            URL url = new URL("http://php-smartread.rhcloud.com/books/" + bookName.replace(" ", "%20"));
            File file = new File(s,bookName);

            URLConnection ucon = url.openConnection();
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.close();
        } catch (Exception e) { e.printStackTrace();
        }
    }
}
