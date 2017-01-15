package com.lee.minDistance;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import com.lee.alg.DJSTL;

/**
 * @ClassName com.lee.mathmodel.MinDistance2
 * @description
 * @author 凌霄
 * @data 2016年11月7日 下午4:14:07
 */
public class MinDistance0 {

	// static String mInputFileUrl = "D://MinDistance//Dis.accdb";
	// static String mOutputFileDir = "D://MinDistance//Output";

	static DecimalFormat df4 = new DecimalFormat("#.0000");

	public static String mOutputFileDir;
	public static String mInputFileUrl;

	static private String newCSVFileName = "";

	static int size = 0;

	// 最大距离
	// static private String maxDis = "";
	// 距离
	// static private String mDis = "";

	// 斑块
	static private ArrayList<String> mX = new ArrayList<String>();

	// 距离矩阵
	static int eachLine[][];

	public static void main(String[] args) {

		mInputFileUrl = args[0];
		mOutputFileDir = args[1];

		try {
			ConnectAccessFile(mInputFileUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static final Properties prop = new Properties();
	static {
		prop.put("charSet", "GBK"); // 这里是解决中文乱码
		prop.put("user", "root");
		prop.put("password", "root");
	}

	private static void ConnectAccessFile(String mDataBasePath) throws Exception {

		String dbur1 = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=" + mDataBasePath;

		Connection conn = DriverManager.getConnection(dbur1, prop);
		Statement stmt = conn.createStatement();
		ResultSet rs;

		// 获取数据行数
		int lineNum;
		rs = stmt.executeQuery("select count(X) from Dis");
		rs.next();
		lineNum = Integer.valueOf(rs.getString(1));
		Window.inTable("共 " + lineNum + " 行数据");
		rs.close();

		// 获取元素个数

		rs = stmt.executeQuery("select count(X)  from (select distinct X from Dis order by X)");
		while (rs.next()) {
			size = rs.getInt(1);
			Window.inTable("共 " + size + " 个元素");
		}

		// 求所有距离的和作为最大距离
		// rs = stmt.executeQuery("select Sum(D) from Dis");
		// while (rs.next()) {
		// maxDis = rs.getString(1);
		// }

		eachLine = new int[size][size];

		int[][] lines = new int[lineNum][3];

		int a = 0;

		rs = stmt.executeQuery("select X, Y, D from Dis");
		while (rs.next()) {
			lines[a][0] = Integer.valueOf(rs.getString(1));
			lines[a][1] = Integer.valueOf(rs.getString(2));
			lines[a][2] = (int) (Double.valueOf(rs.getString(3)) * 10000);
			a++;
		}
		rs.close();
		stmt.close();
		conn.close();

		// 构建对称矩阵
		int x, y;
		for (int i = 0, rows = lines.length; i < rows; ++i) {
			x = (int) (lines[i][0] - 1);
			y = (int) (lines[i][1] - 1);
			eachLine[x][y] = lines[i][2];
		}

		// 0处变换
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				if (eachLine[i][j] == 0)
					eachLine[i][j] = Integer.MAX_VALUE;
			}
		}

		// // 打印出对称矩阵
		// System.out.println("源数据：");
		// for (int i = 0; i < size; ++i) {
		// for (int j = 0; j < size; ++j) {
		// System.out.print(eachLine[i][j] + " ");
		// }
		// System.out.println();
		// }

		// for (String eachX : mX) {
		// Window.inTable("构建第 " + eachX + " 列");
		// for (String eachY : mX) {
		//
		// if (eachX.equalsIgnoreCase(eachY)) {
		// mDis = "0.000";
		// } else {
		// // mDis = maxDis;
		// mDis = "0.000";
		// }
		//
		// rs = stmt.executeQuery(
		// "select D from Dis where X=" + Integer.valueOf(eachX) + " and Y = " +
		// Integer.valueOf(eachY));
		// if (rs.next()) {
		// eachLine[i][j] = Double.valueOf(rs.getString(1));
		// } else {
		// rs.close();
		// rs = stmt.executeQuery("select D from Dis where X=" +
		// Integer.valueOf(eachY) + " and Y = "
		// + Integer.valueOf(eachX));
		// if (rs.next()) {
		// eachLine[i][j] = Double.valueOf(rs.getString(1));
		// } else {
		// eachLine[i][j] = Double.valueOf(mDis);
		// }
		// }
		// j++;
		// }
		// j = 0;
		// i++;
		// }

		Window.inTable("邻接矩阵构建完成");

		// Window.inTable("正在输出模糊关系矩阵");

		// String cont = "";
		// int i = 0;
		// for (double[] js : eachLine) {
		// Window.inTable("正在输出第 " + (i++) + " 行");
		// for (double k : js) {
		// cont += k + ",";
		// }
		// cont += "\n";
		// }

		// newCSVFileName = getSystemTime("_模糊关系阵") + ".csv";
		//
		// createFile(mOutputFileDir, newCSVFileName);
		//
		// writeTxt(mOutputFileDir + "//" + newCSVFileName, cont);
		//
		// Window.inTable("模糊关系矩阵输出完成：" + mOutputFileDir + "//" +
		// newCSVFileName);

		calcAllPath();

	}

	/**
	 * 向CSV文件中写入内容
	 * 
	 * @param mCSVPath
	 *            路径+文件
	 * @param mContent
	 *            内容
	 * @throws IOException
	 */
	public static void writeTxt(String mCSVPath, String mContent) throws IOException {
		FileWriter mFileWriter = new FileWriter(mCSVPath, true);
		BufferedWriter mBufferedWriter = new BufferedWriter(mFileWriter);
		mBufferedWriter.write(mContent);
		mBufferedWriter.flush();
		mFileWriter.flush();
		mBufferedWriter.close();
		mFileWriter.close();
	}

	/**
	 * 创建文件
	 * 
	 * @param mPath
	 *            文件路径
	 * @param mName
	 *            文件名.扩展名
	 */
	public static void createFile(String mPath, String mName) {

		File f = new File(mPath);
		if (!f.exists()) {
			f.mkdirs();
		}

		File file = new File(f, mName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获得文件名称
	 * 
	 * @param mLastName
	 *            后缀名
	 * @return 时间+后缀名
	 */
	public static String getSystemTime(String mLastName) {
		// 设置日期格式
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
		// new Date()为获取当前系统时间
		return df.format(new Date()) + mLastName;
	}

	private static void calcAllPath() throws IOException {
		// AllPath instance = new AllPath(eachLine, eachLine.length);
		// instance.start();
		DJSTL.main(eachLine, size);
	}

}
