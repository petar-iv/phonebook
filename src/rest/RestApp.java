package rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/rest")
public class RestApp extends Application {

    @Override
    public Set<Class<?>> getClasses() {
    	Set<Class<?>> set = new HashSet<Class<?>>();
    	set.add(Endpoints.class);
        return set;
    }
	
}
