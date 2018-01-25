package rest.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;

import rest.mybatis.model.eBillModel.FileAddress;

@Component
public class FileDubboCustomerService {

	@Reference(version = "1.0.0",check=false)
	private FileDubboService fileDubboService;
	
	public String testDubbo(){
		return fileDubboService.testDubbo();
	}
	/**
	 * 
	 * @param comNum
	 * @return
	 */
	public List<String> obtainFilePath(String comNum){
		
		List<String> fps = fileDubboService.obtainFilePathByComNum(comNum);
		
		return fps;
	}
	
	/**
	 * 
	 * @param fd
	 * @return
	 */
	public String saveFileAddress(FileAddress fd){
		
		int result = fileDubboService.saveFileAddress(fd);
		
		if(result >0){
			return "success";
		}else{
			return "error";
		}
	}
}
