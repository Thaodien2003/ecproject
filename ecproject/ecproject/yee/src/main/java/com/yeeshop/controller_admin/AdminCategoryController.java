package com.yeeshop.controller_admin;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yeeshop.dao.ICategoryDAO;
import com.yeeshop.entity.Category;
import com.yeeshop.entity.User;

@Controller
public class AdminCategoryController {
    
    @Autowired 
    ICategoryDAO categoryDAO;

    @Autowired
    HttpSession session;
    @RequestMapping(value="/admin/category-manage", method = RequestMethod.GET)
    public String categoryManagement(Model model){
        // Category category= new Category();
        // check session
        User user= (User) session.getAttribute("user");
        if(user ==null || !user.getAdmin()){
            System.out.println("nulluser");
            return "redirect:/admin/login" ;
        }
        // find all category
        List<Category> categories= categoryDAO.findAll(); 
        model.addAttribute("cates", categories);
        return "admin/category/category-list";
    }

    // add new category
    @RequestMapping(value = "/admin/category/add", method = RequestMethod.GET)
    public String categoryAdd(){
        User user= (User) session.getAttribute("user");
        if(user ==null || !user.getAdmin()){
            System.out.println("nulluser");
            return "redirect:/admin/login" ;
        }
        return "admin/category/category-add";
    }

    @RequestMapping(value = "/admin/category/add", method = RequestMethod.POST)
    public String categoryAdd(RedirectAttributes model,Model model2, @ModelAttribute("category") Category cate,BindingResult errorResult){
        User user= (User) session.getAttribute("user");
        if(user ==null || !user.getAdmin()){
            System.out.println("nulluser");
            return "redirect:/admin/login" ;
        }
        if(!categoryDAO.findExistCate(cate)){
            categoryDAO.create(cate);
            model.addFlashAttribute("message", "Yee Shop Notification: Add new category success");
        }
        else{
            model2.addAttribute("message", "Category is existed !!!");
            return "admin/category/category-add";
        }
        // categoryDAO.create(cate);
		
        return "redirect:/admin/category-manage";
    }
    // update category
    @RequestMapping("/admin/category/edit/{id}")
	public String edit(Model model, @PathVariable("id") Integer id) {
		Category category = categoryDAO.findById(id);
		model.addAttribute("cate", category);
		model.addAttribute("list", categoryDAO.findAll());
		return "admin/category-manage";
	}
    @RequestMapping(value = "/admin/category/update/{id}", method = RequestMethod.GET)
    public String categoryUpdate(Model model, @PathVariable("id") Integer id){
        User user= (User) session.getAttribute("user");
        if(user ==null || !user.getAdmin()){
            System.out.println("nulluser");
            return "redirect:/admin/login" ;
        }
        Category category=categoryDAO.findById(id);
        model.addAttribute("cate", category);
        return "admin/category/category-update";
    }
    @RequestMapping(value = "/admin/category/update/{id}", method = RequestMethod.POST)
    public String categoryUpdate(RedirectAttributes model,Model model2,@PathVariable("id") Integer id,@ModelAttribute("category") Category category,BindingResult errorResult){
        User user= (User) session.getAttribute("user");
        if(user ==null || !user.getAdmin()){
            System.out.println("nulluser");
            return "redirect:/admin/login" ;
        }
        category.setId(id);
        System.out.println("cate: "+categoryDAO.findExistCate(category));
        if(!categoryDAO.findExistCate(category)){
            categoryDAO.update(category);
            model.addFlashAttribute("message", "Update Done!!!");
        }
        else{
            model.addFlashAttribute("message", "This category is exist !!!");
            return "redirect:/admin/category/update/"+id;
        }
        return "redirect:/admin/category-manage";
    }

    // delete category
    @RequestMapping(value = {"/admin/category/delete","/admin/category/delete/{id}"})
	public String delete(RedirectAttributes model, 
			@RequestParam(value="id", required = false) Integer id1, 
			@PathVariable(value="id", required = false) Integer id2) {
		if(id1 != null) {
			categoryDAO.delete(id1);
		}else {
			categoryDAO.delete(id2);
		}
		
		model.addFlashAttribute("message", "Delete Success !!!");
		return "redirect:/admin/category-manage";
	}
}
