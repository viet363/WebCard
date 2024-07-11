package Servlet;

import Model.User;
import Model.UserDao;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/user-list")
public class UserListServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<User> userList = UserDao.getAllUsers();
            request.setAttribute("userList", userList);
        } catch (Exception e) {
            // Log the exception or redirect to an error page
            e.printStackTrace();
            request.setAttribute("error", "Error fetching user list");
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/user-list.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            String usernameToDelete = request.getParameter("username");
            deleteUser(usernameToDelete);
        } else if ("update".equals(action)) {
            String usernameToUpdate = request.getParameter("username");
            String newPassword = request.getParameter("newPassword");
            String newEmail = request.getParameter("newEmail");
            String newType = request.getParameter("newType");
            String newSdt = request.getParameter("newSdt");

            updateUser(usernameToUpdate, newPassword, newEmail, newType, newSdt);
        }

        response.sendRedirect(request.getContextPath() + "/user-list");
    }

    private void deleteUser(String username) {
        try {
            UserDao.deleteUser(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUser(String username, String newPassword, String newEmail, String newType, String newSdt) {
        try {
            User updatedUser = new User(username, newPassword, newEmail, newType, newSdt);
            UserDao.updateUser(updatedUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
