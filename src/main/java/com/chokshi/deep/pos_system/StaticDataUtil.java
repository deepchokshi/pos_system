package com.chokshi.deep.pos_system;

public class StaticDataUtil {

    static User user;
    static Machine machine;

    public static void setCurrentUser(User user){
        StaticDataUtil.user = user;
    }

    public static User getCurrentUser(){
        return user;
    }

    public static void setCurrentMachine(Machine machine){
        StaticDataUtil.machine = machine;
    }

    public static Machine getCurrentMachine(){
        return machine;
    }
}
