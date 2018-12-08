package ch.dkrieger.permissionsystem.lib.task;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 07.07.18 22:25
 *
 */

import java.util.concurrent.TimeUnit;

public interface PermissionTaskManager {

    public void runTaskAsync(Runnable runnable);

    public void runTaskLater(Runnable runnable, Long duration, TimeUnit unit);

    public void scheduleTask(Runnable runnable, Long repet, TimeUnit unit);

}
