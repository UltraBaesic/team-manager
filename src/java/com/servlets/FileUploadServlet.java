/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.servlets;

import com.classes.ResourceManager;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import com.engine.*;

/**
 *
 * @author Stephen
 */
@WebServlet(name = "FileUploadServlet", urlPatterns = {"/FileUploadServlet"})
public class FileUploadServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession(true);
            String contentType = request.getContentType();
            if ((contentType.indexOf("multipart/form-data") >= 0)) {
                String address = FilePathManager.getInstance().getTeamManagerResourcesAddress();
                File file;
                int maxFileSize = 5000 * 1024;
                int maxMemSize = 5000 * 1024;
                String filePath = address;
                org.apache.commons.fileupload.disk.DiskFileItemFactory factory = new org.apache.commons.fileupload.disk.DiskFileItemFactory();
                factory.setSizeThreshold(maxMemSize);
                factory.setRepository(new File(address));
                org.apache.commons.fileupload.servlet.ServletFileUpload upload = new org.apache.commons.fileupload.servlet.ServletFileUpload(factory);
                upload.setSizeMax(maxFileSize);
                
                try {
                    // Parse the request to get file items.
                    List<FileItem> fileItems = upload.parseRequest(request);

                    // Process the uploaded file items
                    Iterator i = fileItems.iterator();
                    String teamid = "0";
                    String extension = "";
                    while (i.hasNext()) {
                        FileItem fi = (FileItem) i.next();
                        if (!fi.isFormField()) {
//                            int cdid = Integer.parseInt(cardid);
                            String newfilename = fi.getName();
//                            newfilename = newfilename.replaceAll(" ", "");
                            //Delete existing files if any
                            ResourceManager.deleteFile(newfilename);

                            // GET THE UPLOADED FILE PARAMETERS
                            String fileName = newfilename;
                            int ni = fileName.lastIndexOf('.');
                            if (ni > 0) {
                                extension = fileName.substring(ni + 1);
                                extension = extension.toLowerCase();
                            }
//                            String original = fi.getName(); //PUT YOUR OWN FILENAME HERE

                            // WRITE THE FILE TO A FOLDER ON HARD DISK
                            if (fileName.lastIndexOf("\\") >= 0) {
                                file = new File(filePath
                                        + fileName.substring(fileName.lastIndexOf("\\")));
                            } else {
                                file = new File(filePath
                                        + fileName.substring(fileName.lastIndexOf("\\") + 1));
                            }
                            fi.write(file);
                            int id = Integer.parseInt(teamid);
                            String temp = "" + session.getAttribute("Id");
                            int userid = Integer.parseInt(temp);
                            ResourceManager.addResource(fileName, id, userid, extension);
                        } else {
                            teamid = fi.getString();
                        }
                    }
                } catch (Exception ex) {
                    String problem = ex.getMessage();
                    System.out.println(problem);
                }
            }
            response.sendRedirect(request.getHeader("referer"));
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

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
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
