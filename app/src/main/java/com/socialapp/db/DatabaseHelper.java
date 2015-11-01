package com.socialapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.location.places.Places;
import com.socialapp.bean.Challenge;
import com.socialapp.bean.Team;
import com.socialapp.utils.LoginInfo;

import java.util.ArrayList;

/**
 * Created by Sanjoy on 01-11-2015.
 */
public class DatabaseHelper  extends SQLiteOpenHelper {

    private static  String DATABASE_NAME = "socialApp";
    private static final int DATABASE_VERSION = 1;
    private static final String TYPE_TXT = " TEXT";
    private static final String TYPE_INT = " INTEGER";
    private static final String TYPE_REAL = " REAL";

    private static final String CONST_PRIMARY_KEY = "PRIMARY KEY";
    private static final String CONST_CHECK = "CHECK";

    private static final String IN = "IN";
    private static final String UNIQUE  =" unique";

    private static final String TEAM_TABLE = "Team";
    private static final String ID = "localId";
    private static final String TEAM_ID = "teamId";
    private static final String NAME = "teamName";
    private static final String OWNER = "ownerName";
    private static final String TEAM_ICON_PATH = "iconPath";

    private static final String CHALLENGE_TABLE = "Challenge";
    private static final String CHALLENGE_DESC = "desc";
    private static final String CHALLENGE_DATE = "date";;

    private static DatabaseHelper database;

    private static LoginInfo loginInfo;
    private static final String TAG=DatabaseHelper.class.getSimpleName();

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context)
    {
//        loginInfo=new LoginInfo(context);
//        DATABASE_NAME=DATABASE_NAME_INIT+loginInfo.getLoggedInUser();
        if (database==null)
        {
            database = new DatabaseHelper(context);
        }
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query_team = "CREATE TABLE IF NOT EXISTS " + TEAM_TABLE + " ("
                + ID + TYPE_INT + " " + CONST_PRIMARY_KEY + " AUTOINCREMENT , " + NAME + TYPE_TXT + ", " + OWNER + TYPE_TXT + ", "+ TEAM_ICON_PATH + TYPE_TXT + ");";
        db.execSQL(query_team);
        String query_challenge = "CREATE TABLE IF NOT EXISTS " + CHALLENGE_TABLE + " ("
                + ID + TYPE_INT + " " + CONST_PRIMARY_KEY + " AUTOINCREMENT , "+ NAME + TYPE_TXT + ","+ TEAM_ID + TYPE_INT + ","+CHALLENGE_DESC + TYPE_TXT + ", "+OWNER + TYPE_TXT + ", " +CHALLENGE_DATE + TYPE_TXT+ "   );";
        db.execSQL(query_challenge);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }

    public long insertTeam(Team team)
    {
        long id=0;
        SQLiteDatabase db=this.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(NAME, team.getName());
            cv.put(OWNER, team.getOwnerName());
            //cv.put(TEAM_ID, team.getId());
            cv.put(TEAM_ICON_PATH, team.getIconLocalPath());
            id=  db.insert(TEAM_TABLE, null, cv);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public ArrayList<Team> getTeamList()
    {
        Cursor cursor = null;
        ArrayList<Team> teams=new ArrayList<Team>();
        String id = "";
        try {
            SQLiteDatabase db=this.getWritableDatabase();
            cursor = db.query(TEAM_TABLE, new String[]{ID, NAME, OWNER, TEAM_ICON_PATH},
                    null, null, null, null, null);
            if (cursor != null)
            {
                while(cursor.moveToNext())
                {
                    Team team=new Team();
                    team.setLocalId(cursor.getInt(cursor.getColumnIndex(ID)));
                    team.setIconLocalPath(cursor.getString(cursor.getColumnIndex(TEAM_ICON_PATH)));
                    team.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                    team.setOwnerName(cursor.getString(cursor.getColumnIndex(OWNER)));
                    teams.add(team);
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (null != cursor)
                cursor.close();
        }
        return teams;
    }

    public long insertChallenge(Challenge challenge)
    {
        long id=0;
        SQLiteDatabase db=this.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(NAME, challenge.getName());
            cv.put(OWNER, challenge.getCreatedBy());
            cv.put(TEAM_ID, challenge.getTeamId());
            cv.put(CHALLENGE_DATE, challenge.getDate());
            cv.put(CHALLENGE_DESC, challenge.getDescription());
            id=  db.insert(CHALLENGE_TABLE, null, cv);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public ArrayList<Challenge> getChallengeList( int teamId)
    {
        Cursor cursor = null;
        ArrayList<Challenge> challenges=new ArrayList<Challenge>();
        String id = "";
        try {
            SQLiteDatabase db=this.getWritableDatabase();
            cursor = db.query(CHALLENGE_TABLE, new String[]{ID,TEAM_ID, NAME,CHALLENGE_DESC, OWNER, CHALLENGE_DATE},
                    null, null, null, null, null);
            if (cursor != null)
            {
                while(cursor.moveToNext())
                {
                    Challenge challenge=new Challenge();
                    challenge.setDescription(cursor.getString(cursor.getColumnIndex(CHALLENGE_DESC)));
                    challenge.setDate(cursor.getString(cursor.getColumnIndex(CHALLENGE_DATE)));
                    challenge.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                    challenge.setCreatedBy(cursor.getString(cursor.getColumnIndex(OWNER)));
                    challenge.setTeamId(cursor.getInt(cursor.getColumnIndex(TEAM_ID)));
                    challenge.setLocalId(cursor.getInt(cursor.getColumnIndex(ID)));;
                    challenges.add(challenge);
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (null != cursor)
                cursor.close();
        }
        return challenges;
    }
}
