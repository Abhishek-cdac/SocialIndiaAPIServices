/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package com.pack.utilitypkg;
import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
/**
 *
 * @author MR
 */
public class ImageCompress {

    public static String toCompressImage(String lPimgSourcePath, String lPimgDisPath, String lPimgName, String lPimageFomat, String lPimageFor, int needWidth, int needHeight) {
        BufferedImage originalImage = null;
        BufferedImage resizeImageJpg = null;
        try {
        	Commonutility.crdDir(lPimgDisPath);
        	System.out.println("Image Compress Start Time : "+new SimpleDateFormat("yyyy-MM-dd - [hh:mm:ss]").format(new Date())+"");
            //originalImage = ImageIO.read(new File(lPimgSourcePath));    
        	originalImage = ImageOrientation.getBufferImgOrientation(lPimgSourcePath,lPimageFomat);
            int userimagetyp = originalImage.getType();
            int type = 0;         
           // System.out.println("userimagetyp : " + userimagetyp);
            //if (userimagetyp < tpe_byte_binary) {
               // type = tpe_byte_binary;
            //} else {
                type = userimagetyp;
           // }
           // System.out.println("final : " + type);
            int h = (int) (originalImage.getHeight());
            int w = (int) (originalImage.getWidth());
            if (needWidth == 0) {
                needWidth = w;
            }
            if (needHeight == 0) {
                needHeight = h;
            }
            Dimension newDmsn = null;
            if (lPimageFor != null && lPimageFor.equalsIgnoreCase("userprofileMobile")) {
                newDmsn = toGetNewImageSize(w, h, needWidth, needHeight);
            } else if (lPimageFor != null && lPimageFor.equalsIgnoreCase("adpostMobile")) {
                newDmsn = toGetNewImageSize(w, h, needWidth, needHeight);
            } else {
                newDmsn = toGetNewImageSize(w, h, needWidth, needHeight);
            }
            resizeImageJpg = resizeImage(originalImage, type, (int) newDmsn.getWidth(), (int) newDmsn.getHeight());
            ImageIO.write(resizeImageJpg, lPimageFomat, new File(lPimgDisPath + "/" + lPimgName + "." + lPimageFomat));
            System.out.println("Image Compress End Time : "+new SimpleDateFormat("yyyy-MM-dd - [hh:mm:ss]").format(new Date()));
            return "success";
        } catch (Exception ex) {
            System.out.println("Exception found in ImageCompress.java : " + ex);         
            return "Try again - Error";
        } finally {
            originalImage = null;
            resizeImageJpg = null;
        }

    }

    public static Dimension toGetNewImageSize(int lPorginalwidth, int lPorginalhieght, int lPNeedwidth, int lPNeedhieght) {
        Dimension imgSize = new Dimension(lPorginalwidth, lPorginalhieght);
        Dimension boundary = new Dimension(lPNeedwidth, lPNeedhieght);
        Dimension newi = ImageCompress.getScaledDimension(imgSize, boundary);
        return newi;
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int type, int pLwidth, int pLheight) {
        BufferedImage resizedImage = new BufferedImage(pLwidth, pLheight, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, pLwidth, pLheight, null);
        g.dispose();
        return resizedImage;
    }

    public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;
        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }
        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }
        System.out.println("New Width : " + new_width);
        System.out.println("New Height : " + new_height);
        // IMG_WIDTH = new_width;
        // IMG_HEIGHT = new_height;
        return new Dimension(new_width, new_height);
    }

    private static BufferedImage resizeImageWithHint(BufferedImage originalImage, int type) {
        int IMG_WIDTH = 180;
        int IMG_HEIGHT = 102;
        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
        g.dispose();
        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        return resizedImage;
    }

    public static int calculateInSampleSize(int reqWidths, int reqHeights) {
        int height = reqWidths;
        int width = reqHeights;
        int reqWidth = 180;
        int reqHeight = 102;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
                System.out.println("inSampleSize  " + inSampleSize);
                System.out.println("halfHeight  " + halfHeight);
                System.out.println("halfWidth  " + halfWidth);
            }
        }
        //System.out.println("return inSampleSize======== " + inSampleSize);
        return inSampleSize;
    }

    public static void main(String[] args) {
        Date n = new Date();
        String limgSourcePath = "E:/imageorignal/image_3092420140931173.jpeg";
        String limgDisPath = "E:/imagecompress/";
       // String limgName = "Desert_091520140245431" + n.getTime();
        String limgName = "image_3092420140931173";
        String limageFomat = "jpg";
        String limageFor = "all";
        int lneedWidth = 150;
        int lneedHeight = 130;
        toCompressImage(limgSourcePath, limgDisPath, limgName, limageFomat, limageFor, lneedWidth, lneedHeight);// 160, 130 is best for mobile
    }
}
