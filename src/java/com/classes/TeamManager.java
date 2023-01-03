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
public class TeamManager {

    public TeamManager() {

    }

    public static HashMap<Integer, String> getTeams() throws ClassNotFoundException, SQLException {
        HashMap<Integer, String> teams = DBManager.GetIntStringHashMap(Tables.Teams.ID, Tables.Teams.TeamName, Tables.Teams.Table, "");
        return teams;
    }

    public static HashMap<Integer, String> getMemberTeams(int userid) throws ClassNotFoundException, SQLException {
        HashMap<Integer, String> teams = new HashMap<>();
        String userType = UserManager.getUserType(userid);
        ArrayList<Integer> teamids = DBManager.GetIntArrayList(Tables.UserJoin.ItemTwoID, Tables.UserJoin.Table, "where " + Tables.UserJoin.ItemOneType + " = 'User' and " + Tables.UserJoin.ItemTwoType + " = 'Team' and " + Tables.UserJoin.ItemOneID + " = " + userid);
        if (userType.equalsIgnoreCase("admin")) {
            teamids = DBManager.GetIntArrayList(Tables.Teams.ID, Tables.Teams.Table, "");
        }
        if (!teamids.isEmpty()) {
            for (int id : teamids) {
                String team_name = DBManager.GetString(Tables.Teams.TeamName, Tables.Teams.Table, "where " + Tables.Teams.ID + " = " + id);
                teams.put(id, team_name);
            }
        }
        return teams;
    }

    public static HashMap<Integer, String> getTeamMembers(int teamid) throws ClassNotFoundException, SQLException {
        HashMap<Integer, String> teams = new HashMap<>();
        ArrayList<Integer> userids = DBManager.GetIntArrayList(Tables.UserJoin.ItemOneID, Tables.UserJoin.Table, "where " + Tables.UserJoin.ItemOneType + " = 'User' and " + Tables.UserJoin.ItemTwoType + " = 'Team' and " + Tables.UserJoin.ItemTwoID + " = " + teamid);
        if (!userids.isEmpty()) {
            for (int id : userids) {
                String first_name = DBManager.GetString(Tables.UserTable.FirstName, Tables.UserTable.Table, "where " + Tables.UserTable.ID + " = " + id);
                String last_name = DBManager.GetString(Tables.UserTable.LastName, Tables.UserTable.Table, "where " + Tables.UserTable.ID + " = " + id);
                teams.put(id, first_name + " " + last_name);
            }
        }
        return teams;
    }

    public static ArrayList<Integer> getTeamMembersIDs(int teamid) throws ClassNotFoundException, SQLException {
        ArrayList<Integer> userids = DBManager.GetIntArrayList(Tables.UserJoin.ItemOneID, Tables.UserJoin.Table, "where " + Tables.UserJoin.ItemOneType + " = 'User' and " + Tables.UserJoin.ItemTwoType + " = 'Team' and " + Tables.UserJoin.ItemTwoID + " = " + teamid);
        return userids;
    }

    public static ArrayList<Integer> getTeamMemberIDs(int teamid) throws ClassNotFoundException, SQLException {
        ArrayList<Integer> userids = DBManager.GetIntArrayList(Tables.UserJoin.ItemOneID, Tables.UserJoin.Table, "where " + Tables.UserJoin.ItemOneType + " = 'User' and " + Tables.UserJoin.ItemTwoType + " = 'Team' and " + Tables.UserJoin.ItemTwoID + " = " + teamid);
        return userids;
    }

    public static HashMap<Integer, HashMap<String, String>> getTeamMembersDetails(int teamid) throws ClassNotFoundException, SQLException {
        HashMap<Integer, HashMap<String, String>> teams = new HashMap<>();
        ArrayList<Integer> userids = DBManager.GetIntArrayList(Tables.UserJoin.ItemOneID, Tables.UserJoin.Table, "where " + Tables.UserJoin.ItemOneType + " = 'User' and " + Tables.UserJoin.ItemTwoType + " = 'Team' and " + Tables.UserJoin.ItemTwoID + " = " + teamid);
        if (!userids.isEmpty()) {
            for (int id : userids) {
                HashMap<String, String> det = DBManager.GetTableData(Tables.UserTable.Table, "where " + Tables.UserTable.ID + " = " + id);
                teams.put(id, det);
            }
        }
        return teams;
    }

    public static HashMap<Integer, HashMap<String, String>> getTeamResources(int teamid) throws ClassNotFoundException, SQLException {
        HashMap<Integer, HashMap<String, String>> teams = new HashMap<>();
        ArrayList<Integer> ids = DBManager.GetIntArrayList(Tables.Resources.ID, Tables.Resources.Table, "where " + Tables.Resources.TeamID + " = " + teamid);
        if (!ids.isEmpty()) {
            for (int id : ids) {
                HashMap<String, String> det = DBManager.GetTableData(Tables.Resources.Table, "where " + Tables.Resources.ID + " = " + id);
                int userid = Integer.parseInt(det.get(Tables.Resources.UserID));
                String fullname = UserManager.getUserName(userid);
                det.put("user_name", fullname);
                teams.put(id, det);
            }
        }
        return teams;
    }

    public static HashMap<Integer, String> getTeamTasks(int objid, String objType) throws ClassNotFoundException, SQLException {
        HashMap<Integer, String> tasks = new HashMap<>();
        ArrayList<Integer> teamids = new ArrayList<>();
        ArrayList<String> checked = new ArrayList<>();
        if (objType.equalsIgnoreCase("User")) {
            teamids = DBManager.GetIntArrayList(Tables.UserJoin.ItemTwoID, Tables.UserJoin.Table, "where " + Tables.UserJoin.ItemOneType + " = 'User' and " + Tables.UserJoin.ItemTwoType + " = 'Team' and " + Tables.UserJoin.ItemOneID + " = " + objid);
        } else {
            teamids.add(objid);
        }
        if (!teamids.isEmpty()) {
            for (int teamid : teamids) {
                ArrayList<Integer> ids = DBManager.GetIntArrayList(Tables.TaskJoin.ItemTwoID, Tables.TaskJoin.Table, "where " + Tables.TaskJoin.ItemOneType + " = 'Team' and " + Tables.TaskJoin.ItemTwoType + " = 'Task' and " + Tables.TaskJoin.ItemOneID + " = " + teamid);
                if (objType.equalsIgnoreCase("User")) {
                    ids.addAll(DBManager.GetIntArrayList(Tables.Tasks.ID, Tables.Tasks.Table, "where " + Tables.Tasks.CreatedBy + " = " + objid));
                }
                if (!ids.isEmpty()) {
                    for (int id : ids) {
                        String name = TaskManager.getTaskTopicByID(id);
                        if (!name.trim().equals("")) {
                            String output = name.substring(0, 1).toUpperCase() + name.substring(1);
                            if (!checked.contains(output)) {
                                tasks.put(id, output);
                                checked.add(output);
                            }
                        }
                    }
                }
            }
        }
        tasks = Utilities.SortHashMapIntStringByValues(tasks);
        return tasks;
    }

    public static int getTeamLeader(int TeamID) throws ClassNotFoundException, SQLException {
        int result = DBManager.GetInt(Tables.Teams.TeamLeaderID, Tables.Teams.Table, "where " + Tables.Teams.ID + " = " + TeamID);
        return result;
    }

    public static int AddUserToTeam(int UserID, int TeamID) throws ClassNotFoundException, SQLException {
        HashMap<String, Object> tableData = new HashMap<>();
        tableData.put(Tables.UserJoin.ItemOneType, "User");
        tableData.put(Tables.UserJoin.ItemTwoType, "Team");
        tableData.put(Tables.UserJoin.ItemOneID, UserID);
        tableData.put(Tables.UserJoin.ItemTwoID, TeamID);
        tableData.put(Tables.UserJoin.LinkID, 1);
        int result = DBManager.insertTableDataReturnID(Tables.UserJoin.Table, tableData, "");
        return result;
    }
}
