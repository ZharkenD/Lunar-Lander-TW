package Servlets;

import Model.Hash;
import PersistenceDB.Users;
import PersistenceDB.UsersJpaController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Login extends HttpServlet {

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (checkCookie(request)) {
            RequestDispatcher a = request.getRequestDispatcher("/game.jsp");
            a.forward(request, response);
        } else {
            RequestDispatcher a = request.getRequestDispatcher("/login.html");
            a.forward(request, response);
        }
    }

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
                if (user.getPassword().equals(Hash.sha1(request.getParameter("password")))) {
                    Map<String, String> emess = new HashMap<>();
                    emess.put("mess", "Succesful");
                    Gson gson = new GsonBuilder().create();
                    response.setContentType("application/json");
                    PrintWriter pw = response.getWriter();
                    pw.println(gson.toJson(emess));
                    if (request.getParameter("remember").equals("true")) {
                        response.addCookie(createCookie("lunar", request.getParameter("username"), 86400));
                        response.addCookie(createCookie("lander", Hash.sha1(request.getParameter("password")), 86400));//One day
                    } else {
                        response.addCookie(createCookie("lunar", request.getParameter("username"), 30));
                        response.addCookie(createCookie("lander", Hash.sha1(request.getParameter("password")), 30));
                    }
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

    private boolean checkCookie(HttpServletRequest request) {
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        UsersJpaController ufc = new UsersJpaController(emf);

        String cookieUsername = "lunar";
        String cookiePassword = "lander";
        String username = null;
        String password = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookieUsername.equals(cookies[i].getName())) {
                    username = cookies[i].getValue();
                }
                if (cookiePassword.equals(cookies[i].getName())) {
                    password = cookies[i].getValue();
                }
            }
            //Check if the cookie has the correct combination user & password
            if (username != null && password != null) {
                Users user = ufc.findUser(username);
                if (user.getPassword().equals(password)) {
                    return true; // ====================================================================================>
                }
            }
        }
        return false;
    }

    private Cookie createCookie(String nameCookie, String contentCookie, int duration) {
        Cookie cookie = new Cookie(nameCookie, contentCookie);
        cookie.setMaxAge(duration);
        cookie.setPath("/");
        return cookie;
    }
}
