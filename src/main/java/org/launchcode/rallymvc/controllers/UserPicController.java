package org.launchcode.rallymvc.controllers;

import org.launchcode.rallymvc.models.User;
import org.launchcode.rallymvc.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Controller
public class UserPicController {

    @Autowired
    private UserDao userDao;

    //set upload path
    public static String uploadDirectory = System.getProperty("user.dir")+"/src/main/resources/static/uploads";

    @RequestMapping(value = "upload-pic", method = RequestMethod.POST)
    public String processUploadPic(@RequestParam("pic") MultipartFile pic, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        try{
            String ext = pic.getOriginalFilename().substring(pic.getOriginalFilename().lastIndexOf(".") + 1);

            Path fileNameAndPath = Paths.get(uploadDirectory,pic.getOriginalFilename());

            user.setPic(pic.getOriginalFilename());
            userDao.save(user);

            Files.write(fileNameAndPath, pic.getBytes());
         } catch (IOException e) {
            return "redirect:/dashboard";
        }
        return "redirect:/dashboard";
    }

}
