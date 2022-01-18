package com.example.springcloudconfigclient.resource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("person")
public class PersonInfo {
   private String name;
   private String email;


   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public String getEmail () {
      return email;
   }

   public void setEmail (String email) {
      this.email = email;
   }
}
