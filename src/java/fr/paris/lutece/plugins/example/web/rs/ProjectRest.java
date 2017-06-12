package fr.paris.lutece.plugins.example.web.rs;

import java.util.Collection;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import fr.paris.lutece.plugins.example.business.Project;
import fr.paris.lutece.plugins.example.business.ProjectHome;
import fr.paris.lutece.plugins.extend.modules.hit.business.Hit;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.plugins.extend.modules.hit.service.HitService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.plugins.rest.util.json.JSONUtil;
import fr.paris.lutece.portal.service.message.SiteMessageException;

import net.sf.json.JSONObject;

/**
 * Main class of Project Rest Service 
 * 
 * @author leridons
 */
@Path( RestConstants.BASE_PATH + "example" )
public class ProjectRest 
{
    /**
     * builds the project json object
     * 
     * @param project
     * @return the json object
     */
    private JSONObject buildJSON( Project project ) 
    {

        JSONObject json = new JSONObject( );
        json.accumulate( "id", project.getId( ) );
        json.accumulate( "name", project.getName( ) );
        json.accumulate( "description", project.getDescription( ) );
        json.accumulate( "cost", project.getCost( ) );

        return json;
    }

    /**
     * builds the project json object with nb of hits
     * 
     * @param project
     * @return the json object
     */
    private JSONObject buildJSONwithStats( Project project ) 
    {
        JSONObject json = new JSONObject( );

        // get the HitService in the spring context
        HitService hs = SpringContextService.getBean( HitService.BEAN_SERVICE );

        String strIdExtendableResource = String.valueOf( project.getId( ) ); // extend resource ID
        String strExtendableResourceType = Project.PROPERTY_RESOURCE_TYPE; // extend resource type

        Hit hit;
        // search nb of hits
        hit = hs.findByParameters( strIdExtendableResource, strExtendableResourceType );

        json.accumulate( "id", project.getId( ) );
        json.accumulate( "name", project.getName( ) );
        if ( hit != null ) 
        {
            json.accumulate( "hitNb", hit.getNbHits( ) );
        }

        return json;
    }

    /**
     * builds a list of  project json 
     * 
     * @return the list
     */
    @GET
    @Path( "/projects" )
    @Produces( MediaType.APPLICATION_JSON )
    public String getProjectList( ) 
    {

        Collection<Project> listeProject = ProjectHome.getProjectsList( );

        JSONObject listejsonProject = new JSONObject( );

        for ( Project project : listeProject ) 
        {
            listejsonProject.accumulate( "projects", this.buildJSON( project ) );
        }

        return listejsonProject.toString( 2 );
    }

    /**
     * Project Json provider
     * 
     * @param nId
     * @return the json string
     * @throws SiteMessageException 
     */
    @GET
    @Path( "/projects/{id}" )
    @Produces( MediaType.APPLICATION_JSON )
    public String getProjectById( @PathParam( "id" ) int nId ) throws SiteMessageException 
    {

        String strJSON = "";
        try 
        {
            Project project = ProjectHome.findByPrimaryKey( nId );
            strJSON = this.buildJSON( project ).toString( 2 );
        } 
        catch ( NumberFormatException e ) 
        {
            strJSON = JSONUtil.formatError( "Invalid project number", 3 );
        } 
        catch ( Exception e ) 
        {
            strJSON = JSONUtil.formatError( "project not found", 1 );
        }

        return strJSON;
    }

    /**
     * json project with stats provider
     *
     * @param nId
     * @return the json string
     * @throws SiteMessageException 
     */
    @GET
    @Path( "/projects_stats/{id}" )
    @Produces( MediaType.APPLICATION_JSON )
    public String getProjectStatsById( @PathParam( "id" ) int nId ) throws SiteMessageException 
    {

        String strJSON = "";
        try 
        {
            Project project = ProjectHome.findByPrimaryKey( nId );
            strJSON = this.buildJSONwithStats( project ).toString( 2 );
        } 
        catch ( NumberFormatException e ) 
        {
            strJSON = JSONUtil.formatError( "Invalid project number", 3 );
        } 
        catch ( Exception e ) 
        {
            strJSON = JSONUtil.formatError( "project not found", 1 );
        }

        return strJSON;

    }


}
