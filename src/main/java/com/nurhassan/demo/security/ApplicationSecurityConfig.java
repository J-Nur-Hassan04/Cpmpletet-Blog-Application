//package com.nurhassan.demo.security;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//@Configuration
//@EnableWebSecurity
//public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
//	
//
//}
///*
//Step 1: Create a class which will extend WebSecurityConfigurerAdapter;
//Step 2: Add annotation @Configuration
//                      @EnableWebSecurity
//Step 2: Override method  UserDetailsService userDetailsService() and anoted with @Bean
//step 3: In Method 
//					List<UserDetails> users = new ArrayList();
//					user.add(User.withDefaultPasswordEncoder().username("<UserName>").password("<password>").roles("USER/ADMIN/...).build);
//					return InMemoryUserDetailsManager(users);     //return object of  UserDetailsService as we don't created a object of this 																  //so will return InMemoryUserDetailsManager(users);
//*/
//
///*
// * process for verifying user by getting data from database
// */
///*
//Step 1: Create a class which will extend WebSecurityConfigurerAdapter;
//Step 2: Add annotation @Configuration
//                      @EnableWebSecurity
//Step 3: create a method with return type AuthenticationProvider
//		public AuthenticationProvider authProvider()
//		{
//			Step 4: Create a object of DaoAuthenticationProvider 
//			DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//			
//			Step 5: Confirm that you have a model class with user data and if you don't have then create it
//			Step 6: Take a object of UserDetailsService in class and annotted it with @Autowired
//			Step 7: Create a serviceClass whcih will implement UserDetailsService interface with @Service
//					
//			
//			return provider;
//		}
//		for step 7: Override a method UserDetails(it is a interface) loadUserByUserName(String username) throws UserNameNotFound;
//					{
//						Create a repository interface of user by extending JpaRepository;
//						in repository create a method ------>>User findByUserName(String userName);
//						UserRepository userRepo; -->@Autowired
//						
//			------------User user = userRepo.findByUserName(username);
//			|			if(user == null)
//			|			{
//			|				throw new UserNotFoundException("bad Curdentials")
//			|			}
//			|		now UserDetails is an interface so we have to return"??
//			|				---->>>Create a principal class which will implement a interface UserDetails
//			'----------------->create a object User user ;
//								and constructor for setting value of user
//								
//								in collection meth
//								{
//									return Collection.singletone(new SimpleGrantedAuthority("USER"));
//								}
//								
//								public String getPassword()
//								{
//									return user.getPassword();
//								}
//								public String getUserName()
//								{
//									return user.getUserName();
//								}
//					} 
// */