package com.example.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.example.base.BaseExec2;
import com.example.main.OjtApplication1;
import com.orangesignal.csv.Csv;
import com.orangesignal.csv.CsvConfig;
import com.orangesignal.csv.handlers.ColumnNameMapListHandler;

@Service
public class OutputService implements BaseExec2 {
	private static final Logger log = LoggerFactory.getLogger(OutputService.class);
	@Override
	public Boolean execDetails(List<Map<String, String>> csvLines,String dir,String fileName, CsvConfig conf) {
		try {
			Csv.save(csvLines, new File(dir+"\\"+fileName), conf, new ColumnNameMapListHandler());
		} catch (IOException e) {
			log.error("エラー原因:"+e);
			return false;
		}
		log.info("ファイル名:"+fileName+"を出力しました");
		return true;

	}

}
