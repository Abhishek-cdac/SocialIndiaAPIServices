package com.pack.utilitypkg;

import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ShortInfoFrmReadUrl {

	public static String extUrlData(String Url, String FromWhere) {
        String RequestUrl = Url;
        StringBuilder rtnbf = null;
        rtnbf = new StringBuilder();
        URL url = null;
        Document doc = null;
        Elements d1 = null, d2 = null, d3 = null, d4 = null, d5 = null, d6 = null, d7 = null, d8 = null, d9 = null, d10 = null, d11 = null;
        if (RequestUrl != null && !RequestUrl.equalsIgnoreCase("") && !RequestUrl.equalsIgnoreCase("null")) {
            String URLImage = "", URLTitle = "", URLDesc = "", TopDomainName = "";
            try {
                if (RequestUrl.startsWith("www.")) {
                    RequestUrl = "http://" + RequestUrl;
                }
                doc = Jsoup.connect(RequestUrl).header("Accept-Charset", "utf-8").header("Accept-Language", "en-US").timeout(10 * 1000).get();
                try {
                    url = new URL(RequestUrl);//                        url.openConnection().setConnectTimeout(10000);
                    TopDomainName = url.getHost();
                } catch (Exception e) {
                    System.out.println("Exception found " + e);
                }
                if (TopDomainName != null && !TopDomainName.equalsIgnoreCase("") && !TopDomainName.equalsIgnoreCase("null")) {
                    TopDomainName = TopDomainName.toUpperCase();
                }//Getting Title
                URLTitle = doc.title();
                if (URLTitle.equalsIgnoreCase("")) {//facebook
                    d4 = doc.select("meta[property=og:title]");
                    URLTitle = d4.attr("content");
                }
                if (URLTitle.equalsIgnoreCase("")) {//twitter
                    d7 = doc.select("meta[property=twitter:title]");
                    URLTitle = d7.attr("content");
                }
                if (URLTitle.equalsIgnoreCase("")) {//Gplus
                    d10 = doc.select("meta[itemprop=name]");
                    URLTitle = d10.attr("content");
                }//Getting Description
                d1 = doc.select("meta[name=description]");
                URLDesc = d1.attr("content");
                if (URLDesc.equalsIgnoreCase("")) {//facebook
                    d5 = doc.select("meta[property=og:description]");
                    URLDesc = d5.attr("content");
                }
                if (URLDesc.equalsIgnoreCase("")) {//twitter
                    d8 = doc.select("meta[property=twitter:description]");
                    URLDesc = d8.attr("content");
                }
                if (URLDesc.equalsIgnoreCase("")) {//Gplus
                    d11 = doc.select("meta[itemprop=description]");
                    URLDesc = d11.attr("content");
                }
                if (URLDesc.equalsIgnoreCase("")) {//if there is no description content fetched from first paragraph tag
                    d11 = doc.select("p");
                    URLDesc = d11.text();
                }
                if (!URLDesc.equalsIgnoreCase("")) {
                    if (URLDesc.length() > 400) {
                        URLDesc = URLDesc.substring(0, 395);
                        URLDesc = URLDesc + "...";
                    }
                }
                //Getting ImageURL
                d2 = doc.select("link[rel=image_src]");
                URLImage = d2.attr("href");
                if (URLImage.equalsIgnoreCase("")) {//facebook
                    d3 = doc.select("meta[property=og:image]");URLImage = d3.attr("content");
                }
                if (URLImage.equalsIgnoreCase("")) {//twitter
                    d6 = doc.select("meta[property=twitter:image]");URLImage = d6.attr("content");
                }
                if (URLImage.equalsIgnoreCase("")) {//Gplus
                    d9 = doc.select("meta[itemprop=image]"); URLImage = d9.attr("content");
                }
                if (URLImage.equalsIgnoreCase("")) {//parse html line by line to get image tag::::src starts with http or https
                    for (Element e : doc.select("img")) {
                        URLImage = e.attr("src");
                        if ((URLImage.startsWith("http://") || URLImage.startsWith("https://")) && !URLImage.endsWith(".gif")) {
                            break;
                        } else {
                            URLImage = "";
                        }
                    }
                }
                if (URLImage.equalsIgnoreCase("")) {//parse html to get any image tag::src to append
                    d9 = doc.select("img");
                    URLImage = d9.attr("src");
                }
                if (!URLImage.equalsIgnoreCase("")) {
                    if (URLImage.startsWith("http://") || URLImage.startsWith("https://")) {
                    } else {
                        if (URLImage.startsWith("/")) {
                            URLImage = "http://" + TopDomainName + URLImage;
                        } else {
                            URLImage = "http://" + TopDomainName + "/" + URLImage;
                        }
                        boolean imageurlvalidity = ShortInfoFrmReadUrl.validImageUrl(URLImage);//checked for the ssl certificate http or https
                        if (!imageurlvalidity) {
                            URLImage = URLImage.replaceAll("http://", "https://");
                        }
                    }
                }
                boolean imageurlvalidity = ShortInfoFrmReadUrl.validImageUrl(URLImage);
                if (!imageurlvalidity) {
                    URLImage = "";
                }
                if (URLImage.equalsIgnoreCase("") && URLDesc.equalsIgnoreCase("") && URLTitle.equalsIgnoreCase("")) {//ReturnData += "error";
                    rtnbf.append("error");
                } else {
                    if (!URLImage.equalsIgnoreCase("")) {
                        if (FromWhere.equalsIgnoreCase("view")) {
                            // ReturnData += "<div class='left' style='padding:5px;'>";
                            rtnbf.append("<div class='left' style='padding:5px;'>");
                        } else {
                            //ReturnData += "<div class='left' style='padding:5px;'>";
                            rtnbf.append("<div class='left' style='padding:5px;'>");
                        }
                        rtnbf.append("<a href='" + RequestUrl + "' target='_blank'>");//ReturnData += "<a href='" + RequestUrl + "' target='_blank'>";
                        rtnbf.append("<table style='cursor:pointer;' align='center'>");
                        rtnbf.append("<tbody>");
                        rtnbf.append("<tr>");
                        rtnbf.append("<td>");
                        // ReturnData += "<table style='cursor:pointer;' align='center'>";
                        //  ReturnData += "<tbody>";
                        //  ReturnData += "<tr>";
                        //  ReturnData += "<td>";
                        if (FromWhere.equalsIgnoreCase("view")) {
                            rtnbf.append("<img src='" + URLImage + "' style='max-width:150px;max-height:150px;' />");
                            //ReturnData += "<img src='" + URLImage + "' style='max-width:150px;max-height:150px;' />";
                        } else {
                            //ReturnData += "<img src='" + URLImage + "' style='max-width:120px;max-height:120px;' />";
                            rtnbf.append("<img src='" + URLImage + "' style='max-width:120px;max-height:120px;' />");
                        }
                        rtnbf.append("</td>");
                        rtnbf.append("</tr>");
                        rtnbf.append("</tbody>");
                        rtnbf.append("</table>");
                        rtnbf.append("</a>");
                        rtnbf.append("</div>");
                    }
                    if (!URLImage.equalsIgnoreCase("")) {
                        if (FromWhere.equalsIgnoreCase("view")) {
                            //ReturnData += "<div class='left' style='width:auto;height:auto;padding:5px;max-width:335px;min-height:120px;'>";
                            rtnbf.append("<div class='left' style='width:auto;height:auto;padding:5px;max-width:335px;min-height:120px;'>");
                        } else {
                            //ReturnData += "<div class='left' style='width:auto;height:auto;padding:5px;max-width:285px;min-height:120px;'>";
                            rtnbf.append("<div class='left' style='width:auto;height:auto;padding:5px;max-width:285px;min-height:120px;'>");
                        }
                    } else {
                        if (FromWhere.equalsIgnoreCase("view")) {
                            //ReturnData += "<div class='left' style='width:auto;height:auto;padding:5px;max-width:500px;'>";
                            rtnbf.append("<div class='left' style='width:auto;height:auto;padding:5px;max-width:500px;'>");
                        } else {
                            //ReturnData += "<div class='left' style='width:auto;height:auto;padding:5px;max-width:415px;'>";
                            rtnbf.append("<div class='left' style='width:auto;height:auto;padding:5px;max-width:415px;'>");
                        }
                    }
                    rtnbf.append("<div class='left' style='margin-left:5px;'>");
                    rtnbf.append("<div class='left wordwrap pointer' id='pagetitle' style='font-weight:bold;font-size:18px;max-width:420px;'><a href='" + RequestUrl + "' target='_blank' style='text-decoration:none;color:#141823;'>" + URLTitle + "</a></div>");
                    rtnbf.append("<div class='clear height5'></div>");
                    rtnbf.append("<div class='left wordwrap pointer' id='pagedesc' style='font-size:12px;max-width:420px;'><a href='" + RequestUrl + "' target='_blank' style='text-decoration:none;color:#141823;'>" + URLDesc + "</a></div>");
                    rtnbf.append("<div class='clear height5'></div>");
                    rtnbf.append("<div class='left wordwrap pointer' id='pagedesc' style='font-size:11px;color:#9197a3;'><a href='" + RequestUrl + "' target='_blank' style='text-decoration:none;color:#141823;'>" + TopDomainName + "</a></div>");
                    rtnbf.append("</div>");
                    rtnbf.append("</div>");
//                    ReturnData += "<div class='left' style='margin-left:5px;'>";
//                    ReturnData += "<div class='left wordwrap pointer' id='pagetitle' style='font-weight:bold;font-size:18px;max-width:420px;'><a href='" + RequestUrl + "' target='_blank' style='text-decoration:none;color:#141823;'>" + URLTitle + "</a></div>";
//                    ReturnData += "<div class='clear height5'></div>";
//                    ReturnData += "<div class='left wordwrap pointer' id='pagedesc' style='font-size:12px;max-width:420px;'><a href='" + RequestUrl + "' target='_blank' style='text-decoration:none;color:#141823;'>" + URLDesc + "</a></div>";
//                    ReturnData += "<div class='clear height5'></div>";
//                    ReturnData += "<div class='left wordwrap pointer' id='pagedesc' style='font-size:11px;color:#9197a3;'><a href='" + RequestUrl + "' target='_blank' style='text-decoration:none;color:#141823;'>" + TopDomainName + "</a></div>";
//                    ReturnData += "</div>";
//                    ReturnData += "</div>";
                    if (!FromWhere.equalsIgnoreCase("view")) {
//                        ReturnData += "<div id='close_urlview' class='dialog_close_new pointer right' style='margin:2px 2px 0px 0px;width:18px;display:none;' onclick='closeExtLnkData();'></div>";
//                        ReturnData += "<input id='feedappendurl' name='feedappendurl' value='" + RequestUrl + "' type='hidden'/>";
                        rtnbf.append("<div id='close_urlview' class='dialog_close_new pointer right' style='margin:2px 2px 0px 0px;width:18px;display:none;' onclick='closeExtLnkData();'></div>");
                        rtnbf.append("<input id='feedappendurl' name='feedappendurl' value='" + RequestUrl + "' type='hidden'/>");
                    }
                }
//                    System.out.println("URLImage:>>>>" + URLImage);
//                    System.out.println("URLDesc:>>>>" + URLDesc);
//                    System.out.println("URLTitle:>>>>" + URLTitle);
            } catch (Exception e) {
                // ReturnData += "error";
                rtnbf.append("error");
                System.out.println("Exception found in UrlParse: " + e);                
            } finally {
                if (url != null) {
                    url = null;
                }
                if (d1 != null) {
                    d1 = null;
                }
                if (d2 != null) {
                    d2 = null;
                }
                if (d3 != null) {
                    d3 = null;
                }
                if (d4 != null) {
                    d4 = null;
                }
                if (d5 != null) {
                    d5 = null;
                }
                if (d6 != null) {
                    d6 = null;
                }
                if (d7 != null) {
                    d7 = null;
                }
                if (d8 != null) {
                    d8 = null;
                }
                if (d9 != null) {
                    d9 = null;
                }
                if (d10 != null) {
                    d10 = null;
                }
                if (d11 != null) {
                    d11 = null;
                }
                if (doc != null) {
                    doc = null;
                }
            }
        }
        return rtnbf.toString();
    }
	
	public static boolean validImageUrl(String URLName) {
        HttpURLConnection con = null;
        try {
            HttpURLConnection.setFollowRedirects(false);
            con = (HttpURLConnection) new URL(URLName).openConnection();
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            System.out.println("Exception in validimageurl:" + e);
            return false;
        } finally {
            if (con != null) {
                con = null;
            }
        }
    }
	
	public static void main(String rrr[]){
		String pg=ShortInfoFrmReadUrl.extUrlData("http://www.hcl.com","");
		System.out.println(pg);
	}
}
