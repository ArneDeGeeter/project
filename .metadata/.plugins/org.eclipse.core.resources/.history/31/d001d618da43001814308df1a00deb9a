package vibration.JSF;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import vibration.EJB.UserManagementEJBLocal;


 
@WebServlet("/uploadServlet")
@MultipartConfig(maxFileSize = 16177215)    // upload file's size up to 16MB
public class FileUploadDBServlet extends HttpServlet {
	
	@EJB
     private UserManagementEJBLocal userEJB;
     
    /* database connection settings
    private String dbURL = "jdbc:mysql://localhost:3306/AppDB";
    private String dbUser = "root";
    private String dbPass = "secret"; */
     
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    	
        // gets values of text fields
        String firstName = request.getParameter("firstName");
         
        InputStream inputStream = null; // input stream of the upload file
         
        // obtains the upload file part in this multipart request
        
        Part filePart = request.getPart("photo");
        
        if (filePart != null) {
        	
            System.out.println(firstName);
            System.out.println(filePart.getName());
            System.out.println(filePart.getSize());
            System.out.println(filePart.getContentType());
             
            inputStream = filePart.getInputStream();
            
            
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            
            int read = 0;
            final byte[] bytes = new byte[16177215];

            while ((read = inputStream.read(bytes)) != -1) {
            	buffer.write(bytes, 0, read);
            }
            
            System.out.println(buffer.size());
            byte[] image = buffer.toByteArray();
            System.out.println(image.length);
            
            userEJB.uploadenFoto(image,firstName);
            
            getServletContext().getRequestDispatcher("/admin/userView.xhtml").forward(request, response);
            
        }
    }
}