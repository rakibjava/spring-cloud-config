# spring-cloud-config
There are two modules here: spring-cloud-config-server and spring-cloud-config-client.

         spring boot version: 2.6.2
         spring-cloud.version: 2021.0.0

# **spring-cloud-config-server-configuration**

**pom.xml**
      The following dependency is needed for config server, for complete pom.xml please look at the pom under spring-cloud-config-server
       
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-config-server</artifactId>
            </dependency>
            <dependencyManagement>
                <dependencies>
                    <dependency>
                        <groupId>org.springframework.cloud</groupId>
                        <artifactId>spring-cloud-dependencies</artifactId>
                        <version>${spring-cloud.version}</version>
                        <type>pom</type>
                        <scope>import</scope>
                    </dependency>
                </dependencies>
            </dependencyManagement>

**@EnableConfigServer on the main application** 

    @SpringBootApplication
    @EnableConfigServer
    public class SpringCloudConfigServerApplication {
    
        public static void main (String[] args) {
        SpringApplication.run ( SpringCloudConfigServerApplication.class, args );
        }
    }

#### **Configure application.yml or application.properties to connect the server to properties repo**

Basic connection to remote GitHub repo through username and password when used https, but it could be through ssh which is more secure. 

    spring:
      cloud:
        config:
           server:
              git:
                uri: https://github.com/{username}}/{githubrepo}.git
                username: {username}
                password: {password}
                default-label: {branch name} 
                force-pull: true

To connect through local git uri: 

    spring:
      cloud:
        config:
           server:
              git:
                uri: file:///{path-to-local-git-uri}
                default-label: {branch name}

For local development There is also a “native” profile in the Config Server that does not use Git but loads the config files from the local classpath or file system
In this case use search-locations as follows. If you use classpath means inside folder then update to prop file will not get automatically for this use file:// which is 
outside the project folder.

    spring:
      cloud:
        config:
           server:
              git:
                # search-locations: classpath:{path-to-folder-under-resource-folder} 
                # search-locations: file:////{path-to-folder}
                search-locations: file:////{path-to-folder}

Sample config file is located under classpath:localdevprop folder as follows:
        
        person:
            name: firstnamr lastname
            email: test@gmail.com

#Convention of configfile name: 
        The Config Service serves property sources from /{application}/{profile}/{label},where the default bindings in the client app are as follows:
        For example: microservice1-dev.yml
        
        microservice1= "application" = ${spring.application.name}
        dev= "profile" = ${spring.profiles.active} (actually Environment.getActiveProfiles())
        "label" = "master"
    
        
        but if we do not create the properties file based on  client application name then simply create properties file with
        1. application.yml/properties which serves as default profile to all client which connected config service
        2. application-{profile}.yml/properties which serves the  profile set by client application which connected config service

# **spring-cloud-config-client-configuration**

**pom.xml**
 The following dependency is needed for config client, for complete pom.xml please look at the pom under spring-cloud-config-client

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>
            <dependencyManagement>
                <dependencies>
                    <dependency>
                        <groupId>org.springframework.cloud</groupId>
                        <artifactId>spring-cloud-dependencies</artifactId>
                        <version>${spring-cloud.version}</version>
                        <type>pom</type>
                        <scope>import</scope>
                    </dependency>
                </dependencies>
       </dependencyManagement>

To extract the properties value there are multiple ways:
    
      1. Create a class like following wwhere use prefix of properties and inside instance variable with properties name:
         For Example: for following properties create a class like this:
        
        person:
            name: firstnamr lastname
            email: test@gmail.com

         @Component @ConfigurationProperties("person")
         public class PersonInfo {
                private String name;
                private String email;
                // gettters and setters
         }
      2. Use @value annotation: 
        @value("${person.name}")
        private String name;
        @value("${person.name}")
        private String email;
        
        But important point here is @value anotation will not refresh the updated value from config server if any value change for this 
        @RefreshSchope annotation will need to use in the class where @value use but if @vlaue use to get system properties 
        using spring spel @Value ( "#{'${limit.service.maximum}'}" ) then no need @RefreshSchope
