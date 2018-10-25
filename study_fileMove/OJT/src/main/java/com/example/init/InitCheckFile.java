package com.example.init;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.example.base.BaseRun;

public abstract class InitCheckFile extends BaseRun {
	private static final Logger log = LoggerFactory.getLogger(InitCheckFile.class);
	@Autowired
	Environment env;

	/*
	 * point1: File.ioを使用
	 */
	public File[] init(String baseDir,String opponentDir) {
		File checkDir = null;

		
		log.info("処理ファイルディレクトリ:" + baseDir);
		log.info("出力先ディレクトリ:" + opponentDir);
		checkDir = new File(baseDir);
		if (!checkDir.exists() || !checkDir.isDirectory()) {
			return null;
		}
		checkDir = new File(opponentDir);
		if (!checkDir.exists() || !checkDir.isDirectory()) {
			if (!checkDir.mkdirs()) {
				return null;
			}
		}

		File dir = new File(baseDir);

		File[] Files = editFileList(dir.listFiles());

		return Files;
	}

	/*
	 * point1:DateFormatを使用した日付の整合性確認 point2:Mapを使用したソート処理
	 */
	private File[] editFileList(File[] listFiles) {

		List<File> fileList = new ArrayList<>();
		// 配送日の日付形式
		DateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		// あり得ない日付を除外する（例：6月31日）
		sdf1.setLenient(false);

		Map<String, File> fileMap = new HashMap<>();

		for (File p : listFiles) {
			if (!p.getName().matches(env.getProperty("csvName"))) {
				log.info(env.getProperty("csvName"));
				log.info(p.getName()+":<-ファイル名が「分類(3文字)_日付(yyyyMMdd).csv」パターンのみ有効です");
				continue;
			}
			try {

				// 配送日の形式（日付）チェック
				sdf1.parse(p.getName().substring(p.getName().length() - 12, p.getName().length() - 4));

				String f = p.getName().substring(p.getName().length() - 12, p.getName().length() - 4);
				//日付が同じファイルが存在することを想定
				fileMap.put(f + "_" + p.getAbsolutePath(), p);

			} catch (Exception e) {
				log.error(p.getName() + "の日付形式に誤りが存在します");
				continue;
			}
		}
		// ソート
		Object[] mapkey = fileMap.keySet().toArray();
		Arrays.sort(mapkey);

		// 配列からリストへ
		for (Object key : mapkey) {
			File f = fileMap.get(key.toString());
			fileList.add(f);
		}

		return (File[]) fileList.toArray(new File[0]);

	}


	public abstract Boolean exec(File[] files);

}
