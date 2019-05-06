package classwork;

import java.io.IOException;
import java.util.Scanner;

/**
 * 启动程序入口
 * @author hp
 *
 */
public class Application {
	
	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		execute(scanner);
		System.out.println("已完成,欢迎下次使用");
		//new SearchFile();
	}

	private static void execute(Scanner scanner) {
		SearchFile searchFile = null;
		String nu = Constant.SELECT_TYPE_1;
		while(true){
			System.out.println("请输入检索目录：如：C:/, D:/, D:/abc等：");
			String line = scanner.nextLine().trim();
			searchFile = new SearchFile(line);
			System.out.println("请输入检查的学科名：");
			String xuekeM = scanner.nextLine().trim();
			searchFile.setXuekeM(xuekeM);
			System.out.println("请输入班级名称：");
			String banji = scanner.nextLine().trim();
			searchFile.setBanji(banji);
			System.out.println("正在检索文件路径：" + line);
			int i = searchFile.findFile(line);
			if(i != -1){
				System.out.println("请选择当前操作的是文件夹（目录）还是文件：\r\n");
				System.out.println("1、文件, 2、文件夹\r\n");
				nu = scanner.nextLine().trim();
				nu = nu != null &&!"".equals(nu)? nu : Constant.SELECT_TYPE_1;
				// 操作是1
				if(Constant.SELECT_TYPE_1.equals(nu)){
					select_1(searchFile, line);
				}else if(Constant.SELECT_TYPE_2.equals(nu)){
					// 操作是2
					select_2(searchFile, line);
				}else {
					// 默认操作
					select_1(searchFile, line);
				}
				break;
			}else {
				System.out.println("您输入的文件夹路径有误：如：C:/, D:/, D:/abc等");
			}
		}
		System.out.println("是否需要重命名(y/n)?");
		String rename = scanner.nextLine().trim();
		rename = rename.toLowerCase();
		if("y".equals(rename) || "yes".equals(rename)){
			while (true){
				System.out.println("使用前请在原数据文件夹新建一个spring-type.properties文件，用于解析格式");
				System.out.println("请输入重命名后的保存路径：");
				String outPath = scanner.nextLine().trim();
				System.out.println("正在处理文件重命名...");
				int code = -1;
				if(Constant.SELECT_TYPE_1.equals(nu)){
					// 文件操作
					code = searchFile.save2newFile(outPath);
				}else if (Constant.SELECT_TYPE_2.equals(nu)) {
					// 文件夹操作
					code = searchFile.save2ReNameFolder(outPath);
				}else{
					code = searchFile.save2newFile(outPath);
				}
				if( code != -1){
					break;
				}else {
					System.out.println("您输入的文件保存路径有误，请重新输入：如：C:/, D:/, D:/abc等");
				}
			}
		}
	}

	/**
	 * 文件夹操作
	 * @param searchFile
	 * @param line
	 */
	private static void select_2(SearchFile searchFile, String line) {
		searchFile.invokeByFolder();
		String weijiaoZY2 = searchFile.getWeijiaoZY();
		String yijiaoZY2 = searchFile.getYijiaoZY();
		searchFile.saveData2File(line, DateUtils.getyyyy_MM_dd_HH_mm_ss1() + "已交作业.txt", yijiaoZY2);
		searchFile.saveData2File(line, DateUtils.getyyyy_MM_dd_HH_mm_ss1() + "未交作业.txt", weijiaoZY2);
		System.out.println("数据文件已保存至目录文件夹");
	}

	/**
	 * 文件操作
	 * @param searchFile
	 * @param line
	 */
	private static void select_1(SearchFile searchFile, String line) {
		searchFile.invoke();
		String weijiaoZY2 = searchFile.getWeijiaoZY();
		String yijiaoZY2 = searchFile.getYijiaoZY();
		searchFile.saveData2File(line, DateUtils.getyyyy_MM_dd_HH_mm_ss1() + "已交作业.txt", yijiaoZY2);
		searchFile.saveData2File(line, DateUtils.getyyyy_MM_dd_HH_mm_ss1() + "未交作业.txt", weijiaoZY2);
		System.out.println("数据文件已保存至目录文件夹");
	}

}
