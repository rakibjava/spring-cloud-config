package com.example.springcloudconfigclient.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {

   private PersonInfo personInfo;

   public PersonController (PersonInfo personInfo) {
      this.personInfo = personInfo;
   }

   @GetMapping("/person")
   private String getPersonInfo(){
      return  "Person Name: "+personInfo.getName () +"  and person email: "+personInfo.getEmail ();
   }
}
