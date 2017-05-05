package com.mvc.spring;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
//import org.json.JSONException;
//import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.datastore.Transaction;
//import com.google.appengine.labs.repackaged.org.json.JSONException;
//import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.gson.Gson;
//import com.google.appengine.repackaged.com.google.gson.Gson;
import com.mvc.spring.PMF;
import com.mvc.spring.EmployeeLogin;
import com.mvc.spring.EmployeeData;




//import com.google.appengine.labs.repackaged.org.json.JSONObject;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
@Controller
@RequestMapping("/users")
public class EmployeeController {
	
	private static final Logger log = Logger.getLogger(EmployeeData.class.getName());
	
	@RequestMapping(value = "/LoginWithGoogle")
	public ModelAndView go() {
		return new ModelAndView(
				"redirect:https://accounts.google.com/o/oauth2/auth?redirect_uri=https://feisty-flames-164905.appspot.com/users/oauth2callback&response_type=code&client_id=449161362660-mmf2004fms9panuosnvll682lnr2fb4g.apps.googleusercontent.com&approval_prompt=force&scope=email&access_type=online");
	}
	
	
	
	@RequestMapping(value = "/oauth2callback")
	public void get_authorization_code(HttpServletRequest req,
			HttpServletResponse resp) throws IOException, JSONException {


		// code for getting authorization_code

		String auth_code= req.getParameter("code");
		
		System.out.println(auth_code);

		// Code for getting access token from the authorization_code

		
		URL url = new URL("https://www.googleapis.com/oauth2/v4/token?"
				+ "client_id=449161362660-mmf2004fms9panuosnvll682lnr2fb4g.apps.googleusercontent.com"
				+ "&client_secret=jgC-oIBUtIYav8GGJ6db-er-&" + "redirect_uri=https://feisty-flames-164905.appspot.com/users/oauth2callback&"
				+ "grant_type=authorization_code&" + "code=" + auth_code);
		
		
		HttpURLConnection connect = (HttpURLConnection) url.openConnection();
		connect.setRequestMethod("POST");
		connect.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connect.setDoOutput(true);
		BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
		String inputLine;
		String response = "";
		while ((inputLine = in.readLine()) != null) {
			response += inputLine;
		}
		in.close();
		System.out.println(response.toString());
		JSONParser parser = new JSONParser();
		JSONObject json_access_token = null;
		try {
			json_access_token = new JSONObject(response);
		} catch (JSONException e) {

			e.printStackTrace();
		}          
		// String access_token="";
		String access_token = null;
		try {
			access_token = (String) json_access_token.get("access_token");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Access token =" + access_token);

		// code for getting user details by sending access token.......

		URL obj1 = new URL("https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + access_token);
		HttpURLConnection conn = (HttpURLConnection) obj1.openConnection();
		BufferedReader in1 = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine1;
		String responsee = "";
		while ((inputLine1 = in1.readLine()) != null) {
			responsee += inputLine1;
		}
		in1.close();
		System.out.println(responsee.toString());
		JSONObject json_user_details = null;
		try {
			json_user_details = new JSONObject(responsee);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		PrintWriter p=resp.getWriter();
		

		
		HttpSession session=req.getSession();
	System.out.println(json_user_details);
       session.setAttribute("email",(String) json_user_details.get("email"));
//        // Get session creation time.
       Date createTime = new Date(session.getCreationTime());
//        // Get last access time of this web page.
      Date lastAccessTime = new Date(session.getLastAccessedTime());
        session.setAttribute("email",(String) json_user_details.get("email"));
	String userEmail = null;
		String userName = null;
	try {
			userEmail = (String) json_user_details.get("email");
		userName = (String) json_user_details.get("name");
	} catch (JSONException e) {		// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	
	
	
	/*
	 * private void TransferFunds(Key fromKey, Key toKey, long amount)
{
    using (var transaction = _db.BeginTransaction())
    {
        var entities = transaction.Lookup(fromKey, toKey);
        entities[0]["balance"].IntegerValue -= amount;
        entities[1]["balance"].IntegerValue += amount;
        transaction.Update(entities);
        transaction.Commit();
    }
}*/
	
	// private static final Logger log = Logger.getLogger(TaskqueueServlet.class.getName());
	   
/// Task Queue
 //Push Queues
		      //  Queue queue = QueueFactory.getDefaultQueue(); ** If we didn't configure any queue in queue.xml, then we should use 'getDefaultAueue();'
		        Queue queue = QueueFactory.getQueue("Loginqueue");
		                   queue.add(TaskOptions.Builder.withUrl("/users/oauth2callback").param("email", json_user_details.get("email").toString())
		                		                                                         .param("Username", json_user_details.get("name").toString())
		                		                                                         .param("Given name", json_user_details.get("given_name").toString())
		                		                                                        // .param("Family name", json_user_details.get("Family name").toString())
		                		                                                         .param("picture URL", json_user_details.get("picture").toString()));//To specify the options for the task. Here options include parameters for the task and the URL that is going to execute task.
	                       // queue.add(transaction, TaskOptions.Builder.withUrl("/users/oauth2callback")) ;
	                       // ofy().getTransaction();
	                          //ofy().getTranscation();}*/
	
/*	//PULL QUEUES
 *            final int numberOfTasksToAdd = 100;
	    final int numberOfTasksToLease = 100;
	     boolean useTaggedTasks = true;

	
	
	                    Queue queue = QueueFactory.getQueue("pull-queue");
	                      
	                    if (!useTaggedTasks) {
	                        for (int i = 0; i < numberOfTasksToAdd; i++) {
	                               queue.add(TaskOptions.Builder.withMethod(TaskOptions.Method.PULL)
                                           .payload(json_user_details.get("email").toString()));    
	             */
	                        
		PersistenceManager pm = PMF.get().getPersistenceManager();
	if (userEmail != null) {
		Query q = pm.newQuery(EmployeeLogin.class);
			q.setFilter(" email == '" + userEmail + "'");
		@SuppressWarnings("unchecked")
		List<EmployeeLogin> EmployeeLoginData = (List<EmployeeLogin>) q.execute();
			if (!(EmployeeLoginData.isEmpty()))
		{
				System.out.println("to prevent from null");
		}

			else {
				EmployeeLogin objPojo = new EmployeeLogin();
			objPojo.setEmail(userEmail);
		objPojo.setUname(userName);



			pm.makePersistent(objPojo);
		}		}

		PrintWriter pp=resp.getWriter();
		pp.println("Email ID is"+json_user_details.get("email"));
		pp.println("Username is"+json_user_details.get("name"));
		
		pp.println("Given name is"+json_user_details.get("given_name"));
		pp.println("Family name is"+json_user_details.get("family_name"));
		pp.println("picture URL"+json_user_details.get("picture"));
		pp.println("Gender is"+json_user_details.get("gender"));
			
      System.out.println("This is the end of the code");
	}
  
      @RequestMapping(value = "/delete", method = RequestMethod.GET)
  	public void ddelte(HttpServletRequest req, HttpServletResponse respon) throws IOException {               
    	  PersistenceManager pm= PMF.get().getPersistenceManager();
  		 Query c = pm.newQuery(EmployeeData.class);
 		 System.out.println(c);
 		 c.getFetchPlan().setFetchSize(2);
     		pm.deletePersistent(c);

   
      }
//	return new ModelAndView(
//			"welcome.jsp?mail=" + json_user_details.get("email") + "&username=" + json_user_details.get("name"));

	
	
	@RequestMapping(value = "/crontask1", method = RequestMethod.GET)
	public void crontask(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		 System.out.println("inside cron job");
		 PersistenceManager pm= PMF.get().getPersistenceManager();
		 //EmployeeData c = new EmployeeData();
		// c.setAge("20");
		// pm.makePersistent(c);
		 System.out.println("I am here");
		 Query c = pm.newQuery(EmployeeData.class);
		 @SuppressWarnings("unchecked")
		 
		 List<EmployeeData> list= (List<EmployeeData>) c.execute();
		 log.info(c.toString());
		 c.getFetchPlan().setFetchSize(2);
    		pm.deletePersistent(list.get(0));
    		
	}
	
	@RequestMapping(value = "/LoginWithFacebook")
	public ModelAndView goF() {
		System.out.println("The Login Facebook get method is calling");
		return new ModelAndView(
				"redirect:https://www.facebook.com/dialog/oauth?client_id=262454907549893&redirect_uri=http://localhost:8888/users/fbhome&scope=email");
	}
	
	@RequestMapping(value = "/fbhome", method = RequestMethod.GET)
	public ModelAndView getF_authorization_code(HttpServletRequest request,
			HttpServletResponse response) throws IOException, JSONException {
	
		System.out.println("This si the fbhome is calling");
		String outputString = ""; 
		EmployeeLogin fbp = null;
	    try
	    {
	      String rid = request.getParameter("request_ids");
	      if (rid != null)
	      {
	        response.sendRedirect("https://www.facebook.com/dialog/oauth?client_id=262454907549893&redirect_uri=http://localhost:8888/users/fbhome");
	      }
	      else
	      {
	        String code = request.getParameter("code");
	        if (code != null)
	        {
	          URL url = new URL(
	            "https://graph.facebook.com/oauth/access_token?client_id=262454907549893&redirect_uri=http://localhost:8888/users/fbhome&client_secret=2dee422b75771ec56cb9202895f6a887&code=" + 
	            code);
	          HttpURLConnection conn = (HttpURLConnection)url
	            .openConnection();
	          conn.setRequestMethod("POST");
	          conn.setConnectTimeout(20000);
	          
	          BufferedReader reader = new BufferedReader(
	            new InputStreamReader(conn.getInputStream()));
	          String line;
	          while ((line = reader.readLine()) != null)
	          {
	            outputString = outputString + line;
	          }
	          System.out.println(outputString);
	          String accessToken = null;
	          if (outputString.indexOf("access_token") != -1)
	          {
	            accessToken = outputString.substring(15, outputString.indexOf("&"));
	          }
	          System.out.println(accessToken);
	          url = new URL("https://graph.facebook.com/me?access_token=" +accessToken);
	          System.out.println(url);
	          URLConnection conn1 = url.openConnection();
	          conn1.setConnectTimeout(7000);
	          outputString = "";
	          reader = new BufferedReader(new InputStreamReader(
	            conn1.getInputStream()));
	          while ((line = reader.readLine()) != null) {
	            outputString = outputString + line;
	            
	          }
	          System.out.println("This is reader data"+reader);
	          reader.close();
	          System.out.println("This is output String"+outputString);
	          fbp = (EmployeeLogin)new Gson().fromJson(outputString, 
	            EmployeeLogin.class);
	          System.out.println(fbp);
	          request.setAttribute("auth", fbp);

	          System.out.println(fbp.getUname());
	          
	          PersistenceManager pm = PMF.get().getPersistenceManager();
//	  		if (fbp.getUname() != null) {
//	  			Query q = pm.newQuery(EmployeeLogin.class);
////	  			q.setFilter(" email == '" + fbp.getUname() + "'");
//	  			@SuppressWarnings("unchecked")
//	  			List<EmployeeLogin> EmployeeLoginData = (List<EmployeeLogin>) q.execute();
//	  			if (!(EmployeeLoginData.isEmpty()))
//	  			{
//	  				System.out.println("to prevent from null");
//	  			}
//
//	  			else {
//	  				EmployeeLogin objPojo = new EmployeeLogin();
//	  				objPojo.setEmail(fbp.getUname());
//
//
//
//	  				pm.makePersistent(objPojo);
//	  			}
//	  		}
	        } 
	      }
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	    System.out.println(fbp.getUname());
	    return new ModelAndView("welcome.jsp?mail=" + fbp.getUname());
	}

	
    		/*
    		 * private void TransferFunds(Key fromKey, Key toKey, long amount)
{
    using (var transaction = _db.BeginTransaction())
    {
        var entities = transaction.Lookup(fromKey, toKey);
        entities[0]["balance"].IntegerValue -= amount;
        entities[1]["balance"].IntegerValue += amount;
        transaction.Update(entities);
        transaction.Commit();
    }
}
    		 */


		
	@RequestMapping(value = "", method = RequestMethod.GET)
	public void LandingPage(ModelMap model,HttpServletRequest req, HttpServletResponse resp) throws IOException {

		 System.out.println("All the datas are sending from backend to front end.... running");
		 String retVal ="";

		 PersistenceManager pm= PMF.get().getPersistenceManager();


			HashMap<String,Object> response = new HashMap<String,Object>();
			response.put("status", false);
			 HttpSession session=req.getSession(false);
			 if(session!=null){
			        String name=(String)session.getAttribute("email");
			        System.out.println(name+"This is the session data email");
		      
			        Query q	=	pm.newQuery("SELECT FROM " + EmployeeData.class.getName() + " WHERE employeeLoginEmail =='"+name+"'");

			        List<EmployeeData> list= (List<EmployeeData>) q.execute();
					if(list.size()>0)
					{
					Gson obj = new Gson();
					retVal = obj.toJson(list);
					response.put("status", true);
					response.put("list", retVal);
					}
			 }

				//q.setOrdering("date desc");

				resp.getWriter().write(retVal);
				//return retVal;

	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public void SavingData(@RequestBody String body, ModelMap model,HttpServletRequest req, HttpServletResponse resp) throws IOException {

		 System.out.println("Saving new User.... running");

		 System.out.println(body);
		 HttpSession session=req.getSession(false);
		 if(session!=null){
		        String name=(String)session.getAttribute("email");
		        System.out.println(name+"This is Adding  part email accessing data");
		 }

			PersistenceManager pm= PMF.get().getPersistenceManager();
			Gson gson = new Gson();
			JSONObject JSON = null;
			try {
				 if(session!=null){
				        String name=(String)session.getAttribute("email");
				        System.out.println(name+"This is Adding  part email accessing data");

				JSON = new JSONObject(body);
				String firstname=JSON.getString("firstname");
				String lastname=JSON.getString("lastname");
				String age=JSON.getString("age");
				



				// Persisting new Phone details
				EmployeeData r=new EmployeeData();
				r.setFirstname(firstname);
				r.setLastname(lastname);
				r.setAge(age);
				r.setEmployeeLoginEmail(name);

		        pm.makePersistent(r);

		        String s = "Registration Success.";
				System.out.println(s);
				resp.getWriter().write(body);
				 }
			} catch (JSONException e) {

				e.printStackTrace();
			}
			finally {
				pm.close();
			}


	}


	@RequestMapping(method=RequestMethod.GET, value="/{id}")
    @ResponseBody
	 public String editing(HttpServletRequest request,HttpServletResponse resp, ModelMap model,@PathVariable String id) throws IOException {
    		PersistenceManager pm= PMF.get().getPersistenceManager();


    		System.out.println("Update Get is calling");
    		long key = Long.parseLong(id);
			Query q = pm.newQuery(EmployeeData.class);
			q.setFilter("id == idParameter");
			//q.setFilter("name == nameParameter");
			//q.setFilter("id == idParameter"+"&&"+"id == '"+id+"'");
			q.declareParameters("String idParameter");
			List<EmployeeData> list= (List<EmployeeData>) q.execute(key);
			EmployeeData	UDobj	=	(EmployeeData)list.get(0);

			Gson obj = new Gson();
			String retVal = obj.toJson(UDobj);

			return retVal;

		}
    @RequestMapping(method=RequestMethod.PUT, value="/{id}")
	public void updateEntry(@RequestBody String data, HttpServletRequest request, HttpServletResponse resp,@PathVariable String id) throws IOException{

		String k=id;
		long key=Long.parseLong(k);
		PersistenceManager pm= PMF.get().getPersistenceManager();


		HttpSession session=request.getSession(false);
		 if(session!=null){
		        String name=(String)session.getAttribute("email");
		        System.out.println(name+"This is Update part email accessing data");
		 }


//		    Key key=KeyFactory.stringToKey(key1);
		    JSONObject JSON = null;
			try {
				JSON = new JSONObject(data);
				String firstname=JSON.getString("firstname");
				String lastname=JSON.getString("lastname");
				String age=JSON.getString("age");


				EmployeeData US = pm.getObjectById(EmployeeData.class, key);
				US.setFirstname(firstname);
				US.setLastname(lastname);
				US.setAge(age);

				resp.getWriter().write(data);



			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}

    @RequestMapping(method=RequestMethod.DELETE, value="/{id}")
    public void deleteData(HttpServletRequest request,HttpServletResponse resp, @PathVariable String id) throws IOException {



    	String k=id;
		long key=Long.parseLong(k);

    	try{

	    	PersistenceManager pm = PMF.get().getPersistenceManager();

	    	//EmployeeData c = pm.get
    		EmployeeData c = pm.getObjectById(EmployeeData.class, key);
    		pm.deletePersistent(c);
    	}
    	catch(Exception e){
    		System.out.println(e);
    	}
    	resp.getWriter().write(k);
    }

    @RequestMapping(value = "/Logout")
    public void Logout(HttpServletRequest request,HttpServletResponse resp) throws IOException {


			HttpSession session=request.getSession();
			session.invalidate();
			

        resp.sendRedirect("../../index.html");
    }

    }

