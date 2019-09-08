package org.launchcode.rallymvc.controllers;

import com.mysql.jdbc.util.Base64Decoder;
import org.apache.tomcat.jni.File;
import org.apache.tomcat.util.codec.binary.Base64;
import org.launchcode.rallymvc.models.User;
import org.launchcode.rallymvc.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class UserPicController {

    @Autowired
    private UserDao userDao;

    //set upload path
    public static String uploadDirectory = System.getProperty("user.dir")+"/src/main/resources/static/uploads";

//    @RequestMapping(value = "upload-pic", method = RequestMethod.POST)
//    public String processUploadPic(@RequestParam("pic") MultipartFile pic, HttpServletRequest request) {
//        HttpSession session = request.getSession();
//        User user = (User) session.getAttribute("user");
//
//        try{
//            String ext = pic.getOriginalFilename().substring(pic.getOriginalFilename().lastIndexOf(".") + 1);
//
//            Path fileNameAndPath = Paths.get(uploadDirectory,pic.getOriginalFilename());
//
//            user.setPic(pic.getOriginalFilename());
//            userDao.save(user);
//
//            Files.write(fileNameAndPath, pic.getBytes());
//        } catch (IOException e) {
//            return "redirect:/dashboard";
//        }
//        return "redirect:/dashboard";
//    }

    @RequestMapping(value = "upload-pic", method = RequestMethod.POST)
    public String processUploadPic(@RequestParam int id, @RequestParam("baseImage") String baseImage,
                                   HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Integer fid = id;
        String uid = fid.toString();

        try{
            String baseImageSub = baseImage.substring(22); //get img data after comma
            BufferedImage image = null;
            byte[] imgByte;
            BASE64Decoder decoder = new BASE64Decoder();
            imgByte = decoder.decodeBuffer(baseImageSub);
            ByteArrayInputStream bis = new ByteArrayInputStream(imgByte);
            image = ImageIO.read(bis);
            bis.close();

            System.out.println("String baseImage: " + baseImage);
            System.out.println("Substring baseImage: " + baseImageSub);
            System.out.println("byte[] img: " + imgByte);
            System.out.println("Image " + image);

            Path fileNameAndPath = Paths.get(uploadDirectory, uid + ".png");
            user.setPic(uid + ".png");
            userDao.save(user);

            Files.write(fileNameAndPath, imgByte);

        } catch (IOException e) {
            return "redirect:/dashboard";
        }
        return "redirect:/profile";
    }

}
