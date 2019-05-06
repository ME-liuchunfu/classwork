package classwork;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 查找比对文件
 * @author hp
 *
 */
public class SearchFile {
	
	/**
	 * 学生信息
	 */
	private List<String> list;
	
	/**
	 * 学号信息
	 */
	private List<String> xh;
	
	/**
	 * 姓名信息
	 */
	private List<String> xm;
	
	/**
	 * 提交作业的文件
	 */
	private List<String> zywj;
	
	/**
	 * 提交作业的文件夹
	 */
	private List<String> zywjJia;
	
	/**
	 * 已交作业
	 */
	private StringBuffer yijiaoZY;
	
	/**
	 * 未交作业
	 */
	private StringBuffer weijiaoZY;
	
	/**
	 * 学科名称
	 */
	private String xuekeM = "";
	
	/**
	 * 重命名类型
	 */
	private String type = "bj_xh_xm_xk";
	
	/**
	 * 班级信息
	 */
	private String banji;
	
	/**
	 * 检索作业目录，原文件目录
	 */
	private String jiesuoML;

	public String getBanji() {
		return banji;
	}

	public void setBanji(String banji) {
		this.banji = banji;
	}

	public String getJiesuoML() {
		return jiesuoML;
	}

	public void setJiesuoML(String jiesuoML) {
		this.jiesuoML = jiesuoML;
	}

	public void setXuekeM(String xuekeM){
		this.xuekeM = xuekeM;
	}
	
	public String getXuekeM(){
		return this.xuekeM;
	}
	
	public SearchFile(String path){
		list = new ArrayList<String>();
		zywj = new ArrayList<String>();
		zywjJia = new ArrayList<String>();
		this.jiesuoML = path;
		try{
			initData();			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 重命名文件
	 * @param path 文件夹目录
	 * @return returnCode -1,路径不存在，0成功
	 */
	public int save2newFile(String path){
		if(path == null || "".equals(path)){
			return -1;
		}
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		//System.out.println(list.toString());
		//System.out.println(zywj.toString());
		// 重新格式化学生信息
		this.initSplitMsg();
		this.jiexiProperties();
		if(zywj != null && zywj.size() > 0
				&& list != null && list.size() > 0){
			for(int i = 0; i < zywj.size(); i++){
				for(int j = 0; j < xm.size(); j++){
					String zywjm = zywj.get(i);
					String xhn = xh.get(j);
					String xmm = xm.get(j);
					if(zywjm.indexOf(xmm) != -1){
						String type = this.findLastFileType(zywjm);
						if(type != null){
							String newFileName = this.getNewFileName(this.banji, xhn, xmm, this.xuekeM);
							String newName = newFileName + type;
							int code = this.saveReNameFile(zywjm, path, newName);
							if(code == 0){
								xh.remove(j);
								xm.remove(j);
							}
						}
					}
				}
			}
		}
		return 0;
	}
	
	/**
	 * 解析重命名格式
	 * @param path
	 */
	private void jiexiProperties(){
		try {
			String path = this.jiesuoML + "/" + Constant.PROPERTIES_NAME; 
			File file = new File(path);
			FileInputStream fis = new FileInputStream(file);
			Properties pt = new Properties();
			pt.load(fis);
			String type = new String(pt.getProperty(Constant.TYPE).trim().getBytes(Constant.UTF_8), Constant.UTF_8);
			if(type != null && !"".equals(type)){
				type = type.toLowerCase();
				this.type = type;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 获取新的文件名
	 * @param banji 班级名称
	 * @param xuehao 学号
	 * @param xingming 姓名
	 * @param xueke 学科
	 * @return
	 */
	private String getNewFileName(String banji, String xuehao, String xingming, String xueke){
		String type = this.type;
		int bj = type.indexOf(Constant.BJ);
		int xh = type.indexOf(Constant.XH);
		int xh2 = type.indexOf(Constant.XH2);
		int xh3 = type.indexOf(Constant.XH3);
		int xm = type.indexOf(Constant.XM);
		int xk = type.indexOf(Constant.XK);
		if(bj != -1){
			type = type.replace(Constant.BJ, banji);
		}
		if(xh != -1){
			type = type.replace(Constant.XH, xuehao);
		}
		if(xh3 != -1){
			String xuehao3 = xuehao.substring(xuehao.length() - 3);
			type = type.replace(Constant.XH3, xuehao3);
		}
		if(xh2 != -1){
			String xuehao2 = xuehao.substring(xuehao.length() - 2);
			type = type.replace(Constant.XH2, xuehao2);
		}
		if(xm != -1){
			type = type.replace(Constant.XM, xingming);
		}
		if(xk != -1){
			type = type.replace(Constant.XK, xueke);
		}
		return type;
	}
	/**
	 * 根据文件路径获取文件类型
	 * @param path
	 * @return
	 */
	private String findLastFileType(String path){
		if(path == null || "".equals(path)){
			return null;
		}
		int beginIndex = path.lastIndexOf(".");
		String type = path.substring(beginIndex);
		return type;
	}
	
	/**
	 * 保存重命名文件
	 * @param oldFile 旧文件绝对路径
	 * @param newFile 新文件绝对路径
	 * @return returnCode -1,失败了， 0 成功
	 * @throws Exception 
	 */
	private int saveReNameFile(String oldFile, String newFile,String fileName) {
		oldFile = oldFile.replaceAll("/+", "/").replaceAll("\\+", "/");
		newFile = newFile.replaceAll("/+", "/").replaceAll("\\+", "/");
		File old = new File(oldFile);
		if(!old.exists()){
			return -1;
		}
		File newFilePath = new File(newFile);
		if(!newFilePath.exists()){
			newFilePath.mkdirs();
		}
		String filePath = newFile + "/" + fileName;
		filePath = filePath.replaceAll("/+", "/").replaceAll("\\+", "/");
		File newf = new File(filePath);
		FileInputStream fis = null;
		FileOutputStream fos = null;
		
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
 		try{
 			System.out.println("正在为你重命名文件：" + filePath + "\t请稍等...");
			fis = new FileInputStream(old);
			bis = new BufferedInputStream(fis);
			fos = new FileOutputStream(newf);
			bos = new BufferedOutputStream(fos);
			byte[] read = new byte[10240];
			int len;
			while (( len = bis.read(read)) != -1) {
				bos.write(read, 0, len);
			}
			System.out.println("已处理完成。");
		}catch(Exception e){
			System.out.println("处理遇到问题。");
			e.printStackTrace();
			return -1;
		}finally {
			try {
				if(bos != null){
					bos.close();
				}
				if(fos != null){
					fos.close();
				}
				if(bis != null){
					bis.close();
				}
				if(fis != null){
					fis.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return 0;
	}
	
	
	/**
	 * 保存记录到文件
	 * @param filepath 文件路径：如F:/temp/abc
	 * @param fileName 文件名：如：1.txt
	 * @param stringData 文件数据，待写入的数据
	 */
	public void saveData2File(String filepath,String fileName, String stringData){
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		//BufferedWriter
		filepath = filepath.replaceAll("/+", "/").replaceAll("\\+", "/");
		File fileP = new File(filepath);
		if (!fileP.exists()) {
			fileP.mkdirs();
		}
		File file = new File(filepath + "/" + fileName);
		try {
			fos = new FileOutputStream(file);
			osw = new OutputStreamWriter(fos);
			bw = new BufferedWriter(osw);
			bw.write(stringData);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(bw != null){
					bw.close();
				}
				if(osw != null){
					osw.close();
				}
				if(fos != null){
					fos.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	/**
	 * 开始查找交作业的名单，
	 * 数据为目录文件，仅仅是目录
	 * @param absolutePath 查找文件的绝对路径目录
	 * return code = -1,文件路劲不对或者不存在
	 * code = 0,成功
	 */
	public int findFile(String absolutePath){
		if(absolutePath == null && "".equals(absolutePath)){
			System.out.println("文件路径不对或者不存在...");
			return -1;
		}
		// 文件路劲父级目录
		File file = new File(absolutePath);
		if(!file.exists()){
			System.out.println("文件路径不存在...");
			return -1;
		}
		File[] listFiles = file.listFiles();
		for(File f : listFiles){
			// 1、是文件
			if(!f.isHidden() && f.isFile()){				
				System.out.println(f.getAbsolutePath());
				zywj.add(f.getAbsolutePath());
			}
			// 2、是文件夹
			if(!f.isHidden() && f.isDirectory()){
				zywjJia.add(f.getAbsolutePath());
			}
		}
		return 0;
	}

	/**
	 * 文件操作
	 * 对比谁交了作业，谁没交作业
	 * @param zywj 已交作业的文件
	 * @param xm 学生名单
	 */
	public void invoke(){
		this.initSplitMsg();
		yijiaoZY = new StringBuffer();
		weijiaoZY = new StringBuffer();
		if(zywj != null && xm != null
				&& zywj.size() > 0 && xm.size() > 0){
			String banji = this.banji != null ? this.banji : "";
			String xuekeM = this.xuekeM != null ? this.xuekeM : "";
			for (int i = 0; i < zywj.size(); i++) {
				for(int j = 0; j < xm.size(); j++){
					String zyming = zywj.get(i);
					String xsming = xm.get(j);
					String xhn = xh.get(j);
					if(zyming.indexOf(xsming) != -1){
						yijiaoZY.append(banji + xhn + "_" + xsming + "于时间：" + DateUtils.getyyyy_MM_dd_HH_mm_ss() + "提交过" + xuekeM + "作业\r\n");
						xm.remove(j);
						xh.remove(j);
						break;
					}
				}
			}
			weijiaoZY.append("截至时间：" + DateUtils.getyyyy_MM_dd_HH_mm_ss() + "," + banji + xuekeM + "剩余未交作业名单：\r\n" + xm.toString());
		}
	};
	
	/**
	 * 文件夹操作
	 * 对比谁交了作业，谁没交作业
	 * @param zywj 已交作业的文件
	 * @param xm 学生名单
	 */
	public void invokeByFolder(){
		// 重新初始化学生信息，学号姓名
		this.initSplitMsg();
		yijiaoZY = new StringBuffer();
		weijiaoZY = new StringBuffer();
		if(zywjJia != null && xm != null
				&& zywjJia.size() > 0 && xm.size() > 0){
			String banji = this.banji != null ? this.banji : "";
			String xuekeM = this.xuekeM != null ? this.xuekeM : "";
			for (int i = 0; i < zywjJia.size(); i++) {
				for(int j = 0; j < xm.size(); j++){
					String zyming = zywjJia.get(i);
					String xsming = xm.get(j);
					String xhn = xh.get(j);
					if(zyming.indexOf(xsming) != -1){
						yijiaoZY.append(banji + xhn + "_" + xsming + "于时间：" + DateUtils.getyyyy_MM_dd_HH_mm_ss() + "提交过" + xuekeM + "作业\r\n");
						xm.remove(j);
						xh.remove(j);
						break;
					}
				}
			}
			weijiaoZY.append("截至时间：" + DateUtils.getyyyy_MM_dd_HH_mm_ss() + "," + banji + xuekeM + "剩余未交作业名单：\r\n" + xm.toString());
		}
	};

	/**
	 * 重命名文件夹
	 * @param olderPath 就文件夹
	 * @param newPath 新文件夹父级目录
	 * @param newName 新文件夹名
	 * @return returnCode -1重命名失败了， 0成功
	 */
	private int rename2NewFolder(String olderPath, String newPath, String newName){
		if(olderPath == null || "".equals(olderPath)){
			System.out.println("原文件路径有误");
			return -1;
		}
		if(newPath == null || "".equals(newPath)){
			System.out.println("重命名的文件路径有误");
			return -1;
		}
		if(newName == null || "".equals(newName)){
			System.out.println("重命名的文件名有误");
			return -1;
		}
		olderPath = olderPath.replaceAll("/+", "/").replaceAll("\\+", "/");
		newPath = newPath + "/" + newName;
		newPath = newPath.replaceAll("/+", "/").replaceAll("\\+", "/");
		File old = new File(olderPath);
		File newf = new File(newPath);
		boolean flag = old.renameTo(newf);
		return flag ? 0 : -1;
	}

	/**
	 * 重命名文件夹，保存
	 * @param outPath
	 * @return
	 */
	public int save2ReNameFolder(String outPath){
		if(outPath == null || "".equals(outPath)){
			return -1;
		}
		File file = new File(outPath);
		if(!file.exists()){
			file.mkdirs();
		}
		//System.out.println(list.toString());
		//System.out.println(zywj.toString());
		// 重新格式化学生信息
		this.initSplitMsg();
		this.jiexiProperties();
		if(zywjJia != null && zywjJia.size() > 0
				&& list != null && list.size() > 0){
			for(int i = 0; i < zywjJia.size(); i++){
				for(int j = 0; j < xm.size(); j++){
					String zywjm = zywjJia.get(i);
					String xhn = xh.get(j);
					String xmm = xm.get(j);
					if(zywjm.indexOf(xmm) != -1){
						String newFileName = this.getNewFileName(this.banji, xhn, xmm, this.xuekeM);
						int code = this.rename2NewFolder(zywjm, outPath, newFileName);
						if(code == 0){
							xh.remove(j);
							xm.remove(j);
						}
					}
				}
			}
		}
		return 0;
	}
	
	/**
	 * 初始化数据
	 * @throws IOException
	 */
	private void initData() throws IOException {
		String path = this.jiesuoML + "/" + Constant.STUDENT_PROPERTIES;
		path = path.replace("//+", "/").replaceAll("\\+", "/");
		File file = new File(path);
		if(!file.exists()){
			System.out.println("未找到学生配置信息文件：" + path);
			return;
		}
		//InputStream is = SearchFile.class.getClassLoader().getResourceAsStream("classwork/pz.txt");
		FileInputStream is = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(is, Constant.UTF_8);
		BufferedReader bfr = new BufferedReader(isr);
		String buf = null;
		while ((buf = bfr.readLine()) != null) {
			if(!"".equals(buf)){				
				list.add(buf);
			}
		}
		bfr.close();
		isr.close();
		is.close();
		// 初始化配置文件信息格式化
		initSplitMsg();
		System.out.println("找到信息：" + list.toString());
		System.out.println("学号：" + xh.toString());
		System.out.println("姓名：" + xm.toString());
	}

	/**
	 * 初始化配置文件信息格式化，格式化学生信息
	 */
	private void initSplitMsg() {
		xh = new ArrayList<String>();
		xm = new ArrayList<String>();
		if(list.size() > 0){
			for(int i=0; i< list.size(); i++){
				String str = list.get(i);
				String[] split = str.split("=");
				xh.add(split[0].trim());
				xm.add(split[1].trim());
			}
		}
	}
	
	/**
	 * 提交作业文件夹
	 * @return
	 */
	public List<String> getZywnJia(){
		return this.zywjJia;
	}
	
	/**
	 * 获取学号
	 * @return
	 */
	public List<String> getXH(){
		return this.xh;
	}
	
	/**
	 * 得到姓名
	 * @return
	 */
	public List<String> getXM(){
		return this.xm;
	}
	
	/**
	 * 获得学生全部信息
	 * @return
	 */
	public List<String> getXSXX(){
		return this.list;
	}
	
	/**
	 * 得到提交的作业文件
	 * @return
	 */
	public List<String> getZYWJ(){
		return this.zywj;
	}
	
	/**
	 * 获取已交作业名单
	 * @return
	 */
	public String getYijiaoZY(){
		return yijiaoZY != null ? yijiaoZY.toString() : "";
	}
	
	/**
	 * 获取未交作业名单
	 * @return
	 */
	public String getWeijiaoZY(){
		return weijiaoZY != null ? weijiaoZY.toString() : "";
	}
	
}
