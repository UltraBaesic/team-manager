/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.classes;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import com.engine.*;

/**
 *
 * @author VP
 */
public class Login {

    public Login() {
    }

    public static boolean checkEmailAddressOrPhoneNumberExist(String string_to_check) throws ClassNotFoundException, SQLException, UnsupportedEncodingException {
        boolean result = false;
        int usid = DBManager.GetInt(Tables.UserTable.ID, Tables.UserTable.Table, "where " + Tables.UserTable.Email + " = '" + string_to_check + "' or " + Tables.UserTable.PhoneNumber + " = '" + string_to_check + "'");
        if (usid != 0) {
            result = true;
        }
        return result;
    }


    public static int getUserID(String EmailAddress, String PhoneNumber) throws ClassNotFoundException, SQLException, UnsupportedEncodingException {
        int usid = DBManager.GetInt(Tables.UserTable.ID, Tables.UserTable.Table, "where " + Tables.UserTable.Email + " = '" + EmailAddress + "' and " + Tables.UserTable.PhoneNumber + " = '" + PhoneNumber + "'");
        return usid;
    }

    public static int checkPasswordEmailMatch(String Password, String Email_PhoneNum) throws ClassNotFoundException, SQLException, UnsupportedEncodingException {
        int result = 0;
        String memPassword = "";
        String email = Email_PhoneNum;
        memPassword = DBManager.GetString(Tables.UserTable.Password, Tables.UserTable.Table, "where " + Tables.UserTable.Email + " = '" + Email_PhoneNum + "'");
        if (memPassword.equals("none")) {
            memPassword = DBManager.GetString(Tables.UserTable.Password, Tables.UserTable.Table, "where " + Tables.UserTable.PhoneNumber + " = '" + Email_PhoneNum + "'");
            email = DBManager.GetString(Tables.UserTable.Email, Tables.UserTable.Table, "where " + Tables.UserTable.PhoneNumber + " = '" + Email_PhoneNum + "'");
        }
        if (memPassword.equals(Password)) {
            result = DBManager.GetInt(Tables.UserTable.ID, Tables.UserTable.Table, "where " + Tables.UserTable.Email + " = '" + email + "'");
        }
        return result;
    }

}
