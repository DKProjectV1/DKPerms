package ch.dkrieger.permissionsystem.lib.entity;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 09.06.18 21:59
 *
 */

import ch.dkrieger.permissionsystem.lib.utils.GeneralUtil;

import java.text.SimpleDateFormat;

public class SimpleEntity {

    private final long timeout;

    public SimpleEntity(Long timeout) {
        this.timeout = timeout;
    }

    public long getTimeOut() {
        return timeout;
    }

    public String getTimeOutDate(){
        if(this.timeout <= -1) return "lifetime";
        return new SimpleDateFormat("dd.MM.yyyy kk:mm").format(timeout);
    }

    public boolean hasTimeOut(){
        return GeneralUtil.hasTimeOut(this.timeout);
    }
}
