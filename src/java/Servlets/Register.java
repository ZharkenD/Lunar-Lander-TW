package Servlets;

import Model.Hash;
import PersistenceDB.Configurations;
import PersistenceDB.ConfigurationsJpaController;
import PersistenceDB.Users;
import PersistenceDB.UsersJpaController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Register extends HttpServlet {

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
            UsersJpaController ujc = new UsersJpaController(emf);

            if (!ujc.checkUsername(request.getParameter("username"))) {
                if (!ujc.checkEmail(request.getParameter("email"))) {
                    Users user = new Users();
                    user.setNameReal(request.getParameter("name"));
                    user.setUsername(request.getParameter("username"));
                    user.setPassword(Hash.sha1(request.getParameter("password")));
                    user.setEmail(request.getParameter("email"));
                    ujc.create(user);
                    createConf(user);
                    Map<String, String> mess = new HashMap<>();
                    mess.put("mess", "User created successfully.");
                    Gson gson = new GsonBuilder().create();
                    response.setContentType("application/json");
                    PrintWriter pw = response.getWriter();
                    pw.println(gson.toJson(mess));
                } else {
                    Map<String, String> mess = new HashMap<>();
                    mess.put("error", "Email already exists");
                    Gson gson = new GsonBuilder().create();
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.setContentType("application/json");
                    PrintWriter pw = response.getWriter();
                    pw.println(gson.toJson(mess));
                }

            } else {
                Map<String, String> mess = new HashMap<>();
                mess.put("error", "Username already exist.");
                Gson gson = new GsonBuilder().create();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json");
                PrintWriter pw = response.getWriter();
                pw.println(gson.toJson(mess));
            }
        } catch (Exception e) {
            Map<String, String> emess = new HashMap<>();
            emess.put("error", "Server error");
            Gson gs = new GsonBuilder().create();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println(gs.toJson(emess));
        }
    }

    private void createConf(Users user) {
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        ConfigurationsJpaController cjc = new ConfigurationsJpaController(emf);
        Configurations conf = new Configurations();

        conf.setConfigureName("Default");
        conf.setDiffId(0);
        conf.setSpaceshipId(0);
        conf.setPlanetId(0);
        conf.setUserId(user);
        
        cjc.create(conf);
    }
}
