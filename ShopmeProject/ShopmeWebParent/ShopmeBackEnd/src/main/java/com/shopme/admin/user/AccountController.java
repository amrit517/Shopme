package com.shopme.admin.user;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.common.entity.User;

@Controller
public class AccountController {
	
	@Autowired
	private UserService service;
	
	@GetMapping("/account")
	public String viewDetails(@AuthenticationPrincipal ShopmeUserDetails loggedUser,Model model)
	{
		String email = loggedUser.getUsername();
		User user = service.getUserByEmail(email);
		model.addAttribute("user",user);
		return "account_form";
	}
	
	@PostMapping("/account/update")
	public String saveDetails(User user,RedirectAttributes redirectAttributes,
			@AuthenticationPrincipal ShopmeUserDetails loggedUser,		
			@RequestParam("image") MultipartFile multiPartFile) throws IOException {
		if(!multiPartFile.isEmpty()) {
			String fileName = multiPartFile.getOriginalFilename();
			user.setPhotos(fileName);
			User savedUser = service.updateAccount(user);
			
			String uplaodDir  = "user-photos/"+ savedUser.getId();
			FileUploadUtil.cleanDir(uplaodDir);
			FileUploadUtil.saveFile(uplaodDir, fileName, multiPartFile);
		}
		else {
			if(user.getPhotos().isEmpty()) user.setPhotos(null);
			service.updateAccount(user);
		}
		
		loggedUser.setFirstName(user.getFirstName());
		loggedUser.setLastName(user.getLastName());
		
		
		redirectAttributes.addFlashAttribute("message","Your Account Details has been updated successfully");
		return "redirect:/account";
	}
}
