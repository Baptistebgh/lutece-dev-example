/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.paris.lutece.plugins.example.service;

import fr.paris.lutece.plugins.example.business.Project;
import fr.paris.lutece.plugins.example.business.ProjectHome;
import fr.paris.lutece.portal.service.cache.AbstractCacheableService;
import fr.paris.lutece.portal.service.security.LuteceUser;

/**
 *
 * @author leridons
 */
public class ProjectCacheService extends AbstractCacheableService {

    private static final String SERVICE_NAME = "Project Cache Service";
    private static ProjectCacheService _intance = new ProjectCacheService();

    public ProjectCacheService() {
        initCache();
    }

    public static ProjectCacheService getInstance() {

        return _intance;
    }

    public String getName() {
        return SERVICE_NAME;
    }

    public Project getResource(String strId, LuteceUser user) {

        String cacheKey = getCacheKey(strId, user);

        Project r = (Project) getFromCache(cacheKey);
        if (r == null) {
            r = ProjectHome.findByPrimaryKey(Integer.parseInt(strId));

            cacheKey = getCacheKey(String.valueOf(r.getId()), user);
            putInCache(cacheKey, r);
        }
        return r;
    }

    private static String getCacheKey(String strId, LuteceUser user) {
        StringBuilder sbKey = new StringBuilder();
        sbKey.append("[project:").append(strId).append("][user:");

        if (user != null) {
            sbKey.append(user.getName());
        } else {
            sbKey.append("public");
        }
        sbKey.append("]");

        return sbKey.toString();
    }

}
