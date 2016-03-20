/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.bicss.sam.trading;

import java.beans.*;
import java.io.File;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import net.froihofer.util.jboss.WildflyAuthDBHelper;


@Startup
@Singleton
public class StartupBean {
  private String status;

  @PostConstruct
  void init() {
        WildflyAuthDBHelper authHelper;

        // setup initial default bank user with default credentials and role.
        try {
            String password = "bank";
            /*
            TODO can not read out password defined in ejb-jar.xml
             InitialContext iniCtx = new InitialContext();
            Context envCtx = (Context) iniCtx.lookup("java:comp/env");
            password = (String) envCtx.lookup("password");
             */
            String homeDir = System.getProperty("jboss.home.dir");
            authHelper = new WildflyAuthDBHelper(new File(homeDir));
            String[] bankRoles = new String[1];
            bankRoles[0] = "bank";

            authHelper.addUser("bank", password, bankRoles);
            System.out.println("Bank user prepared.");

            //} 
            //catch (NamingException ex) {
            //    System.out.println("Naming exception.");
        } catch (IOException ex) {
            System.out.println("Bank user setup bank role io exception.");
        }
  }

}

