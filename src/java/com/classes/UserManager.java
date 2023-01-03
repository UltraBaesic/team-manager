/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.classes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import com.engine.*;

/**
 *
 * @author VP
 */
public class UserManager {

    public UserManager() {
    }

    public static HashMap<Integer, String> getAllMembersIdName() throws ClassNotFoundException, SQLException {
        HashMap<Integer, String> MembersIdName = new HashMap<>();
        ArrayList<Integer> MemberIds = DBManager.GetIntArrayList(Tables.UserTable.ID, Tables.UserTable.Table, "");
        if (!MemberIds.isEmpty()) {
            for (int memberId : MemberIds) {
                String FirstName = DBManager.GetString(Tables.UserTable.FirstName, Tables.UserTable.Table, "where " + Tables.UserTable.ID + "=" + memberId);
                String LastName = DBManager.GetString(Tables.UserTable.LastName, Tables.UserTable.Table, "where " + Tables.UserTable.ID + "=" + memberId);
                String Name = FirstName + " " + LastName;
                MembersIdName.put(memberId, Name);
            }
        }
        return MembersIdName;
    }

    public static HashMap<Integer, String> getAllMembersIdEmail() throws ClassNotFoundException, SQLException {
        HashMap<Integer, String> MembersIdName = new HashMap<>();
        ArrayList<Integer> MemberIds = DBManager.GetIntArrayList(Tables.UserTable.ID, Tables.UserTable.Table, "");
        if (!MemberIds.isEmpty()) {
            for (int memberId : MemberIds) {
                String Email = DBManager.GetString(Tables.UserTable.Email, Tables.UserTable.Table, "where " + Tables.UserTable.ID + "=" + memberId);
                MembersIdName.put(memberId, Email);
            }
        }
        return MembersIdName;
    }

    public static HashMap<String, String> getUserMemberDetails(int UserID) throws ClassNotFoundException, SQLException {
        HashMap<String, String> userTableData = DBManager.GetTableData("user", "where id = " + UserID);
        HashMap<String, String> userMemberTableData = DBManager.GetTableData("member", "where userId = " + UserID);
        HashMap<String, String> userMemberDetails = new HashMap<>();
        userMemberDetails.putAll(userTableData);
        userMemberDetails.putAll(userMemberTableData);
        return userMemberDetails;
    }

    public static String getUserType(int UserID) throws ClassNotFoundException, SQLException {
        String type = DBManager.GetString(Tables.UserTable.UserType, Tables.UserTable.Table, "where " + Tables.UserTable.ID + "=" + UserID);
        return type;
    }

    public static String getUserName(int UserID) throws ClassNotFoundException, SQLException {
        String Name = "", FirstName, LastName;
        FirstName = DBManager.GetString(Tables.UserTable.FirstName, Tables.UserTable.Table, "where " + Tables.UserTable.ID + "=" + UserID);
        LastName = DBManager.GetString(Tables.UserTable.LastName, Tables.UserTable.Table, "where " + Tables.UserTable.ID + "=" + UserID);
        Name = FirstName + " " + LastName;
        return Name;
    }

    public static String getDateJoined(int UserID) throws ClassNotFoundException, SQLException {
        String Date = "" + DBManager.GetDate(Tables.UserTable.Date, Tables.UserTable.Table, "where " + Tables.UserTable.ID + "=" + UserID);
        return Date;
    }

    public static String UpdateUserMemberDetails(int UserID, String EdittedpersFirstName, String EdittedpersLastName, String EdittedpersEmail, String EdittedpersPhone, String EdittedpersAddress) throws ClassNotFoundException, SQLException {
        String result = DBManager.UpdateStringData("member", "first_name", EdittedpersFirstName, "where userId = " + UserID);
        result = DBManager.UpdateStringData("member", "last_name", EdittedpersLastName, "where userId = " + UserID);
        result = DBManager.UpdateStringData("user", "email", EdittedpersEmail, "where userId = " + UserID);
        result = DBManager.UpdateStringData("user", "phone_number", EdittedpersPhone, "where id = " + UserID);
        result = DBManager.UpdateStringData("user", "address", EdittedpersAddress, "where id = " + UserID);
        return result;
    }

    public static ArrayList<Integer> getLimitedMemberIDs(int start, int limit, String searchtxt) throws ClassNotFoundException, SQLException {
        ArrayList<Integer> Ids = new ArrayList<>();
        if (searchtxt.equals("")) {
            Ids = DBManager.GetIntArrayListDescending(Tables.UserTable.ID, "books", "ORDER by " + Tables.UserTable.FirstName + " DESC LIMIT " + start + ", " + limit);
        } else {
            Ids = DBManager.GetIntArrayListDescending(Tables.UserTable.ID, "books", "where "+Tables.UserTable.FirstName+" LIKE %"+searchtxt+"% or "+Tables.UserTable.LastName+" LIKE %"+searchtxt+"% ORDER by " + Tables.UserTable.FirstName + " DESC LIMIT " + start + ", " + limit);
        }
        return Ids;
    }
}
