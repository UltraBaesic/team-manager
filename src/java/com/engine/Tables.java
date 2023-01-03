/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engine;

/**
 *
 * @author VP
 */
public final class Tables {
    
    public static class Comments{
        public static String Table = "comments";
        public static String ID = "id";
        public static String Comment = "comment";
        public static String SenderID = "sender_id";
        public static String Date = "date";
        public static String Time = "time";
    }
    
    public static class Journals{
        public static String Table = "journal";
        public static String ID = "id";
        public static String Text = "text";
        public static String SenderID = "sender_id";
        public static String Date = "date";
        public static String Time = "time";
    }
    
    public static class Messages {
        public static String Table = "messages";
        public static String ID = "id";
        public static String SenderID = "sender_id";
        public static String Message = "message";
        public static String RecipientType = "recipient_type";
        public static String RecipientID = "recipient_id";
        public static String Time = "time";
        public static String Date = "date";
        public static String Status = "status";
    }
    
    public static class Resources {

        public static String Table = "resources";
        public static String ID = "id";
        public static String FileName = "file_name";
        public static String TeamID = "teamid";
        public static String UserID = "userid";
        public static String Date = "date";
        public static String Extension = "extension";
    }
    
    public static class Tasks {

        public static String Table = "tasks";
        public static String ID = "id";
        public static String TaskTopic = "task_topic";
        public static String Description = "description";
        public static String DateCreated = "date_created";
        public static String TimeCreated = "time_created";
        public static String CreatedBy = "created_by";
        public static String CreatedFor = "created_for";
        public static String TimeFinished = "time_finished";
        public static String DateFinished = "date_finished";
        public static String TimeStarted = "time_started";
        public static String DateStarted = "date_started";
        public static String TimeBudjet = "time_budget_minutes";
        public static String Duration = "duration";
        public static String Status = "status";
        public static String TaskType = "task_type";
    }
    
    public static class TaskJoin {

        public static String Table = "task_join";
        public static String ID = "id";
        public static String LinkID = "LinkId";
        public static String ItemOneType = "ItemOneType";
        public static String ItemTwoType = "ItemTwoType";
        public static String ItemOneID = "ItemOneId";
        public static String ItemTwoID = "ItemTwoId";
    }    
    
    public static class Teams {

        public static String Table = "teams";
        public static String ID = "id";
        public static String TeamName = "team_name";
        public static String TeamLeaderID = "team_leader_id";
    }
    
    public static class Timesheet {

        public static String Table = "timesheet";
        public static String ID = "id";
        public static String TaskID = "task_id";
        public static String UserID = "user_id";
        public static String SessionID = "session_id";
        public static String Topic = "topic";
        public static String Comment = "comment";
        public static String OtherObjectType = "other_object_type";
        public static String OtherObjectID = "other_object_id";
        public static String Date = "date";
        public static String Time = "time";
    }
    
    public static class UserTable {

        public static String Table = "users";
        public static String ID = "id";
        public static String FirstName = "first_name";
        public static String LastName = "last_name";
        public static String Email = "email";
        public static String PhoneNumber = "phone_number";
        public static String Password = "password";
        public static String Date = "date_joined";
        public static String UserType = "type";
    }
    
    public static class UserJoin {

        public static String Table = "userjoin";
        public static String ID = "id";
        public static String LinkID = "LinkId";
        public static String ItemOneType = "ItemOneType";
        public static String ItemTwoType = "ItemTwoType";
        public static String ItemOneID = "ItemOneId";
        public static String ItemTwoID = "ItemTwoId";
    }    
    
}
