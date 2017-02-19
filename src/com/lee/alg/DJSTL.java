package com.lee.alg;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lee.minDistance.MinDistance0;
import com.lee.minDistance.Window;

/**
 * Dijkstra算法
 * 
 * 算法来源：http://www.cnblogs.com/skywang12345/p/3711516.html
 * 备注：若点的数量过多且网络结构复杂，最有可能出现OOM内存溢出
 * 
 * @ClassName csuft.ygzx.path DJSTL
 * @Description: 求某一点至其它点的最短路径、所经过的点
 * @author DZ.Yang
 * @date 2016年11月28日 上午12:37:32
 * @version V1.0
 */
public class DJSTL {
	private int[][] netWork;

	private static String mCont;

	public DJSTL(int[][] Net) {
		this.netWork = Net;
	}

	// 无效值
	private final int INF = Integer.MAX_VALUE;

	// 网络结构
	// private int[][] netWork = new int[][] { //
	// { INF, 8, 32, INF, INF, INF }, //
	// { 8, INF, 9, 15, INF, INF }, //
	// { 32, 9, INF, 6, 13, INF }, //
	// { INF, 15, 6, INF, 7, 1 }, //
	// { INF, INF, 13, 7, INF, 6 }, //
	// { INF, INF, INF, 1, 6, INF },//
	// };

	public void dijkstra(int vs) {
		final int size = netWork.length;

		// flag[i]=true表示"顶点vs"到"顶点i"的最短路径已成功获取
		boolean[] flag = new boolean[size];
		List[] pathArray = new ArrayList[size];
		int[] dist = new int[size];// "顶点vs"到"顶点i"的距离
		String path = String.valueOf(vs);
		// 初始化
		for (int i = 0; i < size; i++) {
			flag[i] = false;
			dist[i] = netWork[vs][i];
			pathArray[i] = new ArrayList<String>();
			// 从1开始计数
			pathArray[i].add((Integer.valueOf(path) + 1) + "");
		}
		// 对"顶点vs"自身进行初始化
		flag[vs] = true;
		dist[vs] = 0;

		// 遍历len - 1次；每次找出一个顶点的最短路径。
		for (int i = 1, k = 0; i < size; i++) {
			// 寻找当前最小的路径(即在未获取最短路径的顶点中，找到离vs最近的顶点(k))
			int min = INF;
			for (int j = 0; j < size; j++) {
				if (!flag[j] && (dist[j] < min)) {
					min = dist[j];
					k = j;
				}
			}
			// 标记"顶点k"为已经获取到最短路径
			flag[k] = true;

			// 修正当前最短路径和前驱顶点(即当已经"顶点k的最短路径"之后，更新"未获取最短路径的顶点的最短路径和前驱顶点")
			for (int j = 0; j < size; j++) {
				if (netWork[k][j] == INF) {
					continue;
				}
				int temp = min + netWork[k][j];
				if (!flag[j]) {
					// 路径各元素编号+1
					List<String> tempPath = fullPath(pathArray[k], k + 1);
					if (temp < dist[j]) {
						dist[j] = temp;
						pathArray[j] = tempPath;
					} else if (temp == dist[j]) {
						pathArray[j].addAll(tempPath);
					}
				}
			}
		}

		// System.out.println("打印结果");
		for (int i = 0; i < size; ++i) {
			// 从1开始计数
			if (!((vs + 1) >= (i + 1))) {
				mCont += (vs + 1) + "," + (i + 1) + ",";
				// System.out.print((vs + 1) + "," + (i + 1) + ",");
				if (dist[i] == INF) {
					mCont += "没有路线";
					// System.out.print("没有路线");
				} else {
					for (int j = 0, pathSize = pathArray[i].size(); j < pathSize; ++j) {
						// System.out.print(pathArray[i].get(j) + " " + (i + 1)
						// + "," + dist[i] + ",");
						mCont += pathArray[i].get(j) + " " + (i + 1) + "," + ((double) dist[i] / 10000.0) + ",";
					}
					mCont += "\n";
				}
			}
		}
	}

	/**
	 * 补全路径（在前一个点的路径基础上，补全连接的点，得到这个点的路径）
	 * 
	 * @param pathArray
	 * @param k
	 * @return List
	 */
	private List fullPath(List<String> pathArray, int k) {
		List<String> resPathArray = new ArrayList<String>();
		String path;
		for (int i = 0, size = pathArray.size(); i < size; ++i) {
			path = (pathArray.get(i)) + " " + k;
			resPathArray.add(path);
		}
		return resPathArray;
	}

	/**
	 * @param args
	 *            关系矩阵
	 * @param Max
	 * @throws IOException
	 */
	public static void main(int[][] args, int Max) throws IOException {
		// public static void main(String[] argss) throws IOException {
		// int INF = Integer.MAX_VALUE;
		// int[][] args = new int[][] { //
		// { INF, 8, 32, INF, INF, INF }, //
		// { 8, INF, 9, 15, INF, INF }, //
		// { 32, 9, INF, 6, 13, INF }, //
		// { INF, 15, 6, INF, 7, 1 }, //
		// { INF, INF, 13, 7, INF, 6 }, //
		// { INF, INF, INF, 1, 6, INF }, //
		// };
		//
		// int Max = 6;
		String txtFileName2 = MinDistance0.getSystemTime("_最短路径") + ".csv";
		MinDistance0.createFile(MinDistance0.mOutputFileDir, txtFileName2);
		FileWriter mFileWriter = new FileWriter(MinDistance0.mOutputFileDir + "//" + txtFileName2, true);
		BufferedWriter mBufferedWriter = new BufferedWriter(mFileWriter);
		mCont = "strat,end,path1,length,path2,length,path3,length,path4,length,path5,length,path6,length,path7,length,path8,length,path9,length,path10,length,"
				+ "\n";
		mBufferedWriter.write(mCont);

		Window.inTable("最短路径文件创建完成：" + txtFileName2);
		Window.inTable("开始计算");

		DJSTL instance = new DJSTL(args);
		for (int vs = 0; vs < Max; vs++) {
			mCont = "";
			Window.inTable("正在计算元素 " + (vs + 1) + " 到其他元素最短路径");
			instance.dijkstra(vs);
			mBufferedWriter.write(mCont);
		}

		mBufferedWriter.flush();
		mFileWriter.flush();
		mBufferedWriter.close();
		mFileWriter.close();

		Window.inTable("最短路径输出完成：" + MinDistance0.mOutputFileDir + "//" + txtFileName2);
	}
}
