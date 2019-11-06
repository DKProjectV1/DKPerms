package ch.dkrieger.permissionsystem.lib.entity;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 21:59
 *
 */

import ch.dkrieger.permissionsystem.lib.utils.GeneralUtil;
import ch.dkrieger.permissionsystem.lib.utils.Messages;

import java.text.SimpleDateFormat;

public class SimpleEntity {

    private final long timeout;

    public SimpleEntity(Long timeout) {
        this.timeout = timeout;
    }

    public long getTimeOut() {
        return timeout;
    }

    public long getRemaining(){
        if(timeout < 0) return -1;
        else return timeout-System.currentTimeMillis();
    }

    public String getRemainingFormatted(){
        long duration = getRemaining();
        if(duration == -1) return Messages.PLACEHOLDER_EXPIRY_NEVER;

        long secondsInMillis = 1000;
        long minutesInMillis = secondsInMillis * 60;
        long hoursInMillis = minutesInMillis *60;
        long daysInMillis = hoursInMillis *24;

        long days = duration / daysInMillis;
        duration = duration % daysInMillis;

        long hours = duration / hoursInMillis;
        duration = duration % hoursInMillis;

        long minutes = duration / minutesInMillis;
        duration = duration % minutesInMillis;

        long seconds = duration / secondsInMillis;

        long time;
        String unit;

        if(days > 0){
            time = days;
            unit = days > 1?Messages.UNIT_DAY_PLURAL:Messages.UNIT_DAY_SINGULAR;
        }else if(hours > 0){
            time = hours;
            unit = hours > 1?Messages.UNIT_HOUR_PLURAL:Messages.UNIT_HOUR_SINGULAR;
        }else if(minutes > 0){
            time = minutes;
            unit = minutes > 1?Messages.UNIT_MINUTE_PLURAL:Messages.UNIT_MINUTE_SINGULAR;
        }else {
            time = seconds;
            unit = seconds > 1?Messages.UNIT_SECOND_PLURAL:Messages.UNIT_SECOND_SINGULAR;
        }

        return Messages.PLACEHOLDER_EXPIRY_TEMPORARY.replace("[time]",String.valueOf(time)).replace("[unit]",unit);
    }

    public String getTimeOutDate(){
        if(this.timeout <= -1) return "lifetime";
        return new SimpleDateFormat("dd.MM.yyyy kk:mm").format(timeout);
    }

    public boolean hasTimeOut(){
        return GeneralUtil.hasTimeOut(this.timeout);
    }
}
