package com.example.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.example.base.BaseExec1;
import com.example.main.OjtApplication1;
import com.orangesignal.csv.Csv;
import com.orangesignal.csv.CsvConfig;
import com.orangesignal.csv.handlers.ColumnNameMapListHandler;

@Service
public class InputService implements BaseExec1 {
	private static final Logger log = LoggerFactory.getLogger(InputService.class);
	@Autowired
	OutputService service;
	@Autowired
	Environment env;

	@Override
	public Boolean execDetails(File[] files) {
		String csvHeader = env.getProperty("csvHeader");
		CsvConfig conf = new CsvConfig(',', '"', '"');
		conf.setIgnoreEmptyLines(true);
		conf.setQuoteDisabled(true);
		conf.setEscapeDisabled(false);
		conf.setBreakString("\n");

		for (File file : files) {
			log.info("処理ファイル名:"+file.getName());
			// point1:try-with-resource文の使用。point2:インスタンス化の順番
			try (FileInputStream in = new FileInputStream(file.getAbsolutePath());
					InputStreamReader insr = new InputStreamReader(in, "SJIS");
					BufferedReader br = new BufferedReader(insr);) {
				if (!br.readLine().equals(csvHeader)) {
					log.error("ヘッダが異なります");
					continue;
				}
			} catch (IOException e) {
				log.error("エラー原因:" + e);
				continue;
			}
			List<Map<String, String>> csvLines = new ArrayList<>();
			try {
				csvLines = Csv.load(file, "SJIS", conf, new ColumnNameMapListHandler());
			} catch (Exception e) {
				log.warn("CSV名：" + file.getName() + "を読み込むことが出来ません");
				continue;
			}

			for (Map<String, String> csvLine : csvLines) {
				if (csvLine.get("名前").equals("tanaka"))
					csvLine.put("名前", "田中");
			}
			
			// 出力日時
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String dateTime = sdf.format(timestamp);

			String fileName = file.getName().substring(0, file.getName().length() - 4) + "_" + dateTime + ".csv";
			if (!service.execDetails(csvLines, env.getProperty("outputDir"), fileName, conf)) {
				log.error("失敗");
				return false;
			}
		}
		log.info("ファイル出力成功");
		return true;
	}

}
