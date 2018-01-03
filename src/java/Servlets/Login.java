package Servlets;

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

public class Login extends HttpServlet {

    /**
     * Checking users credentials
     *
     * @param request userName and password of the user.
     * @param response In case the credentials are correct a JSON is returned
     * with the result.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
            UsersJpaController ufc = new UsersJpaController(emf);
            Users user = ufc.findUser(request.getParameter("username"));
            if (user == null) {
                Map<String, String> emess = new HashMap<>();
                emess.put("error", "Username was not found");
                Gson gson = new GsonBuilder().create();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json");
                PrintWriter pw = response.getWriter();
                pw.println(gson.toJson(emess));
            } else {
                if (user.getPassword().equals(request.getParameter("password"))) {
                    Map<String, String> emess = new HashMap<>();
                    emess.put("mess", "Succesful");
                    Gson gson = new GsonBuilder().create();
                    response.setContentType("application/json");
                    PrintWriter pw = response.getWriter();
                    pw.println(gson.toJson(emess));
                    //If the "Remember me" option is checked, the cookie will last longer
                } else {
                    Map<String, String> emess = new HashMap<>();
                    emess.put("error", "The password is not correct.");
                    Gson gson = new GsonBuilder().create();
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.setContentType("application/json");
                    PrintWriter pw = response.getWriter();
                    pw.println(gson.toJson(emess));
                }
            }
        } catch (Exception e) {
            Map<String, String> emess = new HashMap<>();
            emess.put("error", "Server error");

            Gson gson = new GsonBuilder().create();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println(gson.toJson(emess));

        }
    }
}
