package com.lee.alg;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.lee.minDistance.MinDistance0;
import com.lee.minDistance.Window;

/**
 * @ClassName csuft.ygzx.path AllPath
 * @Description: 求从A点B点所有可行的path（不能经过重复点） 若条件为不能经过重复的path， 注释(index != -1)的判断
 *               此种方法为递归方法，若点的数据过多，需改写成双重循环的方法（递归层级不宜太深）
 * @author DZ.Yang
 * @date 2016年10月17日 上午2:42:34
 * @version V1.0
 */
public class AllPath {

	// 将path构建成一个对称矩阵
	// int size = 11;
	// int[][] maze = new int[size][size];

	static DecimalFormat df4 = new DecimalFormat("0.0000");

	static int mSize;
	static double[][] mMaze;

	static String mAllPath = "";

	String mMinPath = "";

	String mEachLineMinPath = "";

	// 存放搜索时经过path的点
	List<Integer> path = new ArrayList<Integer>();
	int id = 0;

	static double mMinDistance;

	public AllPath(double[][] maze, int size) {

		AllPath.mMaze = maze;
		AllPath.mSize = size;

		// 打印出对称矩阵
		// System.out.println("源数据：");
		// for (int i = 0; i < mSize; ++i) {
		// for (int j = 0; j < mSize; ++j) {
		// System.out.print(mMaze[i][j] + " ");
		// }
		// System.out.println();
		// }
		// System.out.println("结果：");

	}

	/**
	 * 开始计算
	 * 
	 * @throws IOException
	 */
	void start() throws IOException {
		Window.inTable("开始计算路径");
		mMinPath += "strat,end,path1,length,path2,length,path3,length,path4,length,path5,length,path6,length,path7,length,path8,length,path9,length,path10,length," + "\n";
		for (int i = 0; i < mSize; i++) {
			for (int j = i + 1; j < mSize; j++) {
				Window.inTable("计算 " + (i + 1) + "----------->" + (j + 1) + " 的路径");
				mAllPath += (i + 1) + "----------->" + (j + 1) + "\n";
				mMinPath += (i + 1) + "," + (j + 1) + ",";
				// System.out.print(i + 1 + "----------->");
				// System.out.println(j + 1);
				mMinDistance = Integer.MAX_VALUE;
				this.pass(i, j, 0);
				mAllPath += "\n";
				mMinPath += mEachLineMinPath;
				mMinPath += "\n";
			}
		}

		String txtFileName = MinDistance0.getSystemTime("_全部路径") + ".txt";

		MinDistance0.createFile(MinDistance0.mOutputFileDir, txtFileName);

		MinDistance0.writeTxt(MinDistance0.mOutputFileDir + "//" + txtFileName, mAllPath);

		Window.inTable("全部路径输出完成：" + MinDistance0.mOutputFileDir + "//" + txtFileName);

		String txtFileName2 = MinDistance0.getSystemTime("_最短路径") + ".csv";

		MinDistance0.createFile(MinDistance0.mOutputFileDir, txtFileName2);

		MinDistance0.writeTxt(MinDistance0.mOutputFileDir + "//" + txtFileName2, mMinPath);

		Window.inTable("最短路径输出完成：" + MinDistance0.mOutputFileDir + "//" + txtFileName2);

	}

	/**
	 * 求得所有可行的path
	 * 
	 * @param start
	 *            开始点编号
	 * @param end
	 *            void 结束点编号
	 */
	void pass(int start, final int end, double distance) {
		String mEachLine = "";
		path.add(start);
		if (start == end) {// 找到一条path
			mAllPath += "path" + (++id) + "：";
			for (int i = 0, size = path.size(); i < size; ++i) {
				mAllPath += (path.get(i) + 1) + " ";
				mEachLine += (path.get(i) + 1) + " ";
			}
			mAllPath += "===>" + df4.format(distance) + "\n";
			mEachLine += "," + df4.format(distance) + ",";
			if (distance < mMinDistance) {
				mMinDistance = distance;
				mEachLineMinPath = "";
				mEachLineMinPath = mEachLine;
			} else if (distance == mMinDistance) {
				mEachLineMinPath += mEachLine;
			}
		} else {
			int index = -1;
			double temp;
			for (int i = 0; i < mSize; ++i) {
				if (mMaze[start][i] > 0) {
					index = path.indexOf(i);
					if (index != -1) {
						continue;
					}
					temp = mMaze[start][i];
					// 改写数据，表示此点已遍历过
					mMaze[start][i] = mMaze[i][start] = -1;
					pass(i, end, distance + temp);
					// System.out.println(i);
					// 还原数据
					mMaze[start][i] = mMaze[i][start] = temp;
				}
			}
		}
		path.remove(path.size() - 1);
	}

	// public static void main(String[] args) {
	// AllPath instance = new AllPath(mMaze, mSize);
	// for (int i = 0; i < instance.mSize; i++) {
	// for (int j = i + 1; j < instance.mSize; j++) {
	// instance.pass(i, j, 0);
	// }
	// }
	// System.out.println("************over************");
	// }

}
