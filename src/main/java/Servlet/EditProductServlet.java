package Servlet;

import Model.Product;
import Model.ProductDao;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/EditProductServlet")
public class EditProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> productList = null;
        try {
            productList = ProductDao.getAllProducts();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        request.setAttribute("productList", productList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("product-list.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String idParameter = request.getParameter("id");

        if (idParameter != null && !idParameter.isEmpty()) {
            int productId = Integer.parseInt(idParameter);
            ProductDao productDao = new ProductDao();

            try {
                if ("edit".equals(action)) {
                    Product product = productDao.getProductById(productId);
                    request.setAttribute("product", product);
                    request.getRequestDispatcher("edit-product.jsp").forward(request, response);
                } else if ("delete".equals(action)) {
                    productDao.deleteProduct(productId);
                    response.sendRedirect("EditProductServlet?action=list");
                } else {
                    List<Product> productList = productDao.getAllProducts();
                    request.setAttribute("productList", productList);
                    request.getRequestDispatcher("product-list.jsp").forward(request, response);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            response.getWriter().println("error");
        }
    }
}
