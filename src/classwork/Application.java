package classwork;

import java.io.IOException;
import java.util.List;
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
				List<String> xm2 = searchFile.getXM();
				List<String> zywj2 = searchFile.getZYWJ();
				searchFile.invoke(zywj2, xm2);
				String weijiaoZY2 = searchFile.getWeijiaoZY();
				String yijiaoZY2 = searchFile.getYijiaoZY();
				searchFile.saveData2File(line, DateUtils.getyyyy_MM_dd_HH_mm_ss1() + "已交作业.txt", yijiaoZY2);
				searchFile.saveData2File(line, DateUtils.getyyyy_MM_dd_HH_mm_ss1() + "未交作业.txt", weijiaoZY2);
				System.out.println("数据文件已保存至目录文件夹");
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
				int code = searchFile.save2newFile(outPath);
				if( code != -1){
					break;
				}else {
					System.out.println("您输入的文件保存路径有误，请重新输入：如：C:/, D:/, D:/abc等");
				}
			}
		}
	}

}
