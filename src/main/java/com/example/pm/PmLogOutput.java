package com.example.pm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.example.pm.PmCommon.Display;
import com.example.pm.PmCommon.Strings;

public class PmLogOutput {
	
	private File file;
	private FileWriter fileWriter;
	private PrintWriter printWriter;
	
	/**
	 * 現在日付を取得する
	 */
	private String getNowTime() {
		LocalDateTime nowTime = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		String nowTimeStr = nowTime.format(format);
		return nowTimeStr;
	}
	
	/**
	 * ファイルの共通オープン処理
	 */
	public void setUp() {
		file = new File("C:\\Users\\kingd\\Desktop\\フォルダ１\\sts workspace\\PasswordManager\\PmLog.log");
		try {
			fileWriter = new FileWriter(file, true);
			printWriter = new PrintWriter(new BufferedWriter(fileWriter));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ファイルの共通クローズ処理
	 */
	public void closeProcess() {
		printWriter.close();
		try {
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通常レベルのログ出力を行う
	 * @param displayName 画面名
	 */
	public void info(String displayName) {
		this.setUp();
		printWriter.println("INFO ".concat(getNowTime()).concat("  ").concat(displayName));
		this.closeProcess();
	}
	
	/**
	 * 通常レベルのログ出力を行う
	 * @param displayName 画面名
	 * @param process 処理名
	 */
	public void info(String displayName, String process) {
		this.setUp();
		if(Strings.BLANKVALUE.equals(displayName)) {
			printWriter.println("INFO ".concat(getNowTime()).concat("  ").concat(process));
		}
		else {
			printWriter.println("INFO ".concat(getNowTime()).concat("  ").concat(displayName).concat("  ").
					concat(process));
		}
		this.closeProcess();
	}
	
	/**
	 * 通常レベルのログ出力を行う（ログイン時専用）
	 * @param displayName 画面名
	 * @param userId  ユーザーID
	 * @param passWd  パスワード
	 */
	public void info(String displayName, String userId, String passWd, String passKey) {
		this.setUp();
		if(Display.PassKeyCert.equals(displayName)) {
			printWriter.println("INFO ".concat(getNowTime()).concat("  ").concat(displayName).
					concat("  ").concat("ユーザーID：").concat(userId).concat("  ").concat("パスワード：").concat(passWd));
		}
		else if(Display.TopPage.equals(displayName)){
			printWriter.println("INFO ".concat(getNowTime()).concat("  ").concat(displayName).
					concat("  ").concat("パスキー：").concat(passKey));
		}
		else {
			printWriter.println("INFO ".concat(getNowTime()).concat("  ").concat("ユーザーID：").concat(userId).
					concat("  ").concat("パスワード：").concat(passWd).concat("  ").concat("パスキー：").concat(passKey));
		}
		this.closeProcess();
	}
	
	/**
	 * 警告レベルのログ出力を行う
	 * @param displayName 画面名
	 * @param process 処理名
	 * @param warningContent 警告内容
	 */
	public void warning(String displayName, String process, String warningContent) {
		this.setUp();
		printWriter.println("WARN ".concat(getNowTime()).concat("  ").concat(displayName).
				concat("  ").concat(process).concat("  ").concat(warningContent));
		this.closeProcess();
	}
	
	/**
	 * エラーレベルのログ出力を行う
	 * @param displayName 画面名
	 * @param processResult 処理名
	 * @param errorContent エラー内容
	 * @param throwContent 例外内容
	 */
	public void error(String displayName, String process, String errorContent, Throwable throwContent) {
		this.setUp();
		printWriter.println("ERROR ".concat(getNowTime()).concat("  ").concat(displayName).
				concat("  ").concat(process).concat("  ").concat(errorContent));
		printWriter.println(throwContent.getMessage());
		this.closeProcess();
	}
}