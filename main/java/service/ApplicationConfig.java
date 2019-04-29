/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author HP
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(backend.DebugExceptionMapper.class);
        resources.add(service.EventFacadeREST.class);
        resources.add(service.InstrumentFacadeREST.class);
        resources.add(service.MusicalPieceFacadeREST.class);
        resources.add(service.SheetFacadeREST.class);
        resources.add(service.UnverifiedUserFacadeREST.class);
        resources.add(service.UserFacadeREST.class);
    }
    
}
