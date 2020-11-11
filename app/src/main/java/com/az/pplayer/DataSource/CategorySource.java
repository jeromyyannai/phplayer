package com.az.pplayer.DataSource;

import com.az.pplayer.Base.SSLHelper;
import com.az.pplayer.Models.CategoryItem;
import com.az.pplayer.Models.VideoItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import info.guardianproject.netcipher.NetCipher;

import com.az.pplayer.Base.SSLHelper;

public class CategorySource {
    public static List<CategoryItem> GetCategories(String url){
        url = "https://www.pornhub.com/categories";
        List<CategoryItem> cateories = new ArrayList<>();
        try {
            Document doc = SSLHelper.getDocUrl(url);
            Elements as = doc.select("li.catPic>div>a");

            for (Element el: as)
            {
                Element image = el.select("img").first();
                String imgUrl = image.attr("data-thumb_url");
                String id = el.parent().parent().attr("data-category");
                if (imgUrl =="")
                     imgUrl = image.attr("src");

                cateories.add(new CategoryItem(el.attr("data-mxptext"),
                        el.attr("href"),imgUrl, id
                        ));
            }


        }
        catch ( Exception e) {
            e.printStackTrace();
        }
        return cateories;
    }



}
