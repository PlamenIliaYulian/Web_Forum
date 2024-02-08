package com.PlamenIliaYulian.Web_Forum.controllers.MVC;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserMvcController {


    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession httpSession){
        return httpSession.getAttribute("currentUser")!=null;
    }

    @GetMapping
    public String showAllUsersPage(){
        return null;
    }


    @GetMapping("/{id}")
    public String showSingleUserPage(){
        return null;
    }

    @GetMapping("/{id}/posts")
    public String showSingleUsersPosts(){
        return null;
    }

    @GetMapping("/{id}/comments")
    public String showSingleUserComments(){
        return null;
    }

    @GetMapping("/{id}/edit")
    public String showEditPage(){
        return null;
    }

    @PostMapping("/{id}/edit")
    public String handleEditUser(){
        return null;
    }

    @PostMapping("/{id}/edit/UploadAvatar")
    public String handleEditUserUploadAvatar(){
        return null;
    }

    @PostMapping("/{id}/edit/RemoveAvatar")
    public String handleEditUserRemoveAvatar(){
        return null;
    }

    @PostMapping("/{id}/edit/AddPhoneNumber")
    public String handleEditUserAddPhoneNumber(){
        return null;
    }

    @GetMapping("/{id}/edit/delete")
    public String showDeleteConfirmationPage(){
        return null;
    }

    @PostMapping("/{id}/edit/delete")
    public String handleDeleteUser(){
        return null;
    }

    @GetMapping("/{id}/administrative-changes")
    public String showAdminChangesPage(){
        return null;
    }

    @PostMapping("/{id}/administrative-changes")
    public String handleAdminChanges(){
        return null;
    }



}
