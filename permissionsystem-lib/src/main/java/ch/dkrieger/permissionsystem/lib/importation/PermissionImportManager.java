package ch.dkrieger.permissionsystem.lib.importation;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 15.07.18 21:25
 *
 */

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PermissionImportManager {

    private static PermissionImportManager INSTANCE;

    private final Map<String,PermissionImport> imports;

    public PermissionImportManager() {
        INSTANCE = this;
        this.imports =  new ConcurrentHashMap<>();
    }

    public Map<String, PermissionImport> getImports() {
        return imports;
    }

    public List<PermissionImport> getAvailableImports(){
        List<PermissionImport> list = new LinkedList<>();
        for(PermissionImport imp  :this.imports.values()) if(imp.isAvailable()) list.add(imp);
        return list;
    }

    public PermissionImport getImport(String name){
        PermissionImport imp = this.imports.get(name);
        if(imp != null && imp.isAvailable()) return imp;
        return null;
    }

    public void registerImport(PermissionImport imp){
        this.imports.put(imp.getName().toLowerCase(),imp);
    }

    public static PermissionImportManager getInstance() {
        return INSTANCE;
    }
}
