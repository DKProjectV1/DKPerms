package ch.dkrieger.permissionsystem.lib.task;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 07.07.18 22:25
 *
 */

import java.util.concurrent.TimeUnit;

public interface PermissionTaskManager {

    void runTaskAsync(Runnable runnable);

    void runTaskLater(Runnable runnable, Long duration, TimeUnit unit);

    void scheduleTask(Runnable runnable, Long repet, TimeUnit unit);

}
