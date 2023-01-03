/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.classes;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import com.engine.*;

/**
 *
 * @author Stephen
 */
public class ResourceManager {
    
    public ResourceManager(){
        
    }
    
    public static void deleteFile(String filename) throws IOException {
        String address = FilePathManager.getInstance().getTeamManagerResourcesAddress();
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
    
    public static String addResource(String filename, int TeamID, int UserID, String extension) throws ClassNotFoundException, SQLException {
        HashMap<String, Object> tableData = new HashMap<>();
        tableData.put(Tables.Resources.FileName, filename);
        tableData.put(Tables.Resources.TeamID, TeamID);
        tableData.put(Tables.Resources.UserID, UserID);
        tableData.put(Tables.Resources.Extension, extension);
        int resid = DBManager.insertTableDataReturnID(Tables.Resources.Table, tableData, "");
        String result = DBManager.UpdateCurrentDate(Tables.Resources.Table, Tables.Resources.Date, "where "+Tables.Resources.ID+" = "+resid);
        return result;
    }
}
