package rest.service;

import java.util.List;

import rest.mybatis.model.eBillModel.FileAddress;

/**
 * @TODO 处理文件，通过dubbo将接口方法发布
 * @author 刘宗峰
 *
 */
public interface FileDubboService {

	//1保存文件上传的相关信息：
	int saveFileAddress(FileAddress fd);
	
	String testDubbo();
	
	//2根据工号查询路径
	List<String> obtainFilePathByComNum(String comNum);
}
