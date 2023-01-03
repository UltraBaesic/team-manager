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
 * @author Stephen
 */
public class MessageManager {

    public static String Table = Tables.Messages.Table;
    public static String ID = Tables.Messages.ID;
    public static String SenderID = Tables.Messages.SenderID;
    public static String Message = Tables.Messages.Message;
    public static String RecipientType = Tables.Messages.RecipientType;
    public static String RecipientID = Tables.Messages.RecipientID;
    public static String Time = Tables.Messages.Time;
    public static String Date = Tables.Messages.Date;
    public static String Status = Tables.Messages.Status;

    public MessageManager() {

    }

    public static String addMessage(int senderId, String message, String recipient, int recipientId) throws ClassNotFoundException, SQLException {
        HashMap<String, Object> data = new HashMap<>();
        data.put(SenderID, senderId);
        data.put(Message, message);
        data.put(RecipientType, recipient);
        data.put(RecipientID, recipientId);
        int messageID = DBManager.insertTableDataReturnID(Tables.Messages.Table, data, "");
        DBManager.UpdateCurrentTime(Table, Time, "where " + ID + " = " + messageID);
        String result = DBManager.UpdateCurrentDate(Table, Date, "where " + ID + " = " + messageID);
        return result;
    }

    public static HashMap<Integer, HashMap<String, String>> getMessages(String objectType, int objectID, int userID) throws ClassNotFoundException, SQLException {
        HashMap<Integer, HashMap<String, String>> data = new HashMap<>();
        ArrayList<Integer> messageIDs = new ArrayList<>();
        if (objectType.equalsIgnoreCase("user")) {
            messageIDs = DBManager.GetIntArrayList(ID, Table, "where " + SenderID + " = " + objectID + " and " + RecipientID + " = " + userID);
            messageIDs.addAll(DBManager.GetIntArrayList(ID, Table, "where " + RecipientID + " = " + objectID + " and " + SenderID + " = " + userID));
        } else if (objectType.equalsIgnoreCase("team")) {
            messageIDs = DBManager.GetIntArrayList(ID, Table, "where " + RecipientType + " = '" + objectType + "' and " + RecipientID + " = " + objectID);
        }
        if (!messageIDs.isEmpty()) {
            for (int mid : messageIDs) {
                HashMap<String, String> det = DBManager.GetTableData(Table, "where " + ID + " = " + mid);
                int sender_id = Integer.parseInt(det.get(SenderID));
                String fullname = UserManager.getUserName(sender_id);
                det.put("sender", fullname);
                data.put(mid, det);
            }
        }
        return data;
    }
}
