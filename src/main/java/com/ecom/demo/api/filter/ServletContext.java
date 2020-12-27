package com.ecom.demo.api.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import com.ecom.demo.db.model.User;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Component
@RequestScope
@JsonFilter("ServletContextFilter")
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServletContext {


  private User user;
 
  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
 
}