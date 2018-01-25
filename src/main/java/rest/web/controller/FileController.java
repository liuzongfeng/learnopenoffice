package rest.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import rest.mybatis.model.eBillModel.FileAddress;
import rest.param.ReturnResponse;
import rest.service.FileDubboCustomerService;
import rest.utils.FileUtils;

/**
 *
 * @author yudian-it
 * @date 2017/12/1
 */
@RestController
public class FileController {
    @Value("${file.dir}")
    String fileDir;
    @Autowired
    FileUtils fileUtils;
    
    @Autowired
	private FileDubboCustomerService fileDubboCustomerService;
    //String demoDir = "uploadfile";
    //String demoPath = demoDir + File.separator;

    @RequestMapping(value = "fileUpload", method = RequestMethod.POST)
    public String fileUpload(@RequestParam("file") MultipartFile file,
                             HttpServletRequest req) throws JsonProcessingException {
        String fileName = file.getOriginalFilename();
        // 判断该文件类型是否有上传过，如果上传过则提示不允许再次上传
       /* HttpSession session = req.getSession();
        SecurityContext context = (SecurityContext)session.getAttribute("SPRING_SECURITY_CONTEXT");
		User user = (User)context.getAuthentication().getPrincipal();*/
		String comNum = req.getParameter("comNum");
		//String demoPath = demoDir+comNum + File.separator;
		String demoPath = comNum + File.separator;
        if (existsTypeFile(fileName,demoPath)) {
            //return new ObjectMapper().writeValueAsString(new ReturnResponse<String>(1, "每一种类型只可以上传一个文件，请先删除原有文件再次上传", null));
        }
      //保存文件路径，为展示使用
        FileAddress fd = new FileAddress();
        fd.setComNum(comNum);
        fd.setFilePath(comNum);
        fd.setIsLive(1);
        fd.setOperateDate(new Date());
        
        fileDubboCustomerService.saveFileAddress(fd);
        //获取当前线程，作为下级子目录
        File outFile = new File(fileDir + demoPath);
        if (!outFile.exists()) {
            outFile.mkdirs();
        }
        try(InputStream in = file.getInputStream();
            OutputStream ot = new FileOutputStream(fileDir + demoPath + fileName)){
            byte[] buffer = new byte[1024];
            int len;
            while ((-1 != (len = in.read(buffer)))) {
                ot.write(buffer, 0, len);
            }
            
            
            return new ObjectMapper().writeValueAsString(new ReturnResponse<String>(0, "SUCCESS", null));
        } catch (IOException e) {
            e.printStackTrace();
            return new ObjectMapper().writeValueAsString(new ReturnResponse<String>(1, "FAILURE", null));
        }
    }

    @RequestMapping(value = "deleteFile", method = RequestMethod.GET)
    @CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = { "x-auth-token", "x-requested-with" })
    public String deleteFile(String fileName,HttpServletRequest req) throws JsonProcessingException {
        if (fileName.contains("/")) {
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
        }
        String comNum = req.getParameter("comNum");
		//String demoPath = demoDir+comNum + File.separator;
		String demoPath = comNum + File.separator;
        File file = new File(fileDir + demoPath + fileName);
        if (file.exists()) {
            file.delete();
        }
        return new ObjectMapper().writeValueAsString(new ReturnResponse<String>(0, "SUCCESS", null));
    }

    @RequestMapping(value = "listFiles", method = RequestMethod.GET)
    public String getFiles(HttpServletRequest req) throws JsonProcessingException {
    	//
    	String comNum = req.getParameter("comNum");
		//String demoPath = demoDir+comNum + File.separator;
    	//根据工号查询路径
    	List<String> fps = fileDubboCustomerService.obtainFilePath(comNum);
    	
    	List<Map<String, String>> list = Lists.newArrayList();
    	for(String comNum1 : fps){
    		
    		String demoPath = comNum1 + File.separator;
    		File file = new File(fileDir + demoPath);
	        if (file.exists()) {
	            Arrays.stream(file.listFiles()).forEach(file1 -> list.add(ImmutableMap.of("fileName", comNum1 + "/" + file1.getName())));
	        }
    	}
    	//
        return new ObjectMapper().writeValueAsString(list);
    }

    private String getFileName(String name) {
        String suffix = name.substring(name.lastIndexOf("."));
        String nameNoSuffix = name.substring(0, name.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        return uuid + "-" + nameNoSuffix + suffix;
    }

    /**
     * 是否存在该类型的文件
     * @return
     * @param fileName
     */
   /* private boolean existsTypeFile(String fileName) {
        boolean result = false;
        String suffix = fileUtils.getSuffixFromFileName(fileName);
        File file = new File(fileDir + demoPath);
        if (file.exists()) {
            for(File file1 : file.listFiles()){
                String existsFileSuffix = fileUtils.getSuffixFromFileName(file1.getName());
                if (suffix.equals(existsFileSuffix)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }*/
    /**
     * 是否存在该类型的文件
     * @return
     * @param fileName
     */
    private boolean existsTypeFile(String fileName,String demoPath) {
        boolean result = false;
        String suffix = fileUtils.getSuffixFromFileName(fileName);
        File file = new File(fileDir + demoPath);
        if (file.exists()) {
            for(File file1 : file.listFiles()){
                String existsFileSuffix = fileUtils.getSuffixFromFileName(file1.getName());
                if (suffix.equals(existsFileSuffix)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}
