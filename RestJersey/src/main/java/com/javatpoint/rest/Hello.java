/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javatpoint.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.*;

@Path("/hello")
public class Hello {
    // This method is called if HTML and XML is not requested  
   public static String Email = "";
    @POST
    @Path("/login")
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(@FormParam("email") String email, @FormParam("pwd") String pwd ) throws URISyntaxException {
    	Email = email;
    	try{  
    		Class.forName("com.mysql.jdbc.Driver");  
    		Connection con=DriverManager.getConnection(  
    		"jdbc:mysql://localhost:3306/jersey","root","shivamagarwal");  
    		//here sonoo is database name, root is username and password  
    		Statement stmt=con.createStatement();  
    		
    		ResultSet rs=stmt.executeQuery("select Email,Password from AddUser");  
    		while(rs.next())  
    		{
    			if(email.equals(rs.getString(1)) && pwd.equals(rs.getString(2)))
    			{
    				URI location = new URI("http://localhost:8014/RestJersey/home.html");
    		    	return Response.seeOther(location).build(); 	
    			}
    			
    			
    			
    		}
    		con.close();  
    		}catch(Exception e){ System.out.println(e);}  
    	
		URI location = new URI("http://localhost:8014/RestJersey/login.html");
    	return Response.seeOther(location).status(401).build();		
    	
        
    }
    @POST
    @Path("/signup")
    @Produces(MediaType.TEXT_PLAIN)
    public Response signup(@FormParam("uname") String name,@FormParam("email") String email, @FormParam("pwd") String pwd ) throws URISyntaxException {
    	try{  
    		Class.forName("com.mysql.jdbc.Driver");  
    		Connection con=DriverManager.getConnection(  
    		"jdbc:mysql://localhost:3306/jersey","root","shivamagarwal");  
    		//here sonoo is database name, root is username and password  
    	    
    		PreparedStatement ps=con.prepareStatement("insert into AddUser values(?,?,?)");  
    		
    		ps.setString(1,name);  
    		ps.setString(2,email);  
    		ps.setString(3,pwd);  
    		ps.executeUpdate();  	
    		
    		con.close();  
    		}catch(Exception e){ System.out.println(e);}  
    		  
    	URI location = new URI("http://localhost:8014/RestJersey/index.html");
    	return Response.seeOther(location).build(); 
    	
        
    }
    @GET
    @Path("/viewall")
    @Produces(MediaType.TEXT_HTML)
    public String viewall() throws URISyntaxException {
    	String output = "";
    	try{  
    		Class.forName("com.mysql.jdbc.Driver");  
    		Connection con=DriverManager.getConnection(  
    		"jdbc:mysql://localhost:3306/jersey","root","shivamagarwal");  
    		//here sonoo is database name, root is username and password  
    		Statement stmt=con.createStatement();  
    		ResultSet rs=stmt.executeQuery("select * from UserComments"); 
    		output="<html><body>";
    		while(rs.next())  
    		{
    			output += "<p>"+rs.getString(1) + " : " + rs.getString(2) + "</p>";
    		}
    		output+="</body></html>";
    		con.close();
    		return output;
    		}catch(Exception e){ System.out.println(e);}  
    		  
//    	URI location = new URI("http://localhost:8014/RestJersey/index.html");
//    	return Response.seeOther(location).build(); 
    	
    	return null;
        
    }
	/*
	 * // This method is called if XML is requested
	 * 
	 * @GET
	 * 
	 * @Path("/xml")
	 * 
	 * @Produces(MediaType.TEXT_XML) public String sayXMLHello() { return
	 * "<?xml version=\"1.0\"?>" + "<hello> Hello Jersey" + "</hello>"; }
	 * 
	 * // This method is called if HTML is requested
	 * 
	 * @GET
	 * 
	 * @Path("/html")
	 * 
	 * @Produces(MediaType.TEXT_HTML) public String sayHtmlHello() { return
	 * "<html> " + "<title>" + "Hello Jersey" + "</title>" + "<body><h1>" +
	 * "Hello Jersey HTML" + "</h1></body>" + "</html> "; }
	 */
    @GET
    @Path("/YourComments")
    @Produces(MediaType.TEXT_PLAIN)
    public Response viewYourComments() throws URISyntaxException {
    	String output = "";
    	String name="";
    	
    	
    	
    	try{  
    		Class.forName("com.mysql.jdbc.Driver");  
    		Connection con=DriverManager.getConnection(  
    		"jdbc:mysql://localhost:3306/jersey","root","shivamagarwal");  
    	
    		Statement stmt=con.createStatement();
    		
//    		String query = "select name from User where email ="+Email;
//    		ResultSet rs=stmt.executeQuery(query); 
    			
    		String query = "select Name from AddUser where Email =?";
    		PreparedStatement ps = con.prepareStatement(query);
    		ps.setString(1,Email);
    		ResultSet rs = ps.executeQuery();
            
    		while(rs.next())  
    		{
    			
    			name = rs.getString(1);
    		}
    	
    		ResultSet rs1=stmt.executeQuery("select * from UserComments where Name='"+name+"'");
    		output="<html><body>";
    		while(rs.next())  
    		{
    			output += "<p>"+rs.getString(1) + " : " + rs.getString(2) + "</p>";
    		}
    		output+="</body></html>";
    		con.close();
    		return Response.status(200).entity(output).build();	
    		}catch(Exception e){ System.out.println(e);}  

    	return null;
    }
    @POST
    @Path("/AddComment")
    @Produces(MediaType.TEXT_PLAIN)
    public Response AddComment(@FormParam("comment") String comment ) throws URISyntaxException {
    	String name ="";
    	try{  
    		Class.forName("com.mysql.jdbc.Driver");  
    		Connection con=DriverManager.getConnection(  
    		"jdbc:mysql://localhost:3306/jersey","root","shivamagarwal");  
    		String query = "select Name from AddUser where Email =?";
    		PreparedStatement ps = con.prepareStatement(query);
    		ps.setString(1,Email);
    		ResultSet rs = ps.executeQuery();
    	 		while(rs.next())  
    		{
    			name = rs.getString(1);
    		}
    		
    		
    		PreparedStatement ps1=con.prepareStatement("insert into UserComments values(?,?)");  
    		
    		ps1.setString(1,name);  
    		ps1.setString(2,comment);  
    		  
    		ps1.executeUpdate();  	
    		
    		con.close();  
    		}catch(Exception e){ System.out.println(e);}  
    		  
    	URI location = new URI("http://localhost:8014/RestJersey/home.html");
    	return Response.seeOther(location).build(); 
    	
        
    }    

}
