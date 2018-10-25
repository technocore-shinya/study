package com.example.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.example.base.BaseExec1;

@Service

public class MoveService implements BaseExec1 {
	private static final Logger log = LoggerFactory.getLogger(MoveService.class);
	@Autowired
	Environment env;

	public Boolean execDetails(File[] files) {
		String baseDir = env.getProperty("baseDir");
		String opponentDir = env.getProperty("moveDir");
		for (File file : files) {
			log.info(file.getName());
			// 現在のパス
			Path baseDirPath = Paths.get(baseDir + "/" + file.getName());
			// 移動先のパス
			Path moveDirPath = Paths.get(opponentDir + "/" + file.getName());
			try {
				//CopyOptionを上書き設定に指定する
				Files.move(baseDirPath, moveDirPath,StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				log.error("エラー原因"+e);
				return false;
			}

		}

		return true;
	}
}
