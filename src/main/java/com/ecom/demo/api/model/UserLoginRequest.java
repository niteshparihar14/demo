package com.ecom.demo.api.model;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Accessors(chain = true)
@Setter
@Getter
public class UserLoginRequest {
  private String login;
  private String password;

  public boolean validate() {
    if (StringUtils.isEmpty(login)) {
      return false;
    }

    if (StringUtils.isEmpty(password)) {
      return false;
    }
    return true;
  }
}
