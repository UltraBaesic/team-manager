/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engine;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 *
 * @author ndfmac
 */
public class FilePathManager {

    private static FilePathManager instance = null;
    private String RoothDirPath,
            ShopProfilePicture, ManagerProfilePicture, PortalProfilePicture, 
            ShopProductImage, ManagerProductImage, PortalProductImage,
            ManagerUnlistedProductImage, PortalUnlistedProductImage, 
            ManagerCategoryImage, ShopCategoryImage,
            pdfAttachmentsManager, pdfAttachmentsPortal, TMResources;

    private String WMManagerPath = "WMManager/";
    private String WMPortalPath = "WMPortal/";
    private String WMShopPath = "WMShop/";
    private String TeamManager = "TeamManager/";

//    static String pdfAttachmentsManager = "/Users/ndfmac/Dropbox/Current/NDF/Web/WMManager/web/assets/images/PDFDocuments/";
//    static String pdfAttachmentsPortal = "/Users/ndfmac/Dropbox/Current/NDF/Web/WMPortal/web/assets/images/PDFDocuments/";
    private FilePathManager() throws UnsupportedEncodingException {
        File myClass = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getFile());

        //The package name give the number of parentFile to apply
        //We add 3 : the first one is the .class name, the "." counted are one less, and the package is
        //inside a classes folder. So 3.
        int packageSubFolder = getClass().getPackage().getName().replaceAll("[^.]", "").length() + 6;

        //Place the file to the good parent file to point into the web inf
        for (int i = 0; i < packageSubFolder; i++) {
            myClass = myClass.getParentFile();
        }

        this.RoothDirPath = myClass.getAbsolutePath().replaceAll("%20", " ") + File.separator;

        ShopProductImage = this.getWMShopAppPath() + "web/assets/images/ProductImages/";
        ShopProfilePicture = this.getWMShopAppPath() + "web/assets/images/ProfilePicture/";
        ShopCategoryImage = this.getWMShopAppPath() + "web/assets/images/CategoryImages/";

        ManagerProfilePicture = this.getWMManagerAppPath() + "web/assets/images/ProfilePicture/";
        ManagerUnlistedProductImage = this.getWMManagerAppPath() + "web/assets/images/UnlistedProductImages/";
        pdfAttachmentsManager = this.getWMManagerAppPath() + "web/assets/images/PDFDocuments/";
        ManagerProductImage = this.getWMManagerAppPath() + "web/assets/images/ProductImages/";
        ManagerCategoryImage = this.getWMManagerAppPath() + "web/assets/images/CategoryImages/";

        pdfAttachmentsPortal = this.getWMPortalAppPath() + "web/assets/images/PDFDocuments/";
        PortalProfilePicture = this.getWMPortalAppPath() + "web/assets/images/ProfilePicture/";
        PortalProductImage = this.getWMPortalAppPath() + "web/assets/images/ProductImages/";
        PortalUnlistedProductImage = this.getWMPortalAppPath() + "web/assets/images/UnlistedProductImages/";
        
        TMResources = this.getTeamManagerAppPath() + "web/resources/";
    }

    public static FilePathManager getInstance() throws UnsupportedEncodingException {
        if (instance == null) {
            instance = new FilePathManager();
        }
        return instance;
    }

    public String getRoothDirPath() {
        return this.RoothDirPath;
    }

    ///Path to WMManagerRoot
    public String getWMManagerAppPath() {
        return this.getRoothDirPath() + WMManagerPath;
    }

    ///Path to WMPortal Root
    public String getWMPortalAppPath() {
        return this.getRoothDirPath() + WMPortalPath;
    }
    
    ///Path to WMShop Root
    public String getWMShopAppPath() {
        return this.getRoothDirPath() + WMShopPath;
    }
    
    ///Path to TeamManager Root
    public String getTeamManagerAppPath() {
        return this.getRoothDirPath() + TeamManager;
    }

    ///Path to WMManager Unlisted Product Image Folder
    public String getManagerUnlistedProductImageAddress() {
        return ManagerUnlistedProductImage;
    }

    ///Path to WMPortal Unlisted Product Image Folder
    public String getPortalUnlistedProductImageAddress() {
        return PortalUnlistedProductImage;
    }

    ///Path to WMManager Product Image Folder
    public String getManagerProductImageAddress() {
        return ManagerProductImage;
    }

    ///Path to WMPortal Product Image Folder
    public String getPortalProductImageAddress() {
        return PortalProductImage;
    }

    ///Path to WMShop Product Image Folder
    public String getShopProductImageAddress() {
        return ShopProductImage;
    }

    ///Path to WMManager Profile Picture Folder
    public String getManagerProfilePictureAddress() {
        return ManagerProfilePicture;
    }

    ///Path to WMPortal Profile Picture Folder
    public String getPortalProfilePictureAddress() {
        return PortalProfilePicture;
    }

    ///Path to WMShop Profile Picture Folder
    public String getShopProfilePictureAddress() {
        return ShopProfilePicture;
    }

    ///Path to WMManager PDF Attachment Folder
    public String pdfAttachmentsManagerAddress() {
        return pdfAttachmentsManager;
    }

    ///Path to WMPortal PDF Attachment Folder
    public String pdfAttachmentsPortalAddress() {
        return pdfAttachmentsPortal;
    }

 ///Path to WMPortal Profile Picture Folder
    public String getManagerCategoryImageAddress() {
        return ManagerCategoryImage;
    }

    ///Path to WMShop Profile Picture Folder
    public String getShopCategoryImageAddress() {
        return ShopCategoryImage;
    }
    
    ///Path to TeamManager Resources Folder
    public String getTeamManagerResourcesAddress() {
        return TMResources;
    }
    
    public static void deleteUnlistedProductImageFile(String filename, String filepath) throws IOException {
        String address;
        if (filepath.contains("WMManager")) {
            address = FilePathManager.getInstance().getManagerUnlistedProductImageAddress();
        } else {
            address = FilePathManager.getInstance().getPortalUnlistedProductImageAddress();
        }

        String dest = address + filename;
        try {
            String tempFile = dest;
            //Delete if tempFile exists
            File fileTemp = new File(tempFile);
            if (fileTemp.exists()) {
                fileTemp.delete();
            }
        } catch (Exception e) {
            String error = e.getMessage();
            e.printStackTrace();
        }
    }
    public static void deleteCategoryImageFile(String filename, String filepath) throws IOException {
        String address;
        if (filepath.contains("WMManager")) {
            address = FilePathManager.getInstance().getManagerCategoryImageAddress();
        } else {
            address = FilePathManager.getInstance().getShopCategoryImageAddress();
        }

        String dest = address + filename;
        try {
            String tempFile = dest;
            //Delete if tempFile exists
            File fileTemp = new File(tempFile);
            if (fileTemp.exists()) {
                fileTemp.delete();
            }
        } catch (Exception e) {
            String error = e.getMessage();
            e.printStackTrace();
        }
    }

    public static void deleteProfilePictureFile(String filename, String filepath) throws IOException {
        String address;
        if (filepath.contains("WMManager")) {
            address = FilePathManager.getInstance().getManagerProfilePictureAddress();
        } else if (filepath.contains("WMPortal")) {
            address = FilePathManager.getInstance().getPortalProfilePictureAddress();
        } else {
            address = FilePathManager.getInstance().getShopProfilePictureAddress();
        }
        String dest = address + filename;
        try {
            String tempFile = dest;
            //Delete if tempFile exists
            File fileTemp = new File(tempFile);
            if (fileTemp.exists()) {
                fileTemp.delete();
            }
        } catch (Exception e) {
            String error = e.getMessage();
            e.printStackTrace();
        }
    }
}
