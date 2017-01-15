package com.lee.minDistance;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

public class NetAna {
	public static void main(String[] args) {

		// args[]--元数据、输出
		Window.inTable("start===>" + getSystemTime());
		System.out.println("start===>" + getSystemTime());
		switch (Window.whichDialog) {
		case 1:
			MinDistance0.main(args);
			break;

		}
		System.out.println("over===>" + getSystemTime());
		Window.inTable("over===>" + getSystemTime());
		JOptionPane.showMessageDialog(null, "任务完成!", "提示信息", JOptionPane.INFORMATION_MESSAGE);
		Window.startJButton.setEnabled(true);
	}

	static String getSystemTime() {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		return df.format(new Date());
	}
}
