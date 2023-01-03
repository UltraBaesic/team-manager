/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engine;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Stephen
 */
public class DBManager {

    public DBManager() {

    }

    public static HashMap<Integer, HashMap<String, String>> GetAllCollumnsLimitNumberOfRows(int StartSeed, int limit, String TableName, String Condition) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        HashMap<Integer, HashMap<String, String>> TableData = new HashMap<>();
        String sql = "Select * from " + TableName + " " + Condition + " LIMIT " + StartSeed + "," + limit;

        try {
            con = new JDBCConnector().getConnection();
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.last()) {
                HashMap<String, String> result = new HashMap<>();
                ResultSetMetaData rsmd = rs.getMetaData();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    int type = rsmd.getColumnType(i);
                    String ColumnName = rsmd.getColumnName(i);
                    if (type == Types.VARCHAR || type == Types.CHAR || type == Types.LONGVARCHAR) {
                        result.put(ColumnName, rs.getString(i));
                    } else if (type == Types.INTEGER) {
                        result.put(ColumnName, "" + rs.getInt(i));
                    } else if (type == Types.DATE) {
                        result.put(ColumnName, "" + rs.getDate(i));
                    } else if (type == Types.TIME) {
                        result.put(ColumnName, "" + rs.getTime(i));
                    } else if (type == Types.TIMESTAMP) {
                        result.put(ColumnName, "" + rs.getTimestamp(i));
                    } else {
                        result.put(ColumnName, "" + rs.getLong(i));
                    }
                }

                Integer TempID = Integer.parseInt(result.get("id"));
                TableData.put(TempID, result);

                while (rs.previous()) {
                    HashMap<String, String> resultToo = new HashMap<>();
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        int type = rsmd.getColumnType(i);
                        String ColumnName = rsmd.getColumnName(i);
                        if (type == Types.VARCHAR || type == Types.CHAR || type == Types.LONGVARCHAR) {
                            resultToo.put(ColumnName, rs.getString(i));
                        } else if (type == Types.INTEGER) {
                            resultToo.put(ColumnName, "" + rs.getInt(i));
                        } else if (type == Types.DATE) {
                            resultToo.put(ColumnName, "" + rs.getDate(i));
                        } else if (type == Types.TIME) {
                            resultToo.put(ColumnName, "" + rs.getTime(i));
                        } else if (type == Types.TIMESTAMP) {
                            resultToo.put(ColumnName, "" + rs.getTimestamp(i));
                        } else {
                            resultToo.put(ColumnName, "" + rs.getLong(i));
                        }
                    }

                    TempID = Integer.parseInt(resultToo.get("id"));
                    TableData.put(TempID, resultToo);

                }

            }
        } catch (SQLException e) {
            String error = e.getMessage();
            System.out.print(error);
            return TableData;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return TableData;
    }

    public static int GetInt(String outputColumn, String TableName, String Condition) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int result = 0;
        String sql = "Select * from " + TableName + " " + Condition;

        try {
            con = new JDBCConnector().getConnection();
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.last()) {
                result = rs.getInt(outputColumn);
            } else {
                result = 0;
            }
        } catch (SQLException e) {
            String error = e.getMessage();
            System.out.print(error);
            return result;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public static int GetFirstInt(String outputColumn, String TableName, String Condition) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int result = 0;
        String sql = "Select * from " + TableName + " " + Condition;

        try {
            con = new JDBCConnector().getConnection();
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.first()) {
                result = rs.getInt(outputColumn);
            } else {
                result = 0;
            }
        } catch (SQLException e) {
            String error = e.getMessage();
            System.out.print(error);
            return result;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public static String GetString(String outputColumn, String TableName, String Condition) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String result = "none";
        String sql = "Select * from " + TableName + " " + Condition;

        try {
            con = new JDBCConnector().getConnection();
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rs.first();
            if (rs.first()) {
                result = rs.getString(outputColumn);
            } else {
                result = "none";
            }
        } catch (SQLException e) {
            String error = e.getMessage();
            System.out.print(error);
            return result;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public static String GetLastString(String outputColumn, String TableName, String Condition) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String result = "none";
        String sql = "Select * from " + TableName + " " + Condition;

        try {
            con = new JDBCConnector().getConnection();
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rs.last();
            if (rs.last()) {
                result = rs.getString(outputColumn);
            } else {
                result = "none";
            }
        } catch (SQLException e) {
            String error = e.getMessage();
            System.out.print(error);
            return result;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public static String GetLimitedString(String outputColumn, String TableName, String Condition, int start, int limit) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String result = "none";
        String sql = "SELECT SUBSTRING(`" + outputColumn + "`, " + start + ", " + limit + ") FROM " + TableName + " " + Condition;

        try {
            con = new JDBCConnector().getConnection();
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rs.first();
            if (rs.first()) {
                result = rs.getString(1);
            } else {
                result = "none";
            }
        } catch (SQLException e) {
            String error = e.getMessage();
            System.out.print(error);
            return result;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public static ArrayList<String> GetStringArrayList(String outputColumn, String TableName, String Condition) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<String> result = new ArrayList<>();
        String sql = "Select * from " + TableName + " " + Condition;

        try {
            con = new JDBCConnector().getConnection();
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rs.first();
            if (rs.last()) {
                result.add(rs.getString(outputColumn));
                while (rs.previous()) {
                    result.add(rs.getString(outputColumn));
                }
            }
        } catch (SQLException e) {
            String error = e.getMessage();
            System.out.print(error);
            return result;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public static ArrayList<Integer> GetIntArrayList(String outputColumn, String TableName, String Condition) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Integer> result = new ArrayList<>();
        String sql = "Select * from " + TableName + " " + Condition;

        try {
            con = new JDBCConnector().getConnection();
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.last()) {
                result.add(rs.getInt(outputColumn));
                while (rs.previous()) {
                    result.add(rs.getInt(outputColumn));
                }
            }
        } catch (SQLException e) {
            String error = e.getMessage();
            System.out.print(error);
            return result;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public static ArrayList<Integer> GetIntArrayListDescending(String outputColumn, String TableName, String Condition) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Integer> result = new ArrayList<>();
        String sql = "Select * from " + TableName + " " + Condition;

        try {
            con = new JDBCConnector().getConnection();
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.first()) {
                result.add(rs.getInt(outputColumn));
                while (rs.next()) {
                    result.add(rs.getInt(outputColumn));
                }
            }
        } catch (SQLException e) {
            String error = e.getMessage();
            System.out.print(error);
            return result;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public static HashMap<Integer, String> GetIntStringHashMap(String outputColumn1, String outputColumn2, String TableName, String Condition) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        HashMap<Integer, String> result = new HashMap<>();
        String sql = "Select * from " + TableName + " " + Condition;

        try {
            con = new JDBCConnector().getConnection();
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.last()) {
                result.put(rs.getInt(outputColumn1), rs.getString(outputColumn2));
                while (rs.previous()) {
                    result.put(rs.getInt(outputColumn1), rs.getString(outputColumn2));
                }
            }
        } catch (SQLException e) {
            String error = e.getMessage();
            System.out.print(error);
            return result;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public static HashMap<String, String> GetTableData(String TableName, String Condition) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        HashMap<String, String> result = new HashMap<>();
        String sql = "Select * from " + TableName + " " + Condition;

        try {
            con = new JDBCConnector().getConnection();
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.last()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    int type = rsmd.getColumnType(i);
                    String ColumnName = rsmd.getColumnName(i);
                    if (type == Types.VARCHAR || type == Types.CHAR || type == Types.LONGVARCHAR) {
                        result.put(ColumnName, rs.getString(i));
                    } else if (type == Types.INTEGER) {
                        result.put(ColumnName, "" + rs.getInt(i));
                    } else if (type == Types.DATE) {
                        result.put(ColumnName, "" + rs.getDate(i));
                    } else if (type == Types.TIME) {
                        result.put(ColumnName, "" + rs.getTime(i));
                    } else {
                        result.put(ColumnName, "" + rs.getLong(i));
                    }
                }
                while (rs.previous()) {
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        int type = rsmd.getColumnType(i);
                        String ColumnName = rsmd.getColumnName(i);
                        if (type == Types.VARCHAR || type == Types.CHAR || type == Types.LONGVARCHAR) {
                            result.put(ColumnName, rs.getString(i));
                        } else if (type == Types.INTEGER) {
                            result.put(ColumnName, "" + rs.getInt(i));
                        } else if (type == Types.DATE) {
                            result.put(ColumnName, "" + rs.getDate(i));
                        } else if (type == Types.TIME) {
                            result.put(ColumnName, "" + rs.getTime(i));
                        } else {
                            result.put(ColumnName, "" + rs.getLong(i));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            String error = e.getMessage();
            System.out.print(error);
            return result;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public static HashMap<String, String> GetStringStringHashMap(String outputColumn1, String outputColumn2, String TableName, String Condition) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        HashMap<String, String> result = new HashMap<>();
        String sql = "Select * from " + TableName + " " + Condition;

        try {
            con = new JDBCConnector().getConnection();
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.last()) {
                result.put(rs.getString(outputColumn1), rs.getString(outputColumn2));
                while (rs.previous()) {
                    result.put(rs.getString(outputColumn1), rs.getString(outputColumn2));
                }
            }
        } catch (SQLException e) {
            String error = e.getMessage();
            System.out.print(error);
            return result;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public static String DeleteObject(String TableName, String Condition) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String result = "";
        String sql = "Delete from " + TableName + " " + Condition;

        try {
            con = new JDBCConnector().getConnection();
            stmt = con.prepareStatement(sql);
            stmt.executeUpdate();
            result = "successful";
        } catch (SQLException e) {
            String error = e.getMessage();
            System.out.print(error);
            result = "failed";
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public static String UpdateCurrentDate(String TableName, String Column, String Condition) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String result = "";
        String sql = "UPDATE " + TableName + " SET " + Column + " = CURRENT_DATE " + Condition;

        try {
            con = new JDBCConnector().getConnection();
            stmt = con.prepareStatement(sql);
            stmt.executeUpdate();
            result = "successful";
        } catch (SQLException e) {
            String error = e.getMessage();
            System.out.print(error);
            result = "failed";
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public static String UpdateCurrentTime(String TableName, String Column, String Condition) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String result = "";
        String sql = "UPDATE " + TableName + " SET " + Column + " = CURRENT_TIME " + Condition;

        try {
            con = new JDBCConnector().getConnection();
            stmt = con.prepareStatement(sql);
            stmt.executeUpdate();
            result = "successful";
        } catch (SQLException e) {
            String error = e.getMessage();
            System.out.print(error);
            result = "failed";
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public static String UpdateCurrentTimeStamp(String TableName, String Column, String Condition) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String result = "";
        String sql = "UPDATE " + TableName + " SET " + Column + " = CURRENT_TIMESTAMP " + Condition;

        try {
            con = new JDBCConnector().getConnection();
            stmt = con.prepareStatement(sql);
            stmt.executeUpdate();
            result = "successful";
        } catch (SQLException e) {
            String error = e.getMessage();
            System.out.print(error);
            result = "failed";
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public static String InsertStringData(String TableName, String inputColumn, String Data, String Condition) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String result = "failed";
        String sql = "INSERT INTO " + TableName + " (" + inputColumn + ") VALUES (?) " + Condition;

        try {
            con = new JDBCConnector().getConnection();
            stmt = con.prepareStatement(sql);
            stmt.setString(1, Data);
            stmt.executeUpdate();
            result = "success";
        } catch (SQLException e) {
            String error = e.getMessage();
            System.out.print(error);
            return result;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public static String InsertIntData(String inputColumn, int Data, String TableName, String Condition) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String result = "failed";
        String sql = "INSERT INTO " + TableName + " (" + inputColumn + ") VALUES (" + Data + ") " + Condition;

        try {
            con = new JDBCConnector().getConnection();
            stmt = con.prepareStatement(sql);
            stmt.executeUpdate();
            result = "success";
        } catch (SQLException e) {
            String error = e.getMessage();
            System.out.print(error);
            return result;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public static String UpdateStringData(String TableName, String inputColumn, String Data, String Condition) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String result = "failed";
        String sql = "Update " + TableName + " SET " + inputColumn + " = ? " + Condition;

        try {
            con = new JDBCConnector().getConnection();
            stmt = con.prepareStatement(sql);
            stmt.setString(1, Data);
            stmt.executeUpdate();
            result = "success";
        } catch (SQLException e) {
            String error = e.getMessage();
            System.out.print(error);
            return result;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public static String UpdateIntData(String inputColumn, int Data, String TableName, String Condition) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String result = "failed";
        String sql = "Update " + TableName + " SET " + inputColumn + " = ? " + Condition;

        try {
            con = new JDBCConnector().getConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, Data);
            stmt.executeUpdate();
            result = "success";
        } catch (SQLException e) {
            String error = e.getMessage();
            System.out.print(error);
            return result;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public static String ExchangeforString(String outputColumn, String TableName, String Condition) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String result = "";
        String sql = "Select * from " + TableName + " " + Condition;

        try {
            con = new JDBCConnector().getConnection();
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rs.first();
            if (rs.first()) {
                result = rs.getString(outputColumn);
            } else {
                result = "none";
            }
        } catch (SQLException e) {
            String error = e.getMessage();
            System.out.print(error);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public static int ExchangeforInt(String outputColumn, String TableName, String Condition) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int result = 0;
        String sql = "Select * from " + TableName + " " + Condition;

        try {
            con = new JDBCConnector().getConnection();
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.last()) {
                result = rs.getInt(outputColumn);
            } else {
                result = 0;
            }
        } catch (SQLException e) {
            String error = e.getMessage();
            System.out.print(error);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public static ArrayList<String> ExchangeforStringArrayList(String outputColumn, String TableName, String Condition) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<String> result = new ArrayList<>();
        String sql = "Select * from " + TableName + " " + Condition;

        try {
            con = new JDBCConnector().getConnection();
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rs.first();
            if (rs.first()) {
                result.add(rs.getString(outputColumn));
                while (rs.next()) {
                    result.add(rs.getString(outputColumn));
                }
            }
        } catch (SQLException e) {
            String error = e.getMessage();
            System.out.print(error);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public static ArrayList<Integer> ExchangeforIntArrayList(String outputColumn, String TableName, String Condition) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Integer> result = new ArrayList<>();
        String sql = "Select * from " + TableName + " " + Condition;

        try {
            con = new JDBCConnector().getConnection();
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rs.first();
            if (rs.first()) {
                result.add(rs.getInt(outputColumn));
                while (rs.next()) {
                    result.add(rs.getInt(outputColumn));
                }
            }
        } catch (SQLException e) {
            String error = e.getMessage();
            System.out.print(error);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public static Date GetDate(String outputColumn, String TableName, String Condition) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Date result = null;
        String sql = "Select * from " + TableName + " " + Condition;

        try {
            con = new JDBCConnector().getConnection();
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rs.first();
            if (rs.first()) {
                result = rs.getDate(outputColumn);
            } 
        } catch (SQLException e) {
            String error = e.getMessage();
            System.out.print(error);
            return result;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public static Time GetTime(String outputColumn, String TableName, String Condition) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Time result = null;
        String sql = "Select * from " + TableName + " " + Condition;

        try {
            con = new JDBCConnector().getConnection();
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rs.first();
            if (rs.first()) {
                result = rs.getTime(outputColumn);
            }
        } catch (SQLException e) {
            String error = e.getMessage();
            System.out.print(error);
            return result;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public static String insertTableData(String Tablename, HashMap<String, Object> Data, String Condition) throws ClassNotFoundException, SQLException {
        String result = "failed";
        String dataString = "";
        String Columns = "";
        ArrayList<String> Cols = new ArrayList<>();
        Set<String> keys = Data.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String col = iterator.next();
            Cols.add(col);
            Columns += "`" + col + "`,";
            dataString += "?,";
        }

        dataString = dataString.replaceAll(",$", "");
        Columns = Columns.replaceAll(",$", "");
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "INSERT INTO " + Tablename + " (" + Columns + ") VALUES (" + dataString + ") " + Condition;

        try {
            con = new JDBCConnector().getConnection();
            stmt = con.prepareStatement(sql);
            for (int count = 0; count < Data.size(); count++) {
                String col = Cols.get(count);
                stmt.setObject(count + 1, Data.get(col));
            }
            stmt.executeUpdate();
            result = "success";
        } catch (SQLException e) {
            String error = e.getMessage();
            System.out.print(error);
            return error;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public static int insertTableDataReturnID(String Tablename, HashMap<String, Object> Data, String Condition) throws ClassNotFoundException, SQLException {
        int result = 0;
        String dataString = "";
        String Columns = "";
        ArrayList<String> Cols = new ArrayList<>();
        Set<String> keys = Data.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String col = iterator.next();
            Cols.add(col);
            Columns += "`" + col + "`,";
            dataString += "?,";
        }

        dataString = dataString.replaceAll(",$", "");
        Columns = Columns.replaceAll(",$", "");
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "INSERT INTO " + Tablename + " (" + Columns + ") VALUES (" + dataString + ") " + Condition;

        try {
            con = new JDBCConnector().getConnection();
            stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int count = 0; count < Data.size(); count++) {
                String col = Cols.get(count);
                stmt.setObject(count + 1, Data.get(col));
            }
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Data entry failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    result = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Data entry failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            String error = e.getMessage();
            System.out.print(error);
            return result;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }
    
    public static String LinkObjects(String Tablename, String ItemOneType, int ItemOneID, String ItemTwoType, int ItemTwoID) throws ClassNotFoundException, SQLException{
        HashMap<String, Object> tableData = new HashMap<>();
        tableData.put("LinkId", 1);
        tableData.put("ItemOneType", ItemOneType);
        tableData.put("ItemOneId", ItemOneID);
        tableData.put("ItemTwoType", ItemTwoType);
        tableData.put("ItemTwoId", ItemTwoID);
        String result = DBManager.insertTableData(Tablename, tableData, "");
        return result;
    }
}
