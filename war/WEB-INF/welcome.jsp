<%@ page import="com.mvc.spring.EmployeeController.*"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
					<html>
					  <head>
					  <spring:url value="/resources/theme1/plus.png" var="buttonPlus" />
					  <spring:url value="/resources/theme1/edit1.png" var="buttonPlus1" />
					  <spring:url value="/resources/theme1/delete1.png" var="buttonPlus2" />
					  <spring:url value="/resources/theme1/sync.js" var="mainJs" />
					    <meta charset="utf-8">
					    <title></title>
							
					      <script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.6/handlebars.min.js"></script>
					    <style>
					   .card-data{
        					border-color: white;
        					background-color: #171616;
        					color: white;
        					border-radius: 10px;
        					height: 249px;
        					width: 438px;
        					line-height: 2px;
        					display: inline-block;
        					margin: 1rem;
     					}
     					.card-data:hover {
  						box-shadow: 0 14px 28px rgba(0,0,0,0.25), 0 10px 10px rgba(0,0,0,0.22);
						}
      					.header{
        					background-color: blue;
        					color: white;
        					height: 64px;
      						}
      						h3{
        					display: inline-block;
      						}
     					 #editButton{
        				height: 30px;

      					}
      					
      					input{

     					 width: 300px;
      					height: 45px;
     					 margin-bottom: 5px;
    					}
    					button{
      				height: 40px;
      					width: 308px;
      					color: white;
      					background-color: blue;
    					}
					    .List{
					     background: #fff;
  						border-radius: 2px;
  						display: inline-block;
  						height: 150px;
  						margin: 1rem;
  						position: relative;
  						width: 300px;

					    }
					    .List:hover {
  						box-shadow: 0 14px 28px rgba(0,0,0,0.25), 0 10px 10px rgba(0,0,0,0.22);
						}
						#plusButton{
        					width: 60px;
       						height: 60px;
        					float: right;
        					position: relative;
      						}
					    </style>

					    </head>
					    <body>
					    <div class="container-fluid">
					    <center><h1>Employee Details</h1></center><center><h4><a href="Logout" >Logout</h4></a></center>
					    <a href="#/new"><img src="${buttonPlus}" id="plusButton" alt=" "></a>
						
						 
						
					    <% String userEmailAddress= request.getParameter("email");
					    request.setAttribute("userEmailAddress", userEmailAddress);
					    %>
				    <hr/>

					    <div class="page">

					    </div>
					</div>

					<script type="text/template" id="user-list-template">

					{{ _.each(users, function(user) { }}
					
					<div class="card-data">
    					<div class="header">
      					<h3><b>{{= user.get('firstname') }}<b></h3>

    						</div>
    					<div class="body">
      					<h4>Firstname: {{= user.get('firstname') }}</h4>

     					 <h4>Email: {{= user.get('lastname') }}</h4>
      					<h4>Phone : {{= user.get('age') }}</h4>
      					<a href="#/edit/{{= user.id }}" style="float:right;"><img src="${buttonPlus1}" alt="" id="editButton"></a>
      					<a href="#/delete/{{= user.id }}" style="float:right;"><img src="${buttonPlus2}" alt="" id="editButton"></a>
     					
    </div>
  </div>
  				{{  }); }}

				</script>


					<script type="text/template" id="edit-user-template">
					<form class="edit-user-form">
					<legend>{{= user ? 'Update' :'Create'  }}User</legend>
					<label>Name</label>
					<input type="text" name="firstname" value="{{= user ? user.get('firstname') : '' }}" />
					<label>Email</label>
					<input type="text" name="lastname" value="{{= user ? user.get('lastname') : '' }}" />
					<label> Mobile</label>
					<input type="text" name="age" value="{{= user ? user.get('age') : '' }}" />
					<hr/>
					<%
					request.setAttribute("userEmailAddress", userEmailAddress);
					%>
					<button type="submit" class="btn btn-default">{{= user ? 'Update' :'Create'  }}</button>
					
					{{ if(user) { }}
					<input type="hidden" name="id" value="{{= user.id }}" />


					<button id="{{= user.id }}"type="button" class="btn btn-default delete" >Delete</button>
					{{ }; }}
					
					</form>
					<br>
					<br>
					<button><a href="#/">Cancel</a></button>
					</script>



	   <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>


	   <script src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.4.2/underscore-min.js" ></script>
	   <script src="https://cdnjs.cloudflare.com/ajax/libs/backbone.js/0.9.2/backbone-min.js" ></script>
	   <script src="${mainJs}"></script>

					  <script >
					 
					_.templateSettings = {
    					evaluate: /\{\{(.+?)\}\}/g,
    					interpolate: /\{\{=(.+?)\}\}/g,
    					escape: /\{\{-(.+?)\}\}/g
						};


					      var Users = Backbone.Collection.extend({
					      url : '/users'
					      });

					      var User = Backbone.Model.extend({
					      urlRoot : '/users'
					      });



					    var UserList = Backbone.View.extend({
					      el: '.page',
					      render: function(){
					      var that=this;
					      var users = new Users();
					      users.fetch({
					         success: function(users){
					         console.log(users);
					           var template= _.template($('#user-list-template').html(), {users : users.models});
					            that.$el.html(template);
					         }
					       })



					     }

					   });

					   var EditUser = Backbone.View.extend({
					   el: '.page',
					   model : Users,
					   render: function(options){
					   var that= this;
					   if(options.id){
					    var user= new User({id: options.id});
					    user.fetch({
					    success: function(user){
					      var template= _.template($('#edit-user-template').html(), {user: user});
					            that.$el.html(template);
					    }


					    })
					   }else{
					   var template= _.template($('#edit-user-template').html(), {user: null});
					            this.$el.html(template);
					   }

					   },
					   events: {
					   'submit .edit-user-form' : 'saveUser',
					   'click .delete' : 'deleteUser'

					   },



					   saveUser : function(ev){
					   var userDetails = $(ev.currentTarget).serializeObject();
					   var user = new User();
					       user.save(userDetails, {
					        success: function(user){
					          console.log(user.toJSON());
					          router.navigate('',{trigger:true});

					   }

					   })
					   console.log(userDetails);
					   return false;
					   },


					   deleteUser : function(event){
					   var self = this;
					   console.log(event);
					   var ids = event.target.id;
					    console.log(ids);
					    var user = new User({id : ids});
					    var usersDelete= new Users();
					   user.destroy({
					   success: function(user){
						
					    router.navigate('',{trigger:true});
					    
					   }
					   });
					   return false;
					   }
					   });

					    var Router = Backbone.Router.extend({
					      routes: {
					        '': 'home',
					        'new': 'editUser',
					        'edit/:id' : 'editUser',
					        'delete/:id' : 'editUser'
					      }
					    });


					    var userList = new UserList();
					    var editUser = new EditUser();

					   var router = new Router();
					   router.on('route:home', function() {
					      userList.render();

					    });

					    router.on('route:editUser', function(id) {
					    editUser.render({id: id});

					    });
					   Backbone.history.start();
   					</script>
					  </body>
					</html>
