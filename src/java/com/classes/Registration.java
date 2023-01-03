/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.classes;

import java.sql.SQLException;
import java.util.HashMap;
import com.engine.*;

/**
 *
 * @author VP
 */
public class Registration {

    public Registration() {
    }
    
    public static int CreateMember(String FirstName, String LastName, String Email, int TeamID, String Password, String Phone) throws ClassNotFoundException, SQLException {
        HashMap<String, Object> tableData = new HashMap<>();
        tableData.put(Tables.UserTable.FirstName, FirstName);
        tableData.put(Tables.UserTable.LastName, LastName);
        tableData.put(Tables.UserTable.Email, Email);
        tableData.put(Tables.UserTable.PhoneNumber, Phone);
        tableData.put(Tables.UserTable.Password, Password);
        tableData.put(Tables.UserTable.UserType, "member");
        int userID = DBManager.insertTableDataReturnID(Tables.UserTable.Table, tableData, "");
        DBManager.UpdateCurrentDate(Tables.UserTable.Table, Tables.UserTable.Date, "where "+Tables.UserTable.ID+" = "+userID);
        int result = TeamManager.AddUserToTeam(userID, TeamID);
        return result;
    }

    public static String generateRegistrationActivationCode() {
        String result = "some code";
        return result;
    }

}
